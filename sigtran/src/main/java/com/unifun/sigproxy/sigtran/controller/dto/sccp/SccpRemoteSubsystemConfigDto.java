package com.unifun.sigproxy.sigtran.controller.dto.sccp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SccpRemoteSubsystemConfigDto {


    private Integer id;
    private int remoteSignalingPointCode;
    private int remoteSubsystemNumber;
    private int remoteSubsystemFlag;
    boolean markProhibitedWhenSpcResuming;
}
