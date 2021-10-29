/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.map.dto;

import javax.json.JsonObject;

/**
 * @author okulikov
 */
public class JsonDataCodingScheme {

    private String language;
    private String codingGroup;
    private String nationalLanguage;
    private String messageClass;

    public JsonDataCodingScheme() {
    }

    public JsonDataCodingScheme(JsonObject obj) {
        language = obj.getJsonString("language") != null ?
                obj.getString("language") :
                null;

        codingGroup = obj.getJsonString("coding-group") != null ?
                obj.getString("coding-group") :
                null;

        nationalLanguage = obj.getJsonString("national-language") != null ?
                obj.getString("national-language") :
                null;
        messageClass = obj.get("message-class") != null ?
                obj.getString("message-class") :
                null;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCodingGroup() {
        return codingGroup;
    }

    public void setCodingGroup(String codingGroup) {
        this.codingGroup = codingGroup;
    }

    public String getNationalLanguage() {
        return nationalLanguage;
    }

    public void setNationalLanguage(String nationalLanguage) {
        this.nationalLanguage = nationalLanguage;
    }

    public String getMessageClass() {
        return messageClass;
    }

    public void setMessageClass(String messageClass) {
        this.messageClass = messageClass;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");

        if (language != null) {
            builder.append("\"language\"");
            builder.append(":");
            builder.append('"');
            builder.append(language);
            builder.append('"');
        }

        if (codingGroup != null) {
            builder.append(",");
            builder.append("\"coding-group\"");
            builder.append(":");
            builder.append('"');
            builder.append(codingGroup);
            builder.append('"');
        }

        if (nationalLanguage != null) {
            builder.append(",");
            builder.append("\"national-language\"");
            builder.append(":");
            builder.append('"');
            builder.append(nationalLanguage);
            builder.append('"');
        }

        if (messageClass != null) {
            builder.append(",");
            builder.append("\"message-class\"");
            builder.append(":");
            builder.append('"');
            builder.append(messageClass);
            builder.append('"');
        }

        builder.append("}");

        return builder.toString();
    }
}
