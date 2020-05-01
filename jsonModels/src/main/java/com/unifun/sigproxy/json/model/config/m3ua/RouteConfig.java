package com.unifun.sigproxy.json.model.config.m3ua;

import lombok.Data;

@Data
public class RouteConfig {
    private String asName;
    private int opc;
    private int dpc;
    private int ssn;
}
