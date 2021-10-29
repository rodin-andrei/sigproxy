/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.map.dto;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.ByteArrayInputStream;
import java.io.Serializable;

/**
 * @author okulikov
 */
public class JsonMessage implements Serializable {
    private JsonSccp sccp;
    private JsonTcap tcap;
    private JsonCallback callback;

    public JsonMessage() {
    }

    public JsonMessage(String text) {
        JsonReader reader = Json.createReader(new ByteArrayInputStream(text.getBytes()));
        init(reader.readObject());
    }

    public JsonMessage(JsonObject obj) {
        init(obj);
    }

    private void init(JsonObject obj) {
        if (obj.getJsonObject("sccp") != null) {
            sccp = new JsonSccp(obj.getJsonObject("sccp"));
        }
        if (obj.getJsonObject("callback") != null) {
            callback = new JsonCallback(obj.getJsonObject("callback"));
        }
        tcap = new JsonTcap(obj.getJsonObject("tcap"));
    }

    public JsonSccp getSccp() {
        return sccp;
    }

    public void setSccp(JsonSccp sccp) {
        this.sccp = sccp;
    }

    public JsonTcap getTcap() {
        return tcap;
    }

    public void setTcap(JsonTcap tcap) {
        this.tcap = tcap;
    }

    public JsonCallback getCallback() {
        return callback;
    }

    public void setCallback(JsonCallback callback) {
        this.callback = callback;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");

        if (sccp != null) {
            builder.append("\"sccp\":");
            builder.append(sccp);
            builder.append(",");
        }


        builder.append("\"tcap\":");
        builder.append(tcap);

        if (callback != null) {
            builder.append(",");
            builder.append("\"callback\":");
            builder.append(sccp);
        }

        builder.append("}");
        return builder.toString();
    }
}
