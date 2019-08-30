package com.unifun.sigproxy.model;

import lombok.Data;

@Data
public class DestinationConfig {
    private int destId;
    private int sapId;
    private int firstDpc;
    private int lastDpc;
    private int firstSls = 0;
    private int lastSls = 255;
    private int slsMask = 255;
}
