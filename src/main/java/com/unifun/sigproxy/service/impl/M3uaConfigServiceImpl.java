package com.unifun.sigproxy.service.impl;

import com.unifun.sigproxy.exception.InitializingException;
import com.unifun.sigproxy.exception.NoConfigurationException;
import com.unifun.sigproxy.model.config.M3uaConfig;
import com.unifun.sigproxy.model.config.SigtranConfig;
import com.unifun.sigproxy.service.M3uaConfigService;
import com.unifun.sigproxy.service.M3uaService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
@RequiredArgsConstructor
public class M3uaConfigServiceImpl implements M3uaConfigService {
    private static final Logger LOGGER = LoggerFactory.getLogger(M3uaConfigServiceImpl.class);

    private final SigtranConfig sigtranConfig;
    private final M3uaService m3uaService;


    @PostConstruct
    public void init() {
        try {
            LOGGER.info("Initializing SCTP Layer.");
            m3uaService.initialize(getM3uaConfig());
        } catch (NoConfigurationException | InitializingException e) {
            LOGGER.error("Can't initialize sctp configuration: ", e);
        }
    }

    @PreDestroy
    public void destroy() {
        //TODO: Stop M3uaLayer
    }

    @Override
    public M3uaConfig getM3uaConfig() throws NoConfigurationException {
        M3uaConfig m3uaConfig = sigtranConfig.getM3uaConfig();
        if (m3uaConfig == null) {
            throw new NoConfigurationException("No M3ua Configuration.");
        }
        return m3uaConfig;
    }


}
