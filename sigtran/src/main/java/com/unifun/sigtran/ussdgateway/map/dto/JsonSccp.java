/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.map.dto;

import javax.json.JsonObject;
import java.io.Serializable;

/**
 * @author okulikov
 */
public class JsonSccp implements Serializable {
    private JsonSccpAddress callingPartyAddress;
    private JsonSccpAddress calledPartyAddress;

    public JsonSccp() {
    }

    public JsonSccp(JsonObject obj) {
        callingPartyAddress = new JsonSccpAddress(obj.getJsonObject("calling-party-address"));
        calledPartyAddress = new JsonSccpAddress(obj.getJsonObject("called-party-address"));
    }

    public JsonSccpAddress getCallingPartyAddress() {
        return callingPartyAddress;
    }

    public void setCallingPartyAddress(JsonSccpAddress callingPartyAddress) {
        this.callingPartyAddress = callingPartyAddress;
    }

    public JsonSccpAddress getCalledPartyAddress() {
        return calledPartyAddress;
    }

    public void setCalledPartyAddress(JsonSccpAddress calledPartyAddress) {
        this.calledPartyAddress = calledPartyAddress;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");

        builder.append("\"calling-party-address\"");
        builder.append(":");
        builder.append(callingPartyAddress);

        builder.append(",");
        builder.append("\"called-party-address\"");
        builder.append(":");
        builder.append(calledPartyAddress);
        builder.append("}");

        return builder.toString();
    }
}
