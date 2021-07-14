package com.unifun.sigproxy.models.config.sctp;

import com.unifun.sigproxy.models.config.SigtranStack;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class SctpStackSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double congControl_BackToNormalDelayThreshold_1;
    private double congControl_BackToNormalDelayThreshold_2;
    private double congControl_BackToNormalDelayThreshold_3;
    private double congControl_DelayThreshold_1;
    private double congControl_DelayThreshold_2;
    private double congControl_DelayThreshold_3;
    private int setConnectDelay;
    private boolean optionSctpDisableFragments;
    private int optionSctpFragmentInterleave;
    private int optionSctpInitMaxStreams_MaxInStreams;
    private int optionSctpInitMaxStreams_MaxOutStreams;
    private boolean optionSctpNodelay;
    private int optionSoLinger;
    private int optionSoRcvbuf;
    private int optionSoSndbuf;
    private boolean singleThread;
    private int workerThreads;

    @OneToOne
    @JoinColumn(name = "stack_id", nullable = false)
    private SigtranStack sigtranStack;
}
