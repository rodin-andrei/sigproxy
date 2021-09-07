package com.unifun.sigproxy.controller.dto.sccp;

import lombok.Data;
import org.restcomm.protocols.ss7.sccp.LongMessageRuleType;

@Data
public class SccpLongMessageRuleConfigDto {
    private Integer id;
    private int firstSignalingPointCode;
    private int lastSignalingPointCode;
    private LongMessageRuleType longMessageRuleType;
}
