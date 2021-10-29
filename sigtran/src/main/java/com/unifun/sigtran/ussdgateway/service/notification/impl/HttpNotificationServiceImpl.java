package com.unifun.sigtran.ussdgateway.service.notification.impl;

import com.unifun.sigtran.ussdgateway.service.notification.HttpNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

/**
 * @author asolopa
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class HttpNotificationServiceImpl implements HttpNotificationService {
    private final WebClient notifyClientsWebClient;

    @Override
    @Async
    public void notifyClients(List<String> uriList,
                              String contentType,
                              HttpMethod httpMethod,
                              String body,
                              MultiValueMap<String, String> getParameters) {

        uriList.forEach(uri -> {

            URI customUri = UriComponentsBuilder.fromUri(URI.create(uri)).queryParams(getParameters).build().toUri();
            WebClient.RequestBodySpec request = notifyClientsWebClient.method(httpMethod)
                    .uri(customUri)
                    .header(HttpHeaders.CONTENT_TYPE, contentType);
            if (body != null) {
                request.body(BodyInserters.fromValue(body));
            }
            final Mono<String> response = request.
                    exchangeToMono(clientResponse -> {
                        if (clientResponse.statusCode().equals(HttpStatus.OK)) {
                            return clientResponse.bodyToMono(String.class);
                        } else if (clientResponse.statusCode().is4xxClientError()) {
                            log.error("Can't send notification"
                                    + " to address: "
                                    + uri + " Reason: " + clientResponse.statusCode().getReasonPhrase());
                            return Mono.just("Error clientResponse: " + clientResponse.statusCode().getReasonPhrase());
                        } else {
                            log.error("Can't send notification"
                                    + " to address: "
                                    + uri + " Reason: " + clientResponse.statusCode().getReasonPhrase());
                            return clientResponse.createException().flatMap(Mono::error);
                        }
                    });
            response.
                    subscribe(s -> log.info("Response from notified uri {} is {}", uri, s));
        });
    }
}
