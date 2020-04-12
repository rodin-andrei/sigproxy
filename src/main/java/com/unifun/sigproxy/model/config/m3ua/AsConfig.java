package com.unifun.sigproxy.model.config.m3ua;

import lombok.Data;

@Data
public class AsConfig {
    private String asName;
    private String functionality;
    private String exchangeType;
    private String ipspType;
    private String trafficMode;
    private String networkIndicator;
    private int networkAppearance = -1;
    private long[] routingContext;
}
