/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.gw.config;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * @author okulikov
 */
public class DialogConfig {

    private int dialogTimeout;
    private int invokeTimeout;
    private int maxDialogs;

    public DialogConfig() {
        dialogTimeout = 30000;
        invokeTimeout = 30000;
        maxDialogs = Integer.MAX_VALUE;
    }

    public DialogConfig(JsonObject obj) {
        dialogTimeout = obj.getInt("dialog-timeout");
        invokeTimeout = obj.getInt("invoke-timeout");
        maxDialogs = obj.getInt("max-dialogs");
    }

    public int getDialogTimeout() {
        return dialogTimeout;
    }

    public void setDialogTimeout(int dialogTimeout) {
        this.dialogTimeout = dialogTimeout;
    }

    public int getInvokeTimeout() {
        return invokeTimeout;
    }

    public void setInvokeTimeout(int invokeTimeout) {
        this.invokeTimeout = invokeTimeout;
    }

    public int getMaxDialogs() {
        return maxDialogs;
    }

    public void setMaxDialogs(int maxDialogs) {
        this.maxDialogs = maxDialogs;
    }

    public JsonObjectBuilder toJson() {
        JsonObjectBuilder builder = Json.createObjectBuilder();

        builder.add("dialog-timeout", dialogTimeout);
        builder.add("invoke-timeout", invokeTimeout);
        builder.add("max-dialogs", maxDialogs);

        return builder;
    }
}
