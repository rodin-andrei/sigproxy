package com.unifun.sigproxy.model;

import lombok.Data;

@Data
public class AsConfig {
    private String asName;
    private String functionality;
    private String exchangeType;
    private String ipspType;
    private String trafficMode;
    private String networkIndicator;
    private int routingContext = -1;
    private int networkAppearance = -1;
}
