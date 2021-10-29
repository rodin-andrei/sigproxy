package com.unifun.sigproxy.sigtran.service.m3ua.impl;


import com.unifun.sigproxy.sigtran.exception.InitializingException;
import com.unifun.sigproxy.sigtran.models.config.SigtranStack;
import com.unifun.sigproxy.sigtran.models.config.m3ua.M3uaAsConfig;
import com.unifun.sigproxy.sigtran.models.config.m3ua.M3uaAspConfig;
import com.unifun.sigproxy.sigtran.models.config.m3ua.M3uaRouteConfig;
import com.unifun.sigproxy.sigtran.models.config.m3ua.M3uaStackSettingsConfig;
import com.unifun.sigproxy.sigtran.service.m3ua.M3uaService;
import com.unifun.sigproxy.sigtran.service.sctp.SctpService;
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

        this.initM3uaManagement(sigtranStack);

        sigtranStack.getApplicationServers().forEach(asConfig -> {

            this.addAs(asConfig);

            asConfig.getRoutes().forEach(this::addRoute);

            asConfig.getApplicationServerPoints().forEach(m3uaAspConfig -> this.addAsp(m3uaAspConfig, sigtranStack.getStackName()));
        });

    }

    public void initM3uaManagement(SigtranStack sigtranStack) throws InitializingException {
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
//            this.initSettings(m3uaManagement, Optional.ofNullable(sigtranStack.getM3UaStackSettingsConfig()).orElseGet(M3uaStackSettingsConfig::new));
            log.info("Created m3ua management: {}", sigtranStack.getStackName());
        } catch (Exception e) {
            throw new InitializingException("Can't initialize M3ua Layer. ", e);
        }
    }

    @Override
    public void addAs(M3uaAsConfig m3uaAsConfig) {
        try {
            As as = m3uaManagements.get(m3uaAsConfig.getSigtranStack().getStackName())
                    .createAs(
                            m3uaAsConfig.getName(),
                            m3uaAsConfig.getFunctionality(),
                            m3uaAsConfig.getExchangeType(),
                            m3uaAsConfig.getIpspType(),
                            parameterFactory.createRoutingContext(m3uaAsConfig.getRoutingContexts()),
                            parameterFactory.createTrafficModeType(m3uaAsConfig.getTrafficModeType().getType()),
                            1,
                            parameterFactory.createNetworkAppearance(m3uaAsConfig.getNetworkAppearance())
                    );
            log.info("Created AS: {}, sigtran stack: {}", m3uaAsConfig.getName(), m3uaAsConfig.getSigtranStack().getStackName());
        } catch (Exception e) {
            log.error("Error created AS:" + m3uaAsConfig.getName(), e);
        }
    }

    @Override
    public void addAsp(M3uaAspConfig m3uaAspConfig, String sigtranStackName) {
        try {
            AspFactory aspFactory = m3uaManagements.get(sigtranStackName)
                    .createAspFactory(m3uaAspConfig.getName(), m3uaAspConfig.getSctpAssocName(), m3uaAspConfig.isHeartbeat());
            log.info("Created ASP {}, sigtran stack: {}", m3uaAspConfig.getName(), sigtranStackName);
        } catch (Exception e) {
            log.warn("Error created ASP:" + m3uaAspConfig.getName(), e);
        }

        m3uaAspConfig.getApplicationServers()
                .forEach(asConfig -> {
                    try {
                        m3uaManagements.get(sigtranStackName).assignAspToAs(asConfig.getName(), m3uaAspConfig.getName());
                        log.info("Assign asp {} to as {}", m3uaAspConfig.getName(), asConfig.getName());
                    } catch (Exception e) {
                        log.warn("Error create simple dimple:" + m3uaAspConfig.getName() + " it's popit:" + asConfig.getName(), e);
                    }
                });

        try {
            m3uaManagements.get(sigtranStackName).startAsp(m3uaAspConfig.getName());
            log.info("Started asp {}", m3uaAspConfig.getName());

        } catch (Exception e) {
            log.warn("Error started ASP:" + m3uaAspConfig.getName(), e);
        }
    }

    @Override
    public void addRoute(M3uaRouteConfig m3uaRouteConfig) {
        try {
            m3uaManagements.get(m3uaRouteConfig.getAs().getSigtranStack().getStackName())
                    .addRoute(m3uaRouteConfig.getDpc(),
                            m3uaRouteConfig.getOpc(),
                            m3uaRouteConfig.getSi(),
                            m3uaRouteConfig.getAs().getName(),
                            m3uaRouteConfig.getTrafficModeType().getType());
            log.info("Added route to AS:{}, DPC: {}, OPC: {}, SSN: {}, Traffic-mode: {}",
                    m3uaRouteConfig.getAs().getName(),
                    m3uaRouteConfig.getDpc(),
                    m3uaRouteConfig.getOpc(),
                    m3uaRouteConfig.getSi(),
                    m3uaRouteConfig.getTrafficModeType());
        } catch (Exception e) {
            log.error("Error add Route to AS:" + m3uaRouteConfig.getAs().getName(), e);
        }
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


    private void initSettings(M3UAManagementImpl m3uaManagement, M3uaStackSettingsConfig m3UaStackSettingsConfig) {
        try {
            m3uaManagement.setDeliveryMessageThreadCount(m3UaStackSettingsConfig.getDeliveryMessageThreadCount());
            m3uaManagement.setHeartbeatTime(m3UaStackSettingsConfig.getHeartbeatTime());
            m3uaManagement.setRoutingKeyManagementEnabled(m3UaStackSettingsConfig.isRoutingKeyManagementEnabled());
            m3uaManagement.setMaxAsForRoute(m3UaStackSettingsConfig.getMaxAsForRoute());
            m3uaManagement.setMaxSequenceNumber(m3UaStackSettingsConfig.getMaxSequenceNumber());
            m3uaManagement.setRoutingLabelFormat(m3UaStackSettingsConfig.getRoutingLabelFormat());
            m3uaManagement.setStatisticsEnabled(m3UaStackSettingsConfig.isStatisticsEnabled());
            m3uaManagement.setUseLsbForLinksetSelection(m3UaStackSettingsConfig.isUseLsbForLinksetSelection());
        } catch (Exception e) {
            log.warn("Exception in M3uaServiceImp.iniSetting: " + e, e);
        }

    }

}

