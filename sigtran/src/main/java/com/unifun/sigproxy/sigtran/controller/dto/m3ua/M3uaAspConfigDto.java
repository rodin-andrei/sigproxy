package com.unifun.sigproxy.sigtran.controller.dto.m3ua;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class M3uaAspConfigDto {
    private Long id;
    private String name;
    private String sctpAssocName;
    private boolean heartbeat;
}
