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
public class JsonAddressString implements Serializable {
    private boolean isExtension = false;
    private String numberingPlan;
    private String natureOfAddress;
    private String address;

    public JsonAddressString() {
    }

    public JsonAddressString(JsonObject obj) {
        isExtension = obj.getBoolean("extension", false);
        numberingPlan = obj.getString("numbering-plan");
        natureOfAddress = obj.getString("nature-of-address");
        address = obj.getString("address");
    }

    public String getNumberingPlan() {
        return numberingPlan;
    }

    public void setNumberingPlan(String numberingPlan) {
        this.numberingPlan = numberingPlan;
    }

    public String getNatureOfAddress() {
        return natureOfAddress;
    }

    public void setNatureOfAddress(String natureOfAddress) {
        this.natureOfAddress = natureOfAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isExtension() {
        return isExtension;
    }

    public void setExtension(boolean isExtension) {
        this.isExtension = isExtension;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");

        if (isExtension) {
            builder.append("\"extension\"");
            builder.append(":");
            builder.append('"');
            builder.append(isExtension);
            builder.append("\"");
            builder.append(",");
        }

        builder.append("\"nature-of-address\"");
        builder.append(":");
        builder.append('"');
        builder.append(natureOfAddress);
        builder.append("\"");

        builder.append(",");
        builder.append("\"numbering-plan\"");
        builder.append(":");
        builder.append("\"");
        builder.append(numberingPlan);
        builder.append("\"");

        builder.append(",");
        builder.append("\"address\"");
        builder.append(":");
        builder.append("\"");
        builder.append(address);
        builder.append("\"");

        builder.append("}");

        return builder.toString();
    }
}
