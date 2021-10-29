package com.unifun.sigproxy.sigtran.controller.dto.sctp;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class SctpServerConfigDto {

    private Long id;
    private String name;
    private String localAddress;
    private int localPort;
    private String[] multihomingAddresses;
    private Set<SctpServerAssociationConfigDto> sctpServerAssociationConfigs;
}
