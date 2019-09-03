package com.unifun.sigproxy.model.config;

import lombok.Data;

@Data
public class RouteConfig {
    private String asName;
    private int opc;
    private int dpc;
    private int ssn;
}
