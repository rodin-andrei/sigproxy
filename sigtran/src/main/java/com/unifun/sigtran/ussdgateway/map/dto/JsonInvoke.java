/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.map.dto;

import javax.json.JsonObject;
import java.io.Serializable;

/**
 * @param <T>
 * @author okulikov
 */
public class JsonInvoke<T> implements Serializable {

    private long invokeId;
    private T component;

    public JsonInvoke(long invokeId, T component) {
        this.invokeId = invokeId;
        this.component = component;
    }

    public JsonInvoke(JsonObject obj, Class<T> cls) {
        invokeId = obj.getJsonNumber("invokeID").longValue();
        if (cls.equals(JsonMap.class)) {
            component = (T) new JsonMap(obj.getJsonObject("component"));
        }
    }

    public long getInvokeId() {
        return invokeId;
    }

    public void setInvokeId(long invokeId) {
        this.invokeId = invokeId;
    }

    public T component() {
        return component;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");

        builder.append("\"invokeID\"");
        builder.append(":");
        builder.append(invokeId);

        builder.append(",");
        builder.append("\"component\"");
        builder.append(":");
        builder.append(component.toString());

        builder.append("}");

        return builder.toString();
    }
}
