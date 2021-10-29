/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.gw;

import com.unifun.sigtran.ussdgateway.gw.context.ExecutionContext;
import com.unifun.sigtran.ussdgateway.map.dto.JsonMessage;
import com.unifun.sigtran.ussdgateway.properties.AppProperties;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.DefaultHttpClientIODispatch;
import org.apache.http.impl.nio.pool.BasicNIOConnPool;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.protocol.BasicAsyncRequestProducer;
import org.apache.http.nio.protocol.BasicAsyncResponseConsumer;
import org.apache.http.nio.protocol.HttpAsyncRequestExecutor;
import org.apache.http.nio.protocol.HttpAsyncRequester;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOEventDispatch;
import org.apache.http.protocol.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonParsingException;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Future;

/**
 * @author okulikov
 */
@Data
@Log4j2
@Component
@RequiredArgsConstructor
public class HttpChannel implements Channel {

    private static final Logger LOGGER_HTTP = LoggerFactory.getLogger("HttpLogger");
    private static final String HTTP_LOGGING_STRING_FORMAT_RECEIVED = "[HTTP-RESP] [STATUS CODE: %36s] [%12s] [DialogId: %11s] [PC: %5s] [SSN: %4s] [GT: %13s] [Rx: \"%-10s\"]";
    private static final String HTTP_LOGGING_STRING_FORMAT_TRANSMITTED = "[HTTP-POST] [URL: %44s] [%12s] [DialogId: %11s] [PC: %5s] [SSN: %4s] [GT: %13s] [Tx: \"%-10s\"]";
    private final AppProperties appProperties;
    private HttpAsyncRequester requester;
    private BasicNIOConnPool pool;
    private ConnectingIOReactor ioReactor;


    @Override
    @PostConstruct
    public void start() throws Exception {
        HttpProcessor httpproc = HttpProcessorBuilder.create()
                // Use standard client-side protocol interceptors
                .add(new RequestContent())
                .add(new RequestTargetHost())
                .add(new RequestConnControl())
                .add(new RequestUserAgent("Ussd-Gateway/3.0"))
                /*.add(new RequestExpectContinue(true))*/.build();

        // Create client-side HTTP protocol handler
        HttpAsyncRequestExecutor protocolHandler = new HttpAsyncRequestExecutor();

        // Create client-side I/O event dispatch
        final IOEventDispatch ioEventDispatch = new DefaultHttpClientIODispatch(
                protocolHandler,
                ConnectionConfig.DEFAULT);

        // Create client-side I/O reactor
        IOReactorConfig config = IOReactorConfig.custom()
                .setTcpNoDelay(true)
                .setSoTimeout(appProperties.getEmbeddedTomcatConnectionTimeout())
                .setSoReuseAddress(true)
                .setConnectTimeout(appProperties.getEmbeddedTomcatConnectionTimeout())
                .build();
        ioReactor = new DefaultConnectingIOReactor(config);

        // Create HTTP connection pool
        pool = new BasicNIOConnPool(ioReactor, appProperties.getEmbeddedTomcatConnectionTimeout(), ConnectionConfig.DEFAULT);

        // Limit total number of connections
        pool.setDefaultMaxPerRoute(appProperties.getEmbeddedTomcatMaxConnections());
        pool.setMaxTotal(appProperties.getEmbeddedTomcatMaxConnections());

        // Run the I/O reactor in a separate thread
        new Thread(() -> {
            try {
                // Ready to go!
                ioReactor.execute(ioEventDispatch);
            } catch (InterruptedIOException ex) {
                log.warn("Interrupted I/O reactor", ex);
            } catch (IOException e) {
                log.warn("I/O error: " + e.getMessage(), e);
            }
        }).start();

        requester = new HttpAsyncRequester(httpproc);
    }

