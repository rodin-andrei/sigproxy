package com.unifun.sigtran.ussdgateway.gw;

import com.unifun.sigtran.ussdgateway.gw.context.ExecutionContext;
import com.unifun.sigtran.ussdgateway.map.dto.JsonMessage;
import com.unifun.sigtran.ussdgateway.properties.AppProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import javax.annotation.PostConstruct;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonParsingException;
import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.time.Duration;
import java.util.concurrent.Future;

/**
 * @author asolopa
 */
@Component
@RequiredArgsConstructor
@Log4j2
public class CustomHttpChannel implements Channel {
    private static final Logger LOGGER_HTTP = LoggerFactory.getLogger("HttpLogger");
    private static final String HTTP_LOGGING_STRING_FORMAT_RECEIVED = "[HTTP-RESP] [STATUS CODE: %36s] [%12s] [DialogId: %11s] [PC: %5s] [SSN: %4s] [GT: %13s] [Rx: \"%-10s\"]";
    private static final String HTTP_LOGGING_STRING_FORMAT_TRANSMITTED = "[HTTP-POST] [URL: %44s] [%12s] [DialogId: %11s] [PC: %5s] [SSN: %4s] [GT: %13s] [Tx: \"%-10s\"]";
    private final AppProperties appProperties;
    private WebClient webClient;

