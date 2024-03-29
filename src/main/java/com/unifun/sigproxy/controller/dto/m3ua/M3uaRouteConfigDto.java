package com.unifun.sigproxy.controller.dto.m3ua;

import com.unifun.sigproxy.models.config.m3ua.TrafficModeType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class M3uaRouteConfigDto {
    private Long id;
    private int opc;
    private int dpc;
    private int si;
    private TrafficModeType trafficModeType;
}
