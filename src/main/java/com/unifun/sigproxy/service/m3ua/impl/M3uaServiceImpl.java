package com.unifun.sigproxy.service.m3ua.impl;


import com.unifun.sigproxy.exception.InitializingException;
import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.models.config.m3ua.M3uaAspConfig;
import com.unifun.sigproxy.service.m3ua.M3uaService;
import com.unifun.sigproxy.service.sctp.SctpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.restcomm.protocols.ss7.m3ua.As;
import org.restcomm.protocols.ss7.m3ua.AspFactory;
import org.restcomm.protocols.ss7.m3ua.impl.M3UAManagementImpl;
import org.restcomm.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.restcomm.protocols.ss7.m3ua.parameter.ParameterFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class M3uaServiceImpl implements M3uaService {
    private final ParameterFactory parameterFactory = new ParameterFactoryImpl();
    private final SctpService sctpService;
    private final Map<String, M3UAManagementImpl> m3uaManagements = new HashMap<>();
    @Value("${jss.persist.dir}")
    private String jssPersistDir;

    @Override
    public void initialize(SigtranStack sigtranStack) throws InitializingException {
        log.info("Initializing M3UA management...");
        if (m3uaManagements.containsKey(sigtranStack.getStackName())) {
            throw new InitializingException("SctpManagement: " + sigtranStack.getStackName() + " already exist");
        }
        try {
            var m3uaManagement = new M3UAManagementImpl(sigtranStack.getStackName(), sigtranStack.getStackName(), null);
            m3uaManagements.put(m3uaManagement.getName(), m3uaManagement);
            m3uaManagement.setPersistDir(this.jssPersistDir);
            m3uaManagement.setTransportManagement(sctpService.getTransportManagement(sigtranStack.getStackName()));
            m3uaManagement.start();
            m3uaManagement.removeAllResourses();
            this.initSettings(m3uaManagement,sigtranStack);
            log.info("Created m3ua management: {}", sigtranStack.getStackName());
        } catch (Exception e) {
            throw new InitializingException("Can't initialize M3ua Layer. ", e);
        }


        sigtranStack.getApplicationServers().forEach(asConfig -> {
            try {
                As as = m3uaManagements.get(sigtranStack.getStackName())
                        .createAs(
                                asConfig.getName(),
                                asConfig.getFunctionality(),
                                asConfig.getExchangeType(),
                                asConfig.getIpspType(),
                                parameterFactory.createRoutingContext(asConfig.getRoutingContexts()),
                                parameterFactory.createTrafficModeType(asConfig.getTrafficModeType().getType()),
                                1,
                                parameterFactory.createNetworkAppearance(asConfig.getNetworkAppearance())
                        );
                log.info("Created AS: {}, sigtran stack: {}", asConfig.getName(), sigtranStack.getStackName());
            } catch (Exception e) {
                log.error("Error created AS:" + asConfig.getName(), e);
            }

            asConfig.getRoutes().forEach(routeConfig -> {
                try {
                    m3uaManagements.get(sigtranStack.getStackName())
                            .addRoute(routeConfig.getDpc(),
                                    routeConfig.getOpc(),
                                    routeConfig.getSi(),
                                    asConfig.getName(),
                                    routeConfig.getTrafficModeType().getType());
                    log.info("Added route to AS:{}, DPC: {}, OPC: {}, SSN: {}, Traffic-mode: {}",
                            asConfig.getName(),
                            routeConfig.getDpc(),
                            routeConfig.getOpc(),
                            routeConfig.getSi(),
                            routeConfig.getTrafficModeType());
                } catch (Exception e) {
                    log.error("Error add Route to AS:" + asConfig.getName(), e);

                }
            });
        });

        sigtranStack.getApplicationServerPoints().forEach(aspConfig -> {
            try {
                AspFactory aspFactory = m3uaManagements.get(sigtranStack.getStackName())
                        .createAspFactory(aspConfig.getName(), aspConfig.getSctpAssocName(), aspConfig.isHeartbeat());
                log.info("Created ASP {}, sigtran stack: {}", aspConfig.getName(), sigtranStack.getStackName());
            } catch (Exception e) {
                log.warn("Error created ASP:" + aspConfig.getName(), e);
            }

            aspConfig.getApplicationServers()
                    .forEach(asConfig -> {
                        try {
                            m3uaManagements.get(sigtranStack.getStackName()).assignAspToAs(asConfig.getName(), aspConfig.getName());
                            log.info("Assign asp {} to as {}", aspConfig.getName(), asConfig.getName());
                        } catch (Exception e) {
                            log.warn("Error create simple dimple:" + aspConfig.getName() + " it's popit:" + asConfig.getName(), e);
                        }
                    });

            try {
                m3uaManagements.get(sigtranStack.getStackName()).startAsp(aspConfig.getName());
                log.info("Started asp {}", aspConfig.getName());

            } catch (Exception e) {
                log.warn("Error started ASP:" + aspConfig.getName(), e);
            }
        });
    }

    @Override
    public void stop(String stackName) {
        try {
            m3uaManagements.get(stackName).stop();
            log.info("M3UA Management {} stopped", stackName);
        } catch (Exception e) {
            log.error("Can't stop M3UA Management: {}", stackName, e);
        }
    }

    @Override
    public void stopAsp(M3uaAspConfig m3uaAspConfig, String stackName) {
        try {
            m3uaManagements.get(stackName).stopAsp(m3uaAspConfig.getName());
        } catch (Exception e) {
            log.error("Can't stop ASP: {}, cause: {}", m3uaAspConfig.getName(), e.getMessage(), e);
        }
    }

    @Override
    public void removeAsp(M3uaAspConfig m3uaAspConfig, String stackName) {
        //TODO add implementation
    }

    @Override
    public M3UAManagementImpl getManagement(String stackName) {
        return this.m3uaManagements.get(stackName);
    }


    private void initSettings(M3UAManagementImpl m3uaManagement, SigtranStack sigtranStack) {
        try {
            m3uaManagement.setDeliveryMessageThreadCount(sigtranStack.getM3UaStackSettingsConfig().getDeliveryMessageThreadCount());
            m3uaManagement.setHeartbeatTime(sigtranStack.getM3UaStackSettingsConfig().getHeartbeatTime());
            m3uaManagement.setRoutingKeyManagementEnabled(sigtranStack.getM3UaStackSettingsConfig().isRoutingKeyManagementEnabled());
            m3uaManagement.setMaxAsForRoute(sigtranStack.getM3UaStackSettingsConfig().getMaxAsForRoute());
            m3uaManagement.setMaxSequenceNumber(sigtranStack.getM3UaStackSettingsConfig().getMaxSequenceNumber());
            m3uaManagement.setRoutingLabelFormat(sigtranStack.getM3UaStackSettingsConfig().getRoutingLabelFormat());
            m3uaManagement.setStatisticsEnabled(sigtranStack.getM3UaStackSettingsConfig().isStatisticsEnabled());
            m3uaManagement.setUseLsbForLinksetSelection(sigtranStack.getM3UaStackSettingsConfig().isUseLsbForLinksetSelection());
        } catch (Exception e){
            log.warn("Exception in M3uaServiceImp.iniSetting: "+ e);
        }

    }

}

