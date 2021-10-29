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
public class JsonCallback {

    private String primaryDestination;
    private String spareDestination;

    public JsonCallback() {
    }

    public JsonCallback(JsonObject obj) {
        JsonString s = obj.getJsonString("primary-destination");
        primaryDestination = s != null ? s.getString() : null;

        s = obj.getJsonString("spare-destination");
        spareDestination = s != null ? s.getString() : null;
    }

    public String getPrimaryDestination() {
        return primaryDestination;
    }

    public void setPrimaryDestination(String primaryDestination) {
        this.primaryDestination = primaryDestination;
    }

    public String getSpareDestination() {
        return spareDestination;
    }

    public void setSpareDestination(String spareDestination) {
        this.spareDestination = spareDestination;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");

        builder.append("\"primary-destination\"");
        builder.append(":");
        builder.append('"');
        builder.append(primaryDestination);
        builder.append("\"");

        builder.append(",");
        builder.append("\"spare-destination\"");
        builder.append(":");
        builder.append('"');
        builder.append(spareDestination);
        builder.append("\"");

        builder.append("}");

        return builder.toString();
    }
}
