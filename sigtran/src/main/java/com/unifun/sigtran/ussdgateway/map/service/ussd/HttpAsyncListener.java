package com.unifun.sigtran.ussdgateway.map.service.ussd;

import com.unifun.sigtran.ussdgateway.gw.context.ExecutionContext;
import com.unifun.sigtran.ussdgateway.gw.context.HttpLocalContext;
import lombok.extern.log4j.Log4j2;
import org.restcomm.protocols.ss7.map.api.MAPException;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import java.io.IOException;

@Log4j2
public class HttpAsyncListener implements AsyncListener {
    private final HttpLocalContext httpLocalContext;

    HttpAsyncListener(ExecutionContext executionContext) {
        this.httpLocalContext = (HttpLocalContext) executionContext;
    }

    @Override
    public void onComplete(AsyncEvent asyncEvent) {
    }

    @Override
    public void onTimeout(AsyncEvent asyncEvent) throws IOException {
        log.warn("ussd message processing time out");

        try {
            log.info("Closing MAP dialog by timeout");
            //TODO set sendAbortTimeout As property
            httpLocalContext.getMapDialog().close(false);
        } catch (MAPException e) {
            log.warn("Error closing MAP dialog by timeout", e);
        }

        asyncEvent.getSuppliedResponse().getWriter().println("{\"error\":\"answer timeout\", \"tcapDialogID\": " +
                this.httpLocalContext.getMapDialog().getLocalDialogId() + "}");
    }

    @Override
    public void onError(AsyncEvent asyncEvent) {
        log.warn("error handling ussd messages");
    }

    @Override
    public void onStartAsync(AsyncEvent asyncEvent) {

    }
}
