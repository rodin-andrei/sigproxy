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
public class JsonReturnError<T> implements Serializable {

    private long invokeId;
    private long errorCode;

    public JsonReturnError(long invokeId) {
        this.invokeId = invokeId;
    }

    public JsonReturnError(JsonObject obj) {
        invokeId = obj.getJsonNumber("invokeID").longValue();
    }

    public long getInvokeId() {
        return invokeId;
    }

    public void setInvokeId(long invokeId) {
        this.invokeId = invokeId;
    }

    public long getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(long errorCode) {
        this.errorCode = errorCode;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");

        builder.append("\"invokeID\"");
        builder.append(":");
        builder.append(invokeId);

        builder.append(",");
        builder.append("\"errorCode\"");
        builder.append(":");
        builder.append(errorCode);

        builder.append("}");

        return builder.toString();
    }
}
