package com.unifun.sigproxy.controller.dto.sccp;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.restcomm.protocols.ss7.indicator.NatureOfAddress;
import org.restcomm.protocols.ss7.indicator.NumberingPlan;

@Data
@Builder
public class SccpAddressRuleConfigDto {

    private Long id;
    private Byte addressIndicator;
    private Integer pointCode;
    private Integer ssn;
    private Integer translationType;
    private NumberingPlan numberingPlan;
    private NatureOfAddress natureOfAddress;
    private String digits;
}
