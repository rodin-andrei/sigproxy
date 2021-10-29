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
public class JsonLocationInfoWithLMSI implements Serializable {

    private JsonAddressString networkNodeNumber;

    public JsonLocationInfoWithLMSI() {

    }

    public JsonLocationInfoWithLMSI(JsonObject obj) {
        this.networkNodeNumber = new JsonAddressString(obj);
    }

    public JsonAddressString getNetworkNodeNumber() {
        return networkNodeNumber;
    }

    public void setNetworkNodeNumber(JsonAddressString networkNodeNumber) {
        this.networkNodeNumber = networkNodeNumber;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");

        builder.append("\"networkNodeNumber\"");
        builder.append(":");
        builder.append(networkNodeNumber);

        builder.append("}");

        return builder.toString();
    }

}
