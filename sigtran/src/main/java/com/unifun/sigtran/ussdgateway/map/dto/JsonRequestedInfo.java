/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.map.dto;

import javax.json.JsonObject;

/**
 * @author okulikov
 */
public class JsonRequestedInfo {
    private boolean imei;

    public JsonRequestedInfo() {
    }

    public JsonRequestedInfo(JsonObject obj) {
        imei = obj.getBoolean("imei");
    }

    public boolean isImei() {
        return imei;
    }

    public void setImei(boolean imei) {
        this.imei = imei;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");

        builder.append("\"imei\"");
        builder.append(":");
        builder.append(imei);

        builder.append("}");

        return builder.toString();
    }
}
