package com.unifun.sigproxy.model.config;

import lombok.Data;

@Data
public class RemoteSignalingPoint {
    private int rspId;
    private int dpc;//remote service point code
    private int rspcFlag = 0; //remote service point code flag
    private int mask = 0;
}
