package com.unifun.sigproxy.controller.dto;

import com.unifun.sigproxy.models.config.SigtranStack;
import lombok.Data;

import javax.persistence.*;

@Data
public class SctpStackSettingsDto {
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
    private SigtranStack sigtranStack;
}
