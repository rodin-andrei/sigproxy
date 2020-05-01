package com.unifun.sigproxy.json.model.config;

import com.unifun.sigproxy.json.model.config.m3ua.M3uaConfig;
import com.unifun.sigproxy.json.model.config.sccp.SccpConfig;
import com.unifun.sigproxy.json.model.config.sctp.SctpConfig;
import com.unifun.sigproxy.json.model.config.tcap.TcapConfig;
import lombok.Data;

@Data
public class SigtranConfig {
    private SctpConfig sctpConfig;
    private M3uaConfig m3uaConfig;
    private SccpConfig sccpConfig;
    private TcapConfig tcapConfig;
}
