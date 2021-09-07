package com.unifun.sigproxy.controller.dto.sccp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SccpConcernedSignalingPointCodeConfigDto {
    private Integer id;
    private int signalingPointCode;
}
