/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.gw;

import com.unifun.sigtran.ussdgateway.gw.context.ExecutionContext;
import com.unifun.sigtran.ussdgateway.map.dto.JsonTcapDialog;
import com.unifun.sigtran.ussdgateway.map.service.ussd.UssdRoute;

/**
 * @author okulikov
 */
public class LocalDialog {
    private boolean remoteOriginated;
    private JsonTcapDialog dialog;
    private UssdRoute route;
    private ExecutionContext context;
    private String msisdn;

    public LocalDialog(JsonTcapDialog dialog) {
        this.remoteOriginated = true;
        this.dialog = dialog;
        msisdn = dialog.getMsisdn() != null ? dialog.getMsisdn().getAddress() : null;
    }

    public JsonTcapDialog getDialog() {
        return dialog;
    }

    public void setDialog(JsonTcapDialog dialog) {
        this.dialog = dialog;
    }

    public UssdRoute getRoute() {
        return route;
    }

    public void setRoute(UssdRoute route) {
        this.route = route;
    }

    public ExecutionContext getContext() {
        return context;
    }

    public void setContext(ExecutionContext context) {
        this.context = context;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public boolean isRemoteOriginated() {
        return remoteOriginated;
    }

    public void setRemoteOriginated(boolean remoteOriginated) {
        this.remoteOriginated = remoteOriginated;
    }

    @Override
    public String toString() {
        return msisdn == null ? "DID:" + dialog.getDialogId() : "DID:" + dialog.getDialogId() + ", MSISDN:" + msisdn;
    }
}
