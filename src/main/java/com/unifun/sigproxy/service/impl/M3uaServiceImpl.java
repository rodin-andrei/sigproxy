package com.unifun.sigproxy.service.impl;

import com.unifun.sigproxy.dto.M3uaAsDTO;
import com.unifun.sigproxy.exception.InitializingException;
import com.unifun.sigproxy.models.config.m3ua.AspConfig;
import com.unifun.sigproxy.models.config.m3ua.RouteConfig;
import com.unifun.sigproxy.repository.m3ua.AsRepository;
import com.unifun.sigproxy.repository.m3ua.AspRepository;
import com.unifun.sigproxy.service.M3uaService;
import com.unifun.sigproxy.service.SctpService;
import com.unifun.sigproxy.util.GateConstants;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.restcomm.protocols.ss7.m3ua.As;
import org.restcomm.protocols.ss7.m3ua.AspFactory;
import org.restcomm.protocols.ss7.m3ua.impl.M3UAManagementImpl;
import org.restcomm.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.restcomm.protocols.ss7.m3ua.parameter.ParameterFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class M3uaServiceImpl implements M3uaService {
    private final ParameterFactory parameterFactory = new ParameterFactoryImpl();
    private final SctpService sctpService;
    private final AsRepository asRepository;
    private final AspRepository aspRepository;

    @Value("${jss.persist.dir}")
    private String jssPersistDir;

    @Getter
    private M3UAManagementImpl m3uaManagement;

    @Override
    public void initialize() throws InitializingException {
        m3uaManagement = new M3UAManagementImpl(GateConstants.STACKNAME + "_m3ua", GateConstants.STACKNAME, null);
        m3uaManagement.setPersistDir(this.jssPersistDir);
        m3uaManagement.setTransportManagement(sctpService.getTransportManagement());
        try {
            m3uaManagement.start();
            m3uaManagement.removeAllResourses();
        } catch (Exception e) {
            throw new InitializingException("Can't initialize M3ua Layer. ", e);
        }

        asRepository.findAll().forEach(asConfig -> {
            try {
                As as = m3uaManagement.createAs(
                        asConfig.getName(),
                        asConfig.getFunctionality(),
                        asConfig.getExchangeType(),
                        asConfig.getIpspType(),
                        parameterFactory.createRoutingContext(asConfig.getRoutingContexts()),
                        parameterFactory.createTrafficModeType(asConfig.getTrafficModeType().getType()),
                        10,
                        parameterFactory.createNetworkAppearance(asConfig.getNetworkAppearance())
                );
                log.info("Created AS {}", asConfig.getName());
            } catch (Exception e) {
                log.error("Error created AS:" + asConfig.getName(), e);
            }

//            asConfig.getRoutes().forEach(routeConfig -> {
//                try {
//                    m3uaManagement.addRoute(routeConfig.getDpc(),
//                            routeConfig.getOpc(),
//                            routeConfig.getSsn(),
//                            asConfig.getName(),
//                            routeConfig.getTrafficModeType().getType());
//                } catch (Exception e) {
//                    log.error("Error add Route to AS:" + asConfig.getName(), e);
//
//                }
//            });
        });

        aspRepository.findAll().forEach(aspConfig -> {
            try {
                AspFactory aspFactory = m3uaManagement.createAspFactory(aspConfig.getName(), aspConfig.getSctpAssocName(), aspConfig.isHeartbeat());
                log.info("Created ASP {}", aspConfig.getName());
            } catch (Exception e) {
                log.warn("Error created ASP:" + aspConfig.getName(), e);
            }

            aspConfig.getApplicationServers()
                    .forEach(asConfig -> {
                        try {
                            m3uaManagement.assignAspToAs(asConfig.getName(), aspConfig.getName());
                            log.info("Assign asp {} to as {}", aspConfig.getName(), asConfig.getName());
                        } catch (Exception e) {
                            log.warn("Error create associated Asp:" + aspConfig.getName() + " to AS:" + asConfig.getName(), e);
                        }
                    });

            try {
                m3uaManagement.startAsp(aspConfig.getName());
                log.info("Started asp {}", aspConfig.getName());

            } catch (Exception e) {
                log.warn("Error started ASP:" + aspConfig.getName(), e);
            }
        });
    }

    @Override
    public void stop() {
        try {
            m3uaManagement.stop();
        } catch (Exception e) {
            log.error("Can't stop M3UA Management: ", e);
        }
    }

    @Override
    public void stopAsp(AspConfig aspConfig) {
        try {
            m3uaManagement.stopAsp(aspConfig.getName());
        } catch (Exception e) {
            log.error("Can't stop ASP : ", e);
        }
    }

    @Override
    public void removeAsp(AspConfig aspConfig) {
//        try {
//            m3uaManagement.unassignAspFromAs(aspConfig.getAsName(), aspConfig.getAspName());
//            m3uaManagement.destroyAspFactory(aspConfig.getAspName());
//            AspFactory aspFactory = m3uaManagement.getAspfactories().get(0);
//        } catch (Exception e) {
//            log.error("Can`t destroy ASP");
//        }
    }

    @Override
    public void removeRoute(RouteConfig routeConfig) {
//        try {
//            m3uaManagement.removeRoute(routeConfig.getDpc(), routeConfig.getOpc(), routeConfig.getSsn(), routeConfig.getAs().getName());
//        } catch (Exception e) {
//            log.error("Can`t remove Route", e);
//        }
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
