package com.unifun.sigproxy.controller.dto.sccp;

import lombok.Builder;
import lombok.Data;
import org.restcomm.protocols.ss7.sccp.LoadSharingAlgorithm;
import org.restcomm.protocols.ss7.sccp.OriginationType;
import org.restcomm.protocols.ss7.sccp.RuleType;

@Data
@Builder
public class SccpRuleConfigDto {
    private Integer id;

    private String mask;
    private SccpAddressRuleConfigDto sccpAddressRuleConfig;
    private RuleType ruleType;
    private Integer primaryAddressId;
    private LoadSharingAlgorithm loadSharingAlgorithm = LoadSharingAlgorithm.Undefined;
    private OriginationType originationType = OriginationType.ALL;
    private Integer secondaryAddressId;
    private Integer newCallingPartyAddressAddressId;
    private Integer networkId;
    private SccpAddressRuleConfigDto callingSccpAddressRuleConfig;
}
