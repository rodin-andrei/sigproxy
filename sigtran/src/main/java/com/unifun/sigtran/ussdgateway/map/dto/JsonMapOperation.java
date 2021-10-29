/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.map.dto;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonString;
import java.io.Serializable;

/**
 * @author okulikov
 */
public class JsonMapOperation implements Serializable {
    //USSD
    private JsonAddressString msisdn;
    private String ussdString;
    private JsonDataCodingScheme codingScheme;
    private String alertingPattern;

    //mobility
    private JsonSubscriberIdentity subscriberIdentity;
    private JsonRequestedInfo requestedInfo;
    private JsonAddressString gsmSCFAddress;
    private JsonAddressString gmscAddress;
    private JsonSubscriberInfo subscriberInfo;
    private JsonNetworkSignalInfo networkSignalInfo;
    private String[] camelInfo;
    private JsonAddressString scAddress;
    private JsonLocationInfoWithLMSI locationInfoWithLMSI;

    //callhandling
    private String imsi;
    private JsonExtendedRoutingInfo extendedRoutingInfo;

    public JsonMapOperation() {
    }

    public JsonMapOperation(JsonObject obj) {
        JsonString str = obj.getJsonString("ussdString");
        if (str != null) {
            ussdString = str.getString();
        }

        str = obj.getJsonString("imsi");
        if (str != null) {
            imsi = str.getString();
        }

        JsonObject a = obj.getJsonObject("msisdn");
        if (a != null) {
            msisdn = new JsonAddressString(a);
        }

        a = obj.getJsonObject("coding-scheme");
        if (a != null) {
            codingScheme = new JsonDataCodingScheme(a);
        }

        JsonString ap = obj.getJsonString("alerting-pattern");
        if (ap != null) {
            alertingPattern = ap.getString();
        }

        a = obj.getJsonObject("subscriber-identity");
        if (a != null) {
            subscriberIdentity = new JsonSubscriberIdentity(a);
        }

        a = obj.getJsonObject("requested-info");
        if (a != null) {
            requestedInfo = new JsonRequestedInfo(a);
        }

        a = obj.getJsonObject("gsm-scf-address");
        if (a != null) {
            gsmSCFAddress = new JsonAddressString(a);
        }

        a = obj.getJsonObject("gmscAddress");
        if (a != null) {
            gmscAddress = new JsonAddressString(a);
        }

        a = obj.getJsonObject("subscriber-info");
        if (a != null) {
            subscriberInfo = new JsonSubscriberInfo(a);
        }

        a = obj.getJsonObject("networkSignalInfo");
        if (a != null) {
            networkSignalInfo = new JsonNetworkSignalInfo(a);
        }

        a = obj.getJsonObject("extendedRoutingInfo");
        if (a != null) {
            extendedRoutingInfo = new JsonExtendedRoutingInfo(a);
        }

        JsonArray b = obj.getJsonArray("camelInfo");
        if (b != null) {
            camelInfo = new String[b.size()];
            for (int i = 0; i < camelInfo.length; i++) {
                camelInfo[i] = b.getString(i);
            }
        }

        a = obj.getJsonObject("scAddress");
        if (a != null) {
            scAddress = new JsonAddressString(a);
        }

        a = obj.getJsonObject("networkNodeNumber");
        if (a != null) {
            locationInfoWithLMSI = new JsonLocationInfoWithLMSI(a);
        } else {
            a = obj.getJsonObject("locationInfoWithLMSI");
            if (a != null) {
                locationInfoWithLMSI = new JsonLocationInfoWithLMSI(a.getJsonObject("networkNodeNumber"));
            }

        }
    }

    public JsonAddressString getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(JsonAddressString msisdn) {
        this.msisdn = msisdn;
    }

    public String getUssdString() {
        return ussdString;
    }

    public void setUssdString(String ussdString) {
        this.ussdString = ussdString;
    }

    public JsonDataCodingScheme getCodingScheme() {
        return codingScheme;
    }

    public void setCodingScheme(JsonDataCodingScheme codingScheme) {
        this.codingScheme = codingScheme;
    }

    public String getAlertingPattern() {
        return alertingPattern;
    }

    public void setAlertingPattern(String allertingPattern) {
        this.alertingPattern = allertingPattern;
    }

    public JsonSubscriberIdentity getSubscriberIdentity() {
        return subscriberIdentity;
    }

    public void setSubscriberIdentity(JsonSubscriberIdentity subscriberIdentity) {
        this.subscriberIdentity = subscriberIdentity;
    }

    public JsonRequestedInfo getRequestedInfo() {
        return requestedInfo;
    }

    public void setRequestedInfo(JsonRequestedInfo requestedInfo) {
        this.requestedInfo = requestedInfo;
    }

    public JsonAddressString getGsmSCFAddress() {
        return gsmSCFAddress;
    }

    public void setGsmSCFAddress(JsonAddressString gsmSCFAddress) {
        this.gsmSCFAddress = gsmSCFAddress;
    }

    public JsonAddressString getGmscAddress() {
        return gmscAddress;
    }

