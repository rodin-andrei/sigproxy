package com.unifun.sigproxy.sigtran.controller.dto.sccp;

import lombok.Builder;
import lombok.Data;
import org.restcomm.protocols.ss7.sccp.SccpCongestionControlAlgo;
import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;

@Data
@Builder
public class SccpSettingsConfigDto {

    private Long id;

    private int zmarginxudtmessage;

    private int reassemblytimerdelay;

    private int maxdatamessage;

    private int periodoflogging;

    private boolean removespc;

    private boolean previewmode;

    private int ssttimerduration_min;

    private int ssttimerduration_max;

    private double ssttimerduration_increasefactor;

    private SccpProtocolVersion sccpprotocolversion;

    private int cc_timer_a;

    private int cc_timer_d;

    private boolean canrelay;

    private int connesttimerdelay;

    private int iastimerdelay;

    private int iartimerdelay;

    private int reltimerdelay;

    private int repeatreltimerdelay;

    private int inttimerdelay;

    private int guardtimerdelay;

    private int resettimerdelay;

    private int timerexecutors_threadcount;

    private SccpCongestionControlAlgo cc_algo;

    private boolean cc_blockingoutgoungsccpmessages;

}
