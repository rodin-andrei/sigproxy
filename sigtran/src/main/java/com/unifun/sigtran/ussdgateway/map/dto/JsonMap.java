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
public class JsonMap<T> implements Serializable {

    private final String operationName;
    private final T operation;

    public JsonMap(String operationName, T operation) {
        this.operationName = operationName;
        this.operation = operation;
    }

    public JsonMap(JsonObject jsonMapRequest) {
        operationName = jsonMapRequest.getString("operation");

        switch (operationName) {
            case "process-unstructured-ss-request":
            default:
                operation = (T) new JsonMapOperation(jsonMapRequest.getJsonObject("args"));
                break;
        }
    }

    public String operationName() {
        return operationName;
    }

    public T operation() {
        return operation;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");

        builder.append("\"operation\"");
        builder.append(":");
        builder.append("\"");
        builder.append(operationName);
        builder.append("\"");

        builder.append(",");
        builder.append("\"args\"");
        builder.append(":");
        builder.append(operation);
        builder.append("}");

        return builder.toString();
    }
}
