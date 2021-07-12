package com.unifun.sigproxy.dto;

import lombok.Data;

import java.util.Set;

@Data
public class SctpServerDto {
    private String serverName;
    private String localAddress;
    private int localPort;
    private String[] extraAddresses;
    private Set<SctpServerLinkDto> serverLinks;
}
