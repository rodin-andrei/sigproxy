package com.unifun.sigtran.ussdgateway.service.sendrequest.impl;

import com.unifun.sigtran.ussdgateway.gw.Channel;
import com.unifun.sigtran.ussdgateway.gw.context.HttpCompletableFutureContext;
import com.unifun.sigtran.ussdgateway.map.dto.JsonMessage;
import com.unifun.sigtran.ussdgateway.service.sendrequest.SendUssdRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * @author asolopa
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class SendUssdRequestServiceImpl implements SendUssdRequestService {
    private static final Logger LOGGER_HTTP = LoggerFactory.getLogger("HttpLogger");
    private static final String HTTP_LOGGING_STRING_FORMAT_RECEIVED = "[HTTP-GET ] [URL: %44s] [%12s] [DialogId: %11s] [PC: %5s] [SSN: %4s] [GT: %13s] [Tx: \"%-10s\"]";
    private final Channel mapChannel;

    @Override
    public CompletableFuture<String> sendUssdRequest(String requestJsonBody) {
        HttpCompletableFutureContext futureContext = this.getNewHttpCompletableFutureContext();
        mapChannel.send("map://ussd", new JsonMessage(requestJsonBody), futureContext);
        return futureContext.getCompletable();
    }


    @Lookup
    public HttpCompletableFutureContext getNewHttpCompletableFutureContext() {
        return null;
    }

}
