/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.map.service.ussd;

import com.unifun.sigtran.ussdgateway.gw.Channel;
import com.unifun.sigtran.ussdgateway.gw.context.ExecutionContext;
import com.unifun.sigtran.ussdgateway.map.dto.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author okulikov
 */
public class TestUssdChannel implements Channel {
    private long operatorSpecificPUSSRInvokeId = 0;
    private State state = State.IDLE;
    private long id;

    @Override
    public void start() throws Exception {
    }

    @Override
    public Future send(String url, JsonMessage msg, ExecutionContext context) {
        long dialogId = msg.getTcap().getDialog().getDialogId();
        long invokeId = invokeId(msg);

        if (dialogId != id) {
            id = dialogId;
            state = State.IDLE;
        }

        final JsonTcapDialog dialog = new JsonTcapDialog();
        dialog.setDialogId(dialogId);

        final JsonTcap tcap = new JsonTcap();

        final JsonComponents components = new JsonComponents();

        final JsonDataCodingScheme dc = new JsonDataCodingScheme();
        dc.setCodingGroup("GeneralGsm7");
        dc.setLanguage("UCS2");

        final JsonMapOperation op = new JsonMapOperation();
        op.setCodingScheme(dc);

        final JsonComponent component = new JsonComponent();
        components.addComponent(component);

        switch (state) {
            case IDLE:
                op.setUssdString("Welcome to Unifun\n Dial 1 or 2");

                component.setType("invoke");
                component.setValue(new JsonInvoke(invokeId, new JsonMap("unstructured-ss-request", op)));

                tcap.setType("Continue");
                state = State.FINISH;
                break;
            case FINISH:
                op.setUssdString("You have dialed " + ussdString(msg));

                component.setType("returnResultLast");
                component.setValue(new JsonReturnResultLast(invokeId, new JsonMap("process-unstructured-ss-request", op)));

                tcap.setType("End");
                state = State.IDLE;
                break;
        }

        tcap.setDialog(dialog);
        tcap.setComponents(components);

        final JsonMessage response = new JsonMessage();
        response.setTcap(tcap);

        context.completed(response);

        return new Future() {
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                return false;
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isDone() {
                return true;
            }

            @Override
            public Object get() throws InterruptedException, ExecutionException {
                return null;
            }

            @Override
            public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                return null;
            }
        };
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

    private enum State {
        IDLE, CONTINUE, FINISH
    }

}
