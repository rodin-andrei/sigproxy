package com.unifun.sigproxy.model.dto;

import lombok.Data;

@Data
public class SctpLinkDto {
    private String linkName;
    private String localAddress;
    private int localPort;
    private String remoteAddress;
    private int remotePort;
    private String status;
    private String[] extraAddresses;
}
