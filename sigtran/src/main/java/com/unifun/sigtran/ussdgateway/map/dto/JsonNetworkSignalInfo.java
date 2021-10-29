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
public class JsonNetworkSignalInfo {
    private String protocolId;
    private String signalInfo;

    public JsonNetworkSignalInfo() {
    }

    public JsonNetworkSignalInfo(JsonObject obj) {
        JsonString str = obj.getJsonString("protocolId");
        if (str != null) {
            protocolId = str.getString();
        }

        str = obj.getJsonString("signalInfo");
        if (str != null) {
            signalInfo = str.getString();
        }
    }

    public String getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(String protocolId) {
        this.protocolId = protocolId;
    }

    public String getSignalInfo() {
        return signalInfo;
    }

    public void setSignalInfo(String signlInfo) {
        this.signalInfo = signlInfo;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");

        builder.append("\"protocolId\"");
        builder.append(":");
        builder.append(protocolId);


        builder.append(",");
        builder.append('"').append("signalInfo").append('"').append(":");
        builder.append('"').append(signalInfo).append('"');

        builder.append("}");

        return builder.toString();
    }

}
