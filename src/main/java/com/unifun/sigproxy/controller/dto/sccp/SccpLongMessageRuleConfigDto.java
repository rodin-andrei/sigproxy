package com.unifun.sigproxy.controller.dto.sccp;

import lombok.Builder;
import lombok.Data;
import org.restcomm.protocols.ss7.sccp.LongMessageRuleType;

@Data
@Builder
public class SccpLongMessageRuleConfigDto {
    private Integer id;
    private int firstSignalingPointCode;
    private int lastSignalingPointCode;
    private LongMessageRuleType longMessageRuleType;
}
