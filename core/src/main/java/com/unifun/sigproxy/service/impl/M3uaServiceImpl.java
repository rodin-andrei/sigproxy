package com.unifun.sigproxy.service.impl;

import com.unifun.sigproxy.exception.InitializingException;
import com.unifun.sigproxy.json.model.config.m3ua.AspConfig;
import com.unifun.sigproxy.json.model.config.m3ua.M3uaConfig;
import com.unifun.sigproxy.json.model.config.m3ua.RouteConfig;
import com.unifun.sigproxy.json.model.dto.M3uaAsDTO;
import com.unifun.sigproxy.service.M3uaService;
import com.unifun.sigproxy.service.SctpService;
import com.unifun.sigproxy.util.GateConstants;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.restcomm.protocols.ss7.m3ua.AspFactory;
import org.restcomm.protocols.ss7.m3ua.ExchangeType;
import org.restcomm.protocols.ss7.m3ua.Functionality;
import org.restcomm.protocols.ss7.m3ua.IPSPType;
import org.restcomm.protocols.ss7.m3ua.impl.M3UAManagementImpl;
import org.restcomm.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.restcomm.protocols.ss7.m3ua.parameter.ParameterFactory;
import org.restcomm.protocols.ss7.m3ua.parameter.TrafficModeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class M3uaServiceImpl implements M3uaService {
    private static final Logger LOGGER = LoggerFactory.getLogger(M3uaServiceImpl.class);

    private final ParameterFactory parameterFactory = new ParameterFactoryImpl();
    private final SctpService sctpService;

    @Value("${jss.persist.dir}")
    private String jssPersistDir;

    @Getter
    private M3UAManagementImpl m3uaManagement;

    @Override
    public void initialize(M3uaConfig m3uaConfig) throws InitializingException {
        m3uaManagement = new M3UAManagementImpl(GateConstants.STACKNAME + "_m3ua", GateConstants.STACKNAME, null);
        m3uaManagement.setPersistDir(this.jssPersistDir);
        m3uaManagement.setTransportManagement(sctpService.getTransportManagement());
        try {
            m3uaManagement.start();
            m3uaManagement.removeAllResourses();
        } catch (Exception e) {
            throw new InitializingException("Can't initialize M3ua Layer. ", e);
        }
        m3uaConfig.getAspConfig().forEach(asp -> {
            try {
                m3uaManagement.createAspFactory(
                        asp.getAspName(),
                        asp.getSctpAssocName(),
                        asp.getAspId(),
                        asp.isHeartbeat()
                );
            } catch (Exception e) {
                LOGGER.error("Can't initialize ASP {}. {}", asp.getAspName(), e.getMessage());
            }
        });
        m3uaConfig.getAsConfig().forEach(as -> {
            try {
                TrafficModeType trafficMode;
                switch (as.getTrafficMode().toUpperCase()) {
                    case "OVERRIDE":
                        trafficMode = parameterFactory.createTrafficModeType(TrafficModeType.Override);
                        break;
                    case "BROADCAST":
                        trafficMode = parameterFactory.createTrafficModeType(TrafficModeType.Broadcast);
                        break;
                    case "LOADSHARE":
                    default:
                        trafficMode = parameterFactory.createTrafficModeType(TrafficModeType.Loadshare);
                        break;
                }
                m3uaManagement.createAs(
                        as.getAsName(),
                        Functionality.getFunctionality(as.getFunctionality()),
                        ExchangeType.getExchangeType(as.getExchangeType()),
                        IPSPType.getIPSPType(as.getIpspType()),
                        parameterFactory.createRoutingContext(as.getRoutingContext()),
                        trafficMode,
                        0,
                        parameterFactory.createNetworkAppearance(as.getNetworkAppearance())
                );
            } catch (Exception e) {
                LOGGER.warn("Can't create AS {}. {}", as.getAsName(), e.getMessage());
            }

        });
        m3uaConfig.getRouteConfig().forEach(route -> {
            try {
                m3uaManagement.addRoute(route.getDpc(), route.getOpc(), route.getSsn(), route.getAsName());
            } catch (Exception e) {
                LOGGER.warn("Can't create Route for AS: {}. {}", route.getAsName(), e.getMessage());
            }
        });


        m3uaConfig.getAspConfig().forEach(asp -> {
            try {
                m3uaManagement.assignAspToAs(asp.getAsName(), asp.getAspName());
                m3uaManagement.startAsp(asp.getAspName());
            } catch (Exception e) {
                LOGGER.error("Can't start ASP {}. {}", asp.getAspName(), e.getMessage());
            }
        });

    }

    @Override
    public void stop() {
        try {
            m3uaManagement.stop();
        } catch (Exception e) {
            LOGGER.error("Can't stop M3UA Management: ", e);
        }
    }

    @Override
    public void stopAsp(AspConfig aspConfig) {
        try {
            m3uaManagement.stopAsp(aspConfig.getAspName());
        } catch (Exception e) {
            LOGGER.error("Can't stop ASP : ", e);
        }
    }

    @Override
    public void removeAsp(AspConfig aspConfig) {
        try {
            m3uaManagement.unassignAspFromAs(aspConfig.getAsName(), aspConfig.getAspName());
            m3uaManagement.destroyAspFactory(aspConfig.getAspName());
            AspFactory aspFactory = m3uaManagement.getAspfactories().get(0);
        } catch (Exception e) {
            LOGGER.error("Can`t destroy ASP");
        }
    }

    @Override
    public void removeRoute(RouteConfig routeConfig) {
        try {
            m3uaManagement.removeRoute(routeConfig.getDpc(), routeConfig.getOpc(), routeConfig.getSsn(), routeConfig.getAsName());
        } catch (Exception e) {
            LOGGER.error("Can`t remove Route", e);
        }
    }

    @Override
    public Set<M3uaAsDTO> getM3uaStatuses() {
        return this.m3uaManagement.getAppServers().stream().map(as ->
        {
            M3uaAsDTO m3uaAsDTO = new M3uaAsDTO();
            m3uaAsDTO.setName(as.getName());
            m3uaAsDTO.setIpspType(as.getIpspType().getType());
            m3uaAsDTO.setRoutingContexts(as.getRoutingContext().getRoutingContexts());
            m3uaAsDTO.setState(as.getState().getName());

            return m3uaAsDTO;
        }).collect(Collectors.toSet());
    }
}
