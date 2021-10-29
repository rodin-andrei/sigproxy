/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.map.dto;

import javax.json.JsonObject;
import javax.json.JsonString;

/**
 * @author okulikov
 */
public class JsonSubscriberInfo {
    private String imei;

    public JsonSubscriberInfo() {
    }

    public JsonSubscriberInfo(JsonObject obj) {
        JsonString s = obj.getJsonString("imei");
        imei = s != null ? s.getString() : null;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");

        builder.append("\"imei\"");
        builder.append(":");
        builder.append('"');
        builder.append(imei);
        builder.append("\"");

        builder.append("}");

        return builder.toString();
    }
}
