package com.unifun.sigtran.ussdgateway.map.service.ussd;

import com.unifun.sigtran.ussdgateway.gw.Channel;
import com.unifun.sigtran.ussdgateway.gw.context.ExecutionContext;
import com.unifun.sigtran.ussdgateway.gw.context.UssdLocalContext;
import com.unifun.sigtran.ussdgateway.map.dto.JsonMessage;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * @author asolopa
 */
public class SendAbortUssdChannel implements Channel {

    @Override
    public void start() throws Exception {
    }

    @Override
    public Future send(String url, JsonMessage msg, ExecutionContext context) {
        ((UssdLocalContext) context).abort();
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public void stop() {
    }

}
