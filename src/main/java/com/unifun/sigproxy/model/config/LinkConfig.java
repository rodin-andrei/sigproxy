package com.unifun.sigproxy.model.config;

import lombok.Data;

@Data
public class LinkConfig {
    private String linkName;
    private String linkType;

    private String remoteAddress;
    private int remotePort;

    private String localAddress;
    private int localPort;

    private String[] extraAddresses;
}
