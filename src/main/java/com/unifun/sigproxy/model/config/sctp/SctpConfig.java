package com.unifun.sigproxy.model.config.sctp;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class SctpConfig {
    private Set<LinkConfig> linkConfig = new HashSet<>();
    private Set<SctpServerConfig> sctpServerConfig = new HashSet<>();
}
