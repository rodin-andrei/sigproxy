package com.unifun.sigproxy.controller.dto.m3ua;

import com.unifun.sigproxy.models.config.m3ua.TrafficModeType;
import lombok.Builder;
import lombok.Data;
import org.restcomm.protocols.ss7.m3ua.ExchangeType;
import org.restcomm.protocols.ss7.m3ua.Functionality;
import org.restcomm.protocols.ss7.m3ua.IPSPType;

import java.util.List;
import java.util.Set;

@Data
@Builder
public class M3uaAsConfigDto {
    private Long id;
    private String name;
    private Functionality functionality;
    private ExchangeType exchangeType;
    private IPSPType ipspType;
    private TrafficModeType trafficModeType;
    private int networkIndicator;
    private int networkAppearance;
    private long[] routingContexts;
    private Set<M3uaAspConfigDto> applicationServerPointsDto;
    private Set<M3uaRouteConfigDto> routesDto;
}
