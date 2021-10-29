/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.map.dto;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author okulikov
 */
public class JsonComponents implements Serializable {

    private final ArrayList<JsonComponent> components = new ArrayList<>();

    public JsonComponents() {
    }

    public JsonComponents(JsonArray objects) {
        for (JsonValue object : objects) {
            components.add(new JsonComponent((JsonObject) object));
        }
    }

    public JsonComponent getComponent(int index) {
        return components.get(index);
    }

    public void addComponent(JsonComponent c) {
        components.add(c);
    }

    public int getSize() {
        return components.size();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");

        for (int i = 0; i < components.size(); i++) {
            if (i > 0) {
                builder.append(',');
            }
            builder.append(components.get(i).toString());
        }
        builder.append("]");

        return builder.toString();
    }

}
