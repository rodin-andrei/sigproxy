package com.unifun.sigproxy.controller.dto;

import lombok.Data;

@Data
public class SctpClientAssociationConfigDto {

    private Long id;
    private String linkName;
    private String remoteAddress;
    private int remotePort;
    private String localAddress;
    private int localPort;
    private String[] multihomingAddresses;
    private boolean status;
}
