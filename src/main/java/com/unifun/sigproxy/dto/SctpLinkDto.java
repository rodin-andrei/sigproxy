package com.unifun.sigproxy.dto;

import lombok.Data;

@Data
public class SctpLinkDto {
    private String linkName;
    private String localAddress;
    private int localPort;
    private String remoteAddress;
    private int remotePort;
    private boolean up;
    private boolean started;

    private String[] extraAddresses;
}
