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
public class JsonSubscriberIdentity {
    private String imsi;
    private JsonAddressString msisdn;

    public JsonSubscriberIdentity() {
    }

    public JsonSubscriberIdentity(JsonObject obj) {
        JsonString s = obj.getJsonString("imsi");
        imsi = s != null ? s.getString() : null;

        JsonObject o = obj.getJsonObject("msisdn");
        msisdn = o != null ? new JsonAddressString(o) : null;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public JsonAddressString getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(JsonAddressString msisdn) {
        this.msisdn = msisdn;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");

        builder.append("\"imsi\"");
        builder.append(":");
        builder.append('"');
        builder.append(imsi);
        builder.append("\"");

        builder.append(",");
        builder.append("\"msisdn\"");
        builder.append(":");
        builder.append(msisdn);

        builder.append("}");

        return builder.toString();
    }
}
