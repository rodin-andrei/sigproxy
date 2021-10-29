package com.unifun.sigtran.ussdgateway.map.service.ussd;

import com.unifun.sigtran.ussdgateway.gw.Channel;
import com.unifun.sigtran.ussdgateway.gw.context.ExecutionContext;
import com.unifun.sigtran.ussdgateway.gw.context.UssdLocalContext;
import com.unifun.sigtran.ussdgateway.map.dto.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * @author asolopa
 */
public class SendEmptyResponseUssdChannel implements Channel {
    private long operatorSpecificPUSSRInvokeId = 0;
    private long id;

    @Override
    public void start() throws Exception {
    }

    @Override
    public Future send(String url, JsonMessage msg, ExecutionContext context) {
        ((UssdLocalContext) context).empty();
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public void stop() {
    }

    private long invokeId(JsonMessage msg) {
        try {
            this.operatorSpecificPUSSRInvokeId = ((JsonInvoke) msg.getTcap().getComponents().getComponent(0).getValue()).getInvokeId();
            return this.operatorSpecificPUSSRInvokeId;
        } catch (ClassCastException e) {
            return this.operatorSpecificPUSSRInvokeId;
        }
    }

    private String ussdString(JsonMessage msg) {
        JsonComponent component1 = msg.getTcap().getComponents().getComponent(0);

        JsonMap map;
        try {
            JsonInvoke invoke = (JsonInvoke) component1.getValue();
            map = (JsonMap) invoke.component();
        } catch (ClassCastException e) {
            JsonReturnResultLast returnResultLast = (JsonReturnResultLast) component1.getValue();
            map = (JsonMap) returnResultLast.component();
        }
        return ((JsonMapOperation) map.operation()).getUssdString();
    }


}
