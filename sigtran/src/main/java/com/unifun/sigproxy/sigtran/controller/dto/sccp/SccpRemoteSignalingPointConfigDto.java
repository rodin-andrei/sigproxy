package com.unifun.sigproxy.sigtran.controller.dto.sccp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SccpRemoteSignalingPointConfigDto {

    private Integer id;
    private int remoteSignalingPointCode;
    private int rspcFlag;
    private int mask;
}
