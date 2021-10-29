/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.gw;

import com.unifun.sigtran.ussdgateway.gw.context.ExecutionContext;
import com.unifun.sigtran.ussdgateway.map.dto.*;
import com.unifun.sigtran.ussdgateway.map.service.ussd.OcsTranslator;
import com.unifun.sigtran.ussdgateway.util.URL;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

/**
 * @author okulikov
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class LocalChannel implements Channel {
    private final Gateway gateway;
    private final OcsTranslator ocsTranslator = new OcsTranslator();

    @Override
    public void start() throws Exception {
    }

    @Override
    public synchronized Future send(String url, JsonMessage msg, ExecutionContext context) {
        long dialogID = msg.getTcap().getDialog().getDialogId();
        long invokeID = invokeID(msg.getTcap());

        //!IMPORTANT
        //To be able to send back response we need to reverse calling and
        //called addresses
        JsonSccpAddress calledPartyAddress = msg.getSccp().getCallingPartyAddress();
        JsonSccpAddress callingPartyAddress = msg.getSccp().getCalledPartyAddress();

        if (log.isInfoEnabled()) {
            log.info(String.format("(DID:%d) ---> %s", dialogID, url));
        }

        URL uri = new URL(url);

        JsonMessage query = null;
        switch (uri.host()) {
            case "ocs":
                try {
                    query = ocsTranslator.translate(uri, msg, gateway.getOcsPool().checkAndGetJsonMessage());
                } catch (Exception e) {
                    log.warn(String.format("(DID:%d) ---> %s", dialogID, "Translation failure"), e);
                }
                break;
            default:
                context.failed(new IllegalArgumentException(url));
                return null;
        }

        if (log.isDebugEnabled()) {
            log.debug("Translated message: " + query);
        }

        //Start the asynchronous execution!
        //Asynchronous execution allows to leave methos Servlet.service() immediately
        //without holding this thread and container will be able to recycle this thread
        //for receiving incoming messages.
        //Final HTTP response will be handled later (when it will actually arrive)
        //by callback object HttpLocalContext
        try {
            Channel channel = gateway.channel("map://ussd");
            //send received message over map with async callback handler
            return channel.send("map://ussd", query, new LocalContext(calledPartyAddress, callingPartyAddress, dialogID, invokeID, context));
        } catch (UnknownProtocolException e) {
            log.warn("Could not send message:" + e.getMessage());
            context.failed(e);
        }

        return null;
    }

    @Override
    public void stop() {
    }

    private long invokeID(JsonTcap tcap) {
        JsonComponent component1 = tcap.getComponents().getComponent(0);
        switch (component1.getType()) {
            case "invoke":
                JsonInvoke invoke = (JsonInvoke) component1.getValue();
                return invoke.getInvokeId();
            case "returnResultLast":
                JsonReturnResultLast returnResultLast = (JsonReturnResultLast) component1.getValue();
                return returnResultLast.getInvokeId();
        }
        return 0;
    }

    protected class LocalContext implements ExecutionContext {

        private final JsonSccpAddress calledPartyAddress;
        private final JsonSccpAddress callingPartyAddress;
        private final long dialogId;
        private final long invokeID;
        private final ExecutionContext context;

        public LocalContext(JsonSccpAddress calledPartyAddress, JsonSccpAddress callingPartyAddress, long dialogId, long invokeID, ExecutionContext context) {
            this.calledPartyAddress = calledPartyAddress;
            this.callingPartyAddress = callingPartyAddress;
            this.dialogId = dialogId;
            this.invokeID = invokeID;
            this.context = context;
        }

        @Override
        public void completed(JsonMessage msg) {
            try {
                if (msg.getTcap().getType().equals("Abort")) {
                    context.failed(new Exception(msg.getTcap().getAbortMessage()));
                } else {
                    msg.getSccp().setCalledPartyAddress(calledPartyAddress);
                    msg.getSccp().setCallingPartyAddress(callingPartyAddress);

                    msg.getTcap().getDialog().setDialogId(dialogId);
                    JsonComponent component1 = msg.getTcap().getComponents().getComponent(0);
                    switch (component1.getType()) {
                        case "invoke":
                            JsonInvoke invoke = (JsonInvoke) component1.getValue();
                            invoke.setInvokeId(invokeID);
                            break;
                        case "returnResultLast":
                            JsonReturnResultLast returnResultLast = (JsonReturnResultLast) component1.getValue();
                            returnResultLast.setInvokeId(invokeID);
                            break;
                        case "returnError":
                            log.warn("Class of ReturnError" + component1.getValue().getClass());
                            JsonReturnError returnError = (JsonReturnError) component1.getValue();
                            returnError.setInvokeId(invokeID);
                            break;
                    }
                    context.completed(msg);
                }
            } catch (Exception e) {
                context.failed(e);
            }
        }

        @Override
        public void failed(Exception e) {
            log.warn("Context failed----, notify : " + context);
            context.failed(e);
        }

        @Override
        public void cancelled() {
            context.cancelled();
        }

        @Override
        public void ack(JsonMessage msg) {
        }

        public long getDialogId() {
            return dialogId;
        }
    }
}
