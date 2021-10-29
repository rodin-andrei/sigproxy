/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.map.dto;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.io.Serializable;

/**
 * @author okulikov
 */
public class JsonTcap implements Serializable {
    private String type;
    private JsonTcapDialog dialog;
    private JsonComponents components;
    private String abortMessage;

    public JsonTcap() {
    }

    public JsonTcap(JsonObject obj) {
        type = obj.getString("type");

        JsonObject o = obj.getJsonObject("dialog");
        if (o != null) {
            dialog = new JsonTcapDialog(o);
        }

        JsonArray a = obj.getJsonArray("components");
        if (a != null) {
            components = new JsonComponents(a);
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public JsonTcapDialog getDialog() {
        return dialog;
    }

    public void setDialog(JsonTcapDialog dialog) {
        this.dialog = dialog;
    }

    public JsonComponents getComponents() {
        return components;
    }

    public void setComponents(JsonComponents components) {
        this.components = components;
    }

    public String getAbortMessage() {
        return abortMessage;
    }

    public void setAbortMessage(String abortMessage) {
        this.abortMessage = abortMessage;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");

        builder.append("\"type\"");
        builder.append(":");
        builder.append("\"").append(type).append("\"");

        if (abortMessage != null) {
            builder.append(",");
            builder.append('"').append("abort-message").append('"').append(":");
            builder.append('"').append(abortMessage).append('"');
        }

        if (dialog != null) {
            builder.append(",");
            builder.append('"').append("dialog").append('"').append(":");
            builder.append(dialog);
        }

        if (components != null) {
            builder.append(",");
            builder.append('"').append("components").append('"').append(":");
            builder.append(components);
        }

        builder.append("}");

        return builder.toString();
    }

}
