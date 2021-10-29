package com.unifun.sigproxy.sigtran.controller.dto.sccp;

import lombok.Builder;
import lombok.Data;
import org.restcomm.protocols.ss7.indicator.NatureOfAddress;
import org.restcomm.protocols.ss7.indicator.NumberingPlan;

@Data
@Builder
public class SccpAddressConfigDto {
    private Integer id;
    private Byte addressIndicator;
    private Integer pointCode;
    private Integer ssn;
    private Integer translationType;
    private NumberingPlan numberingPlan;
    private NatureOfAddress natureOfAddress;
    private String digits;
}
