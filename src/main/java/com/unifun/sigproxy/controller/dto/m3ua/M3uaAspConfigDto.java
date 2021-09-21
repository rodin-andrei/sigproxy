package com.unifun.sigproxy.controller.dto.m3ua;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class M3uaAspConfigDto {
    private Long id;
    private String name;
    private String sctpAssocName;
    private boolean heartbeat;
}
