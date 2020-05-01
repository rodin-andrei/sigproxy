package com.unifun.sigproxy.json.model.config.sctp;

import lombok.Data;

import java.util.Set;

@Data
public class SctpServerConfig {
    private String serverName;
    private String linkType;
    private String localAddress;
    private int localPort;
    private String[] extraAddresses;

    private Set<RemoteLinkConfig> remoteLinkConfig;
}
