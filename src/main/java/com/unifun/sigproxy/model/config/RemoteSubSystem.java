package com.unifun.sigproxy.model.config;

import lombok.Data;

@Data
public class RemoteSubSystem {
    private int rssId;
    private int dpc; //remote service point code
    private int ssn; //remote ssn
    private int rssFlag = 0;
}
