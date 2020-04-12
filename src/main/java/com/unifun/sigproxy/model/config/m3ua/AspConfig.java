package com.unifun.sigproxy.model.config.m3ua;

import lombok.Data;

@Data
public class AspConfig {
    private String aspName;
    private String sctpAssocName;
    private int aspId;
    private boolean heartbeat = false;
    private String asName;
}
