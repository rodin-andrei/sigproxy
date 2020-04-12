package com.unifun.sigproxy.model.config.sccp;

import lombok.Data;

import java.util.Set;

@Data
public class SccpConfig {
    private Set<ServiceAccessPointConfig> sapConfig;
    private Set<DestinationConfig> destConfig;
    private Set<RemoteSignalingPoint> rspConfig;
    private Set<AddressRuleConfig.RemoteSubSystem> rssConfig;
    private Set<DestinationConfig.SubsystemAddressConfig> addressConfig;
    private Set<AddressRuleConfig> addressRuleConfig;

}
