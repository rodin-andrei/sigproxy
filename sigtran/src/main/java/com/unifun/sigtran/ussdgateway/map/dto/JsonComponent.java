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
public class JsonComponent implements Serializable {
    private String type;
    private Object value;

    public JsonComponent() {
    }

    public JsonComponent(JsonObject obj) {
        type = obj.getJsonObject("component").getString("type");
        switch (type) {
            case "invoke":
                value = new JsonInvoke<>(obj.getJsonObject("component").getJsonObject("value"), JsonMap.class);
                break;
            case "returnResultLast":
                value = new JsonReturnResultLast<>(obj.getJsonObject("component").getJsonObject("value"), JsonMap.class);
                break;
            case "returnError":
                value = new JsonReturnError<>(obj.getJsonObject("component").getJsonObject("value"));
                break;
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");

        builder.append("\"component\"");
        builder.append(":");
        builder.append("{");
        builder.append("\"type\"");
        builder.append(":");
        builder.append('"');
        builder.append(type);
        builder.append('"');

        builder.append(",");
        builder.append("\"value\"");
        builder.append(":");
        builder.append(value.toString());

        builder.append("}");
        builder.append("}");

        return builder.toString();
    }

}
