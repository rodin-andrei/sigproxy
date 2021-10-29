package com.unifun.sigproxy.sigtran.controller.dto.sctp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SctpStackSettingsConfigDto {

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
}
