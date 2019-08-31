package com.unifun.sigproxy.model.config;

import lombok.Data;

@Data
public class SubsystemAddressConfig {
    private int addressId;
    private int addressIndicator;
    private int pc;
    private int ssn;
    private int translationType;
    private int numberingPlan;
    private int natureOfAddressIndicator;
    private String digits;
}
