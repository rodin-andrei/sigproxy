package com.unifun.sigproxy.controller.dto;

import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.models.config.sctp.SctpServerAssociationConfig;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class SctpServerConfigDto {

    private Long id;
    private String name;
    private String localAddress;
    private int localPort;
    private String[] multihomingAddresses;
    private List<SctpServerAssociationConfigDto> sctpServerAssociationConfigs;
}