    public void setGmscAddress(JsonAddressString gsmcAddress) {
        this.gmscAddress = gsmcAddress;
    }

    public JsonSubscriberInfo getSubscriberInfo() {
        return subscriberInfo;
    }

    public void setSubscriberInfo(JsonSubscriberInfo subscriberInfo) {
        this.subscriberInfo = subscriberInfo;
    }

    public JsonNetworkSignalInfo getNetworkSignalInfo() {
        return networkSignalInfo;
    }

    public void setNetworkSignalInfo(JsonNetworkSignalInfo networkSignalInfo) {
        this.networkSignalInfo = networkSignalInfo;
    }

    public String[] getCamelInfo() {
        return camelInfo;
    }

    public void setCamelInfo(String[] camelInfo) {
        this.camelInfo = camelInfo;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public JsonExtendedRoutingInfo getExtendedRoutingInfo() {
        return extendedRoutingInfo;
    }

    public void setExtendedRoutingInfo(JsonExtendedRoutingInfo extendedRoutingInfo) {
        this.extendedRoutingInfo = extendedRoutingInfo;
    }

    public JsonAddressString getScAddress() {
        return scAddress;
    }

    public void setScAddress(JsonAddressString scAddress) {
        this.scAddress = scAddress;
    }

    public JsonLocationInfoWithLMSI getLocationInfoWithLMSI() {
        return locationInfoWithLMSI;
    }

    public void setLocationInfoWithLMSI(JsonLocationInfoWithLMSI locationInfoWithLMSI) {
        this.locationInfoWithLMSI = locationInfoWithLMSI;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");

        boolean hasParam = false;

        if (ussdString != null) {
            builder.append("\"ussdString\"");
            builder.append(":");
            builder.append('"');
            builder.append(ussdString);
            builder.append('"');
            hasParam = true;
        }

        if (codingScheme != null) {
            if (hasParam) builder.append(",");
            builder.append("\"coding-scheme\"");
            builder.append(":");
            builder.append(codingScheme);
            hasParam = true;
        }

        if (msisdn != null) {
            if (hasParam) builder.append(",");
            builder.append("\"msisdn\"");
            builder.append(":");
            builder.append(msisdn);
            hasParam = true;
        }

        if (alertingPattern != null) {
            if (hasParam) builder.append(",");
            builder.append("\"alerting-pattern\"");
            builder.append(":");
            builder.append('"');
            builder.append(alertingPattern);
            builder.append('"');
            hasParam = true;
        }


        if (subscriberIdentity != null) {
            if (hasParam) builder.append(",");
            builder.append("\"subscriber-identity\"");
            builder.append(":");
            builder.append(subscriberIdentity);
            hasParam = true;
        }

        if (requestedInfo != null) {
            if (hasParam) builder.append(",");
            builder.append("\"requested-info\"");
            builder.append(":");
            builder.append(requestedInfo);
            hasParam = true;
        }

        if (gsmSCFAddress != null) {
            if (hasParam) builder.append(",");
            builder.append("\"gsm-scf-address\"");
            builder.append(":");
            builder.append(gsmSCFAddress);
            hasParam = true;
        }

        if (gmscAddress != null) {
            if (hasParam) builder.append(",");
            builder.append("\"gmscAddress\"");
            builder.append(":");
            builder.append(gmscAddress);
            hasParam = true;
        }

        if (scAddress != null) {
            if (hasParam) builder.append(",");
            builder.append("\"scAddress\"");
            builder.append(":");
            builder.append(scAddress);
            hasParam = true;
        }

        if (locationInfoWithLMSI != null) {
            if (hasParam) builder.append(",");
            builder.append("\"locationInfoWithLMSI\"");
            builder.append(":");
            builder.append(locationInfoWithLMSI);
            hasParam = true;
        }

        if (subscriberInfo != null) {
            if (hasParam) builder.append(",");
            builder.append("\"subscriber-info\"");
            builder.append(":");
            builder.append(subscriberInfo);
            hasParam = true;
        }

        if (networkSignalInfo != null) {
            if (hasParam) builder.append(",");
            builder.append("\"networkSignalInfo\"");
            builder.append(":");
            builder.append(networkSignalInfo);
            hasParam = true;
        }

        if (camelInfo != null) {
            if (hasParam) builder.append(",");
            builder.append("\"camelInfo\"");
            builder.append(":");
            builder.append("[");

            for (int i = 0; i < camelInfo.length; i++) {
                if (i > 0) {
                    builder.append(',');
                }
                builder.append('"').append(camelInfo[i]).append('"');
            }

            builder.append(networkSignalInfo);
            builder.append("]");
            hasParam = true;
        }

        if (imsi != null) {
            if (hasParam) builder.append(",");
            builder.append("\"imsi\"");
            builder.append(":");
            builder.append('"');
            builder.append(imsi);
            builder.append('"');
            hasParam = true;
        }

        if (extendedRoutingInfo != null) {
            if (hasParam) builder.append(",");
            builder.append("\"extendedRoutingInfo\"");
            builder.append(":");
            builder.append(extendedRoutingInfo);
            hasParam = true;
        }

        builder.append("}");

        return builder.toString();
    }
}
