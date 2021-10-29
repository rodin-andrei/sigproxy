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
public class JsonGlobalTitle implements Serializable {

    private String numberingPlan;
    private String encodingSchema;
    private String natureOfAddressIndicator;
    private String digits;

    public JsonGlobalTitle() {

    }

    public JsonGlobalTitle(JsonObject obj) {
        numberingPlan = obj.getString("numbering-plan");
        encodingSchema = obj.getString("encoding-schema");
        natureOfAddressIndicator = obj.getString("nature-of-address-indicator");
        digits = obj.getString("digits");
    }

    public String getNumberingPlan() {
        return numberingPlan;
    }

    public void setNumberingPlan(String numberingPlan) {
        this.numberingPlan = numberingPlan;
    }

    public String getEncodingSchema() {
        return encodingSchema;
    }

    public void setEncodingSchema(String encodingSchema) {
        this.encodingSchema = encodingSchema;
    }

    public String getNatureOfAddressIndicator() {
        return natureOfAddressIndicator;
    }

    public void setNatureOfAddressIndicator(String natureOfAddressIndicator) {
        this.natureOfAddressIndicator = natureOfAddressIndicator;
    }

    public String getDigits() {
        return digits;
    }

    public void setDigits(String digits) {
        this.digits = digits;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");

        builder.append("\"digits\"");
        builder.append(":");
        builder.append('"');
        builder.append(digits);
        builder.append('"');

        if (numberingPlan != null) {
            builder.append(",");
            builder.append("\"numbering-plan\"");
            builder.append(":");
            builder.append('"');
            builder.append(numberingPlan);
            builder.append('"');
        }

        if (encodingSchema != null) {
            builder.append(",");
            builder.append("\"encoding-schema\"");
            builder.append(":");
            builder.append('"');
            builder.append(encodingSchema);
            builder.append('"');
        }

        if (natureOfAddressIndicator != null) {
            builder.append(",");
            builder.append("\"nature-of-address-indicator\"");
            builder.append(":");
            builder.append('"');
            builder.append(natureOfAddressIndicator);
            builder.append('"');
        }

        builder.append("}");

        return builder.toString();
    }
}
