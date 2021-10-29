package com.unifun.sigtran.ussdgateway.map.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.Serializable;

@Data
public class JsonInteractiveProxyDTO implements Serializable {

    private short invokeId;
    private long dialogId;

    public JsonInteractiveProxyDTO(long dialogId) {
        this.dialogId = dialogId;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}

