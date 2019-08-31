package com.unifun.sigproxy.model.config;

import lombok.Data;

@Data
public class SigtranConfig {
    private SctpConfig sctpConfig;
    private M3uaConfig m3uaConfig;
    private SccpConfig sccpConfig;
    private TcapConfig tcapConfig;
}
