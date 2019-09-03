package com.unifun.sigproxy.model.config;

import lombok.Data;

@Data
public class AspConfig {
    private String aspName;
    private String sctpAssocName;
    private int aspId;
    private boolean heartbeat = false;
    @Deprecated
    private String asName;
}
