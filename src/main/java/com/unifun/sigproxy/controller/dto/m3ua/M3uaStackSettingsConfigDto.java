package com.unifun.sigproxy.controller.dto.m3ua;

import lombok.Data;
import org.restcomm.protocols.ss7.mtp.RoutingLabelFormat;

@Data
public class M3uaStackSettingsConfigDto {

    private Long id;
    private int deliveryMessageThreadCount;
    private int heartbeatTime;
    private int maxAsForRoute;
    private int maxSequenceNumber;
    private boolean routingKeyManagementEnabled;
    private RoutingLabelFormat routingLabelFormat;
    private boolean statisticsEnabled;
    private boolean useLsbForLinksetSelection;

}
