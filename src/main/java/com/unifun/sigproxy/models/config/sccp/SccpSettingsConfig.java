package com.unifun.sigproxy.models.config.sccp;

import com.unifun.sigproxy.models.config.SigtranStack;
import lombok.Data;
import lombok.ToString;
import org.restcomm.protocols.ss7.sccp.SccpCongestionControlAlgo;
import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;

import javax.persistence.*;

@Data
@Entity
public class SccpSettingsConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Enumerated(EnumType.STRING)
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

    @Enumerated(EnumType.STRING)
    private SccpCongestionControlAlgo cc_algo;

    private boolean cc_blockingoutgoungsccpmessages;


    @OneToOne
    @ToString.Exclude
    @JoinColumn(name = "stack_id", nullable = false)
    private SigtranStack sigtranStack;
}
