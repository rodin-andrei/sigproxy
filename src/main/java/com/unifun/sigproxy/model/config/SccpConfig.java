package com.unifun.sigproxy.model.config;

import lombok.Data;

import java.util.Set;

@Data
public class SccpConfig {
    private Set<ServiceAccessPointConfig> sapConfig;
    private Set<DestinationConfig> destConfig;
    private Set<RemoteSignalingPoint> rspConfig;
    private Set<RemoteSubSystem> rssConfig;
    private Set<SubsystemAddressConfig> addressConfig;
    private Set<AddressRuleConfig> addressRuleConfig;

}
