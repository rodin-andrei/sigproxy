package com.unifun.sigproxy.json.model.config.sccp;

import lombok.Data;

@Data
public class AddressRuleConfig {
    private int ruleId;
    private String mask;
    private int addressIndicator;
    private int pc;
    private int ssn;
    private int translationType;
    private int numberingPlan;
    private int natureOfAddressIndicator;
    private String digits;
    private String ruleType;
    private String primaryAddressId;
    private String backupAddressId;
    private String loadsharingAlgorithm;
    private String newCallingPartyAddressId;
    private String originationType;

    @Data
    public static class RemoteSubSystem {
        private int rssId;
        private int dpc; //remote service point code
        private int ssn; //remote ssn
        private int rssFlag = 0;
    }
}
