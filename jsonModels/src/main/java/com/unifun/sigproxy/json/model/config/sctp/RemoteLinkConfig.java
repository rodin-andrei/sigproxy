package com.unifun.sigproxy.json.model.config.sctp;

import lombok.Data;

@Data
public class RemoteLinkConfig {
    private String linkName;
    private String remoteAddress;
    private int remotePort;
}
