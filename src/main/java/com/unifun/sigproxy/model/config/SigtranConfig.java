package com.unifun.sigproxy.model.config;

import com.unifun.sigproxy.model.config.m3ua.M3uaConfig;
import com.unifun.sigproxy.model.config.sccp.SccpConfig;
import com.unifun.sigproxy.model.config.sctp.SctpConfig;
import com.unifun.sigproxy.model.config.tcap.TcapConfig;
import lombok.Data;

@Data
public class SigtranConfig {
    private SctpConfig sctpConfig;
    private M3uaConfig m3uaConfig;
    private SccpConfig sccpConfig;
    private TcapConfig tcapConfig;
}
