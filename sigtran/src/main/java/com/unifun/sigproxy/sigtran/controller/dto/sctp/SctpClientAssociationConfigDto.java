package com.unifun.sigproxy.sigtran.controller.dto.sctp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
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