    @PostConstruct
    private void setupWebClient() {
        ConnectionProvider myConnectionPool = ConnectionProvider.builder("customHttpChannelWebClientConnectionPool")
                .maxConnections(appProperties.getCustomHttpChannelWebClientMaxConnections())
                .pendingAcquireMaxCount(appProperties.getCustomHttpChannelWebClientPendingAcquireMaxCount())
                .build();
        HttpClient httpClient = HttpClient
                .create(myConnectionPool)
                .responseTimeout(Duration.ofSeconds(appProperties.getCustomHttpChannelWebClientResponseTimeout()));
        this.webClient = WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient)).build();
    }

    @Override
    public void start() throws Exception {
        /*No need to init this channel*/
    }

    @Override
    public Future send(String uri, JsonMessage msg, ExecutionContext context) {

        webClient
                .post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(msg.toString()))
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> {
                    Exception e = new Exception(clientResponse.statusCode().getReasonPhrase());
                    context.failed(e);
                    logReceivingError(clientResponse.rawStatusCode(), e, msg.getTcap().getDialog().getDialogId());
                    return Mono.empty();
                })
                .bodyToMono(String.class)
                .subscribe(s -> {
                    try (JsonReader reader = Json.createReader(new ByteArrayInputStream(s.getBytes()))) {
                        JsonObject obj = reader.readObject();
                        JsonMessage jsonMessage = new JsonMessage(obj);
                        context.completed(jsonMessage);
                        logOnHttpOkResponse(200, obj, jsonMessage.getTcap().getDialog().getDialogId());
                    } catch (JsonParsingException e) {
                        log.error("Syntax error for message " + s + " at position " + e.getLocation().getStreamOffset() +
                                " near " + area(s, (int) e.getLocation().getStreamOffset()));
                        logReceivingError(200, e, msg.getTcap().getDialog().getDialogId());
                        context.failed(e);
                    }
                });


        log.info(String.format("(DID:%d) ---> %s", msg.getTcap().getDialog().getDialogId(), uri));
        log.trace("TX: " + msg);
        logOnHttpPostSend(uri, msg, msg.getTcap().getDialog().getDialogId());


        return null;
    }

    @Override
    public void stop() {
        /*No need to stop this channel explicit*/
    }

    private void logOnHttpPostSend(String uri, JsonMessage msg, long dialogId) {
        try {
            String msisdn = null;
            String component = null;

            if (msg != null) {
                if (msg.getTcap() != null) {
                    if (msg.getTcap().getDialog() != null) {
                        if (msg.getTcap().getDialog().getMsisdn() != null) {
                            msisdn = msg.getTcap().getDialog().getMsisdn().getAddress();
                        } else if (msg.getTcap().getComponents() != null && msg.getTcap().getComponents().getSize() >= 1) {
                            msisdn = getMsisdn(msg.getTcap().getComponents().getComponent(0).getValue().toString());
                        }
                    }
                    if (msg.getTcap().getComponents() != null && msg.getTcap().getComponents().getSize() >= 1) {
                        if (msg.getTcap().getComponents().getComponent(0).getValue() != null) {
                            component = msg.getTcap().getComponents().getComponent(0).getValue().toString();
                        }
                    }
                }
            }

            LOGGER_HTTP.info(String.format(HTTP_LOGGING_STRING_FORMAT_TRANSMITTED,
                    uri,
                    msisdn,
                    dialogId,
                    msg.getSccp().getCalledPartyAddress().getPc(),
                    msg.getSccp().getCalledPartyAddress().getSsn(),
                    msg.getSccp().getCalledPartyAddress().getGlobalTitle().getDigits(),
                    getUssdString(component)
            ));
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            LOGGER_HTTP.error(String.format(HTTP_LOGGING_STRING_FORMAT_TRANSMITTED,
                    uri,
                    null,
                    dialogId,
                    null,
                    null,
                    null,
                    e.getMessage()
            ));
        }
    }

    private String area(String text, int pos) {
        int p1 = Math.max(0, pos - 1);
        int p2 = Math.min(text.length(), pos + 1);
        return text.substring(p1, p2);
    }

    private String getUssdString(String value) {
        try {
            if (value == null || "".equals(value)) {
                return "null";
            }
            try (JsonReader jsonReader = Json.createReader(new StringReader(value))) {
                return jsonReader.readObject()
                        .getJsonObject("component")
                        .getJsonObject("args")
                        .getString("ussdString");
            }
        } catch (Exception e) {
            return "null";
        }
    }


    private String getMsisdn(String value) {
        if (value == null || "".equals(value)) {
            return "null";
        }
        try (JsonReader jsonReader = Json.createReader(new StringReader(value))) {
            return jsonReader.readObject()
                    .getJsonObject("component")
                    .getJsonObject("args")
                    .getJsonObject("msisdn")
                    .getString("address");
        } catch (NullPointerException | JsonParsingException e) {
            return "null";
        }
    }

    private void logOnHttpOkResponse(int statusCode, JsonObject resp, long dialogId) {
        try {

            LOGGER_HTTP.info(String.format(HTTP_LOGGING_STRING_FORMAT_RECEIVED,
                    statusCode,
                    getMsisdn(resp),
                    dialogId,
                    null,
                    null,
                    null,
                    getUssdString(resp)
            ));

        } catch (NullPointerException | IndexOutOfBoundsException e) {
            logReceivingError(statusCode, e, dialogId);
        }
    }

    private String getUssdString(JsonObject value) {
        try {
            return value
                    .getJsonObject("tcap")
                    .getJsonArray("components")
                    .getJsonObject(0)
                    .getJsonObject("component")
                    .getJsonObject("value")
                    .getJsonObject("component")
                    .getJsonObject("args")
                    .getString("ussdString");
        } catch (Exception e) {
            return "null";
        }
    }

    private String getMsisdn(JsonObject value) {
        try {
            return value
                    .getJsonObject("tcap")
                    .getJsonArray("components")
                    .getJsonObject(0)
                    .getJsonObject("component")
                    .getJsonObject("value")
                    .getJsonObject("component")
                    .getJsonObject("args")
                    .getJsonObject("msisdn")
                    .getString("address");
        } catch (NullPointerException | JsonParsingException e) {
            return "null";
        }
    }


    private void logReceivingError(int statusCode, Exception e, long dialogId) {
        LOGGER_HTTP.error(String.format(HTTP_LOGGING_STRING_FORMAT_RECEIVED,
                statusCode,
                null,
                dialogId,
                null,
                null,
                null,
                e.getMessage()
        ));
    }

}
