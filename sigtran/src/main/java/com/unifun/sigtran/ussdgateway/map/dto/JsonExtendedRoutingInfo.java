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
public class JsonExtendedRoutingInfo {

    private JsonCamelRoutingInfo camelRoutingInfo;
    private JsonRoutingInfo routingInfo;

    public JsonExtendedRoutingInfo() {
    }

    public JsonExtendedRoutingInfo(JsonObject obj) {
        JsonObject o = obj.getJsonObject("camelRoutingInfo");
        camelRoutingInfo = o != null ? new JsonCamelRoutingInfo(o) : null;

        o = obj.getJsonObject("routingInfo");
        routingInfo = o != null ? new JsonRoutingInfo(o) : null;
    }

    public JsonCamelRoutingInfo getCamelRoutingInfo() {
        return camelRoutingInfo;
    }

    public void setCamelRoutingInfo(JsonCamelRoutingInfo camelRoutingInfo) {
        this.camelRoutingInfo = camelRoutingInfo;
    }

    public JsonRoutingInfo getRoutingInfo() {
        return routingInfo;
    }

    public void setRoutingInfo(JsonRoutingInfo routingInfo) {
        this.routingInfo = routingInfo;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");

        boolean hasParams = false;
        if (camelRoutingInfo != null) {
            builder.append("\"camelRouting\"");
            builder.append(":");
            builder.append(camelRoutingInfo);
            hasParams = true;
        }

        if (routingInfo != null) {
            if (hasParams) builder.append(",");
            builder.append("\"routingInfo\"");
            builder.append(":");
            builder.append(routingInfo);
        }

        builder.append("}");

        return builder.toString();
    }
}