    @Override
    public Future send(String uri, JsonMessage msg, ExecutionContext context) {
        URL url;

        try {
            url = new URL(uri);
        } catch (MalformedURLException e) {
            context.failed(e);
            return null;
        }

        HttpHost target = new HttpHost(url.getHost(), url.getPort(), url.getProtocol());
        HttpPost post = new HttpPost(url.toExternalForm());
        post.setHeader("Content-Type", "application/json");

        long dialogId = msg.getTcap().getDialog().getDialogId();


        if (log.isInfoEnabled()) {
            log.info(String.format("(DID:%d) ---> %s", dialogId, uri));
        }

        String content = msg.toString();
        try {
            post.setEntity(new StringEntity(content));
        } catch (UnsupportedEncodingException e) {
            log.warn("incorrect Encoding  by set Entity", e);
        }

        if (log.isTraceEnabled()) {
            log.trace("TX: " + content);
        }


        HttpCoreContext coreContext = HttpCoreContext.create();

        logOnHttpPostSend(uri, msg, dialogId);

        return requester.execute(
                new BasicAsyncRequestProducer(target, post),
                new BasicAsyncResponseConsumer(),
                pool,
                coreContext,
                // Handle HTTP response from a callback
                new ResponseHandler(dialogId, context));
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

    @Override
    public void stop() {
        try {
            ioReactor.shutdown();
        } catch (Exception e) {
            log.error("error shutdown ioReactor", e);
        }
    }

    private String text(HttpEntity entity) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        InputStream in = entity.getContent();
        int b;
        while ((b = in.read()) != -1) {
            bout.write(b);
        }
        return bout.toString();
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


    private class ResponseHandler implements FutureCallback<HttpResponse> {

        private final long dialogId;
        private final ExecutionContext context;

        public ResponseHandler(long dialogId, ExecutionContext context) {
            this.dialogId = dialogId;
            this.context = context;
        }


        @Override
        public void completed(HttpResponse response) {
            int statusCode = response.getStatusLine().getStatusCode();

            if (log.isInfoEnabled()) {
                log.info("(DID: " + dialogId + ") <--- " + response.getStatusLine());
            }

            switch (statusCode) {
                case 200:
                    String reply = "";
                    try {
                        reply = text(response.getEntity());

                        JsonObject responseJsonObject;
                        try (JsonReader reader = Json.createReader(
                                new InputStreamReader(
                                        new ByteArrayInputStream(reply.getBytes()))
                        )) {
                            responseJsonObject = reader.readObject();
                        }
                        JsonMessage resp = new JsonMessage(responseJsonObject);
                        if (log.isTraceEnabled()) {
                            log.trace("RX: " + resp);
                            log.trace("RX:reply " + reply);
                        }

                        logOnHttpOkResponse(statusCode, responseJsonObject);

                        context.completed(resp);
                    } catch (JsonParsingException e) {
                        log.error("Syntax error for message " + reply + " at position " + e.getLocation().getStreamOffset() +
                                " near " + area(reply, (int) e.getLocation().getStreamOffset()), e);
                        logReceivingError(statusCode, e);
                    } catch (IOException | UnsupportedOperationException e) {
                        log.error("Could not read message: ", e);
                        logReceivingError(statusCode, e);
                        context.failed(e);
                    }
                    break;
                default:
                    if (log.isInfoEnabled()) {
                        log.info("(Dialog-Id): " + dialogId + " trying spare destination");
                    }
                    context.failed(new IOException("Response: " + statusCode));
            }

        }

        private void logOnHttpOkResponse(int statusCode, JsonObject resp) {
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
                logReceivingError(statusCode, e);
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


        private void logReceivingError(int statusCode, Exception e) {
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

        @Override
        public void failed(Exception e) {
            log.error("(Dialog-Id): " + dialogId + " Transmission failure: {}", e);
            if (log.isInfoEnabled()) {
                log.info("(Dialog-Id): " + dialogId + " trying spare destination");
            }
            logReceivingError(404, e);
            context.failed(e);
        }

        @Override
        public void cancelled() {
            context.cancelled();
        }

    }
}
