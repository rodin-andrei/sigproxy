package com.unifun.sigproxy.controller.dto;

import com.unifun.sigproxy.models.config.sctp.SctpServerAssociationConfig;
import lombok.Data;

import java.util.Set;

@Data
public class SctpServerDto {

    private Long id;
    private String name;
    private String localAddress;
    private int localPort;
    private String[] multihomingAddresses;
    private Set<SctpServerAssociationConfig> sctpServerAssociationConfigs;
    private String status;
}
