package com.unifun.sigproxy.service.rabbit.pojo;

import lombok.Data;
import lombok.ToString;


//for tests
@Data
@ToString
public class MapSupplementaryMessageRabbit {
    private Long tcapId;
    private Long continueTcapId;
    private String ussdString;
    private String operation;
    private String type;
    private String stackName;
    private SccpRabbitAddress callingParty;
    private SccpRabbitAddress calledParty;
    private String msisdn;
}
