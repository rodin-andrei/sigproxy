package com.unifun.sigproxy.model.config.sccp;

import lombok.Data;

@Data
public class ServiceAccessPointConfig {
    private int sapId;
    private int mtp3Id;
    private int opc;
    private int ssn;
}
