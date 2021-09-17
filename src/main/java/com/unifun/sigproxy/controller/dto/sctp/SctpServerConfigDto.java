package com.unifun.sigproxy.controller.dto.sctp;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SctpServerConfigDto {

    private Long id;
    private String name;
    private String localAddress;
    private int localPort;
    private String[] multihomingAddresses;
    private List<SctpServerAssociationConfigDto> sctpServerAssociationConfigs;
}
