package com.unifun.sigproxy.service.rabbit.pojo;

import lombok.Data;
import lombok.ToString;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;

@Data
@ToString
public class SccpRabbitAddress {
    private String digits;
    private Integer ssn;
    private Integer pc;
    private Integer routingIndicator;
    private Integer globalTitleIndicator;
    private String sccpNumberingPlan;
    private String sccpNatureOfAddress;
    private String sccpEncodingSchema;
}
