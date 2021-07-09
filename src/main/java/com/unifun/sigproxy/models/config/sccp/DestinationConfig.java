package com.unifun.sigproxy.models.config.sccp;

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

    @Data
    public static class SubsystemAddressConfig {
        private int addressId;
        private int addressIndicator;
        private int pc;
        private int ssn;
        private int translationType;
        private int numberingPlan;
        private int natureOfAddressIndicator;
        private String digits;
    }
}
