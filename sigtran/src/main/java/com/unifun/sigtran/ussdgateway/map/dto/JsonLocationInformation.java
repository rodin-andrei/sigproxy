/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.map.dto;

import javax.json.JsonNumber;
import javax.json.JsonObject;

/**
 * @author okulikov
 */
public class JsonLocationInformation {
    private Integer ageOfLocationInformation;

    public JsonLocationInformation() {
    }

    public JsonLocationInformation(JsonObject obj) {
        JsonNumber n = obj.getJsonNumber("age-of-location-information");
        ageOfLocationInformation = n != null ? n.intValue() : null;
    }

    public Integer getAgeOfLocationInformation() {
        return ageOfLocationInformation;
    }

    public void setAgeOfLocationInformation(Integer ageOfLocationInformation) {
        this.ageOfLocationInformation = ageOfLocationInformation;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");

        builder.append("\"age-of-location-information\"");
        builder.append(":");
        builder.append(ageOfLocationInformation);


        builder.append("}");

        return builder.toString();
    }
}
