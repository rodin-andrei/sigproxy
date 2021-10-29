package com.unifun.sigproxy.sigtran.service.rabbit.pojo;

import lombok.Data;
import lombok.ToString;

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
