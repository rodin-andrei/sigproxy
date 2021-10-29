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
public class JsonRoutingInfo {
    private JsonAddressString roamingNumber;

    public JsonRoutingInfo() {
    }

    public JsonRoutingInfo(JsonObject obj) {
        JsonObject o = obj.getJsonObject("roaming-number");
        roamingNumber = o != null ? new JsonAddressString(o) : null;
    }

    public JsonAddressString getRoamingNumber() {
        return roamingNumber;
    }

    public void setRoamingNumber(JsonAddressString roamingNumber) {
        this.roamingNumber = roamingNumber;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");

        builder.append("\"roaming-number\"");
        builder.append(":");
        builder.append(roamingNumber);

        builder.append("}");

        return builder.toString();
    }
}
