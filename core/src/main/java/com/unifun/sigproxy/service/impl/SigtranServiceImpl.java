package com.unifun.sigproxy.service.impl;

import com.unifun.sigproxy.exception.InitializingException;
import com.unifun.sigproxy.exception.NoConfigurationException;
import com.unifun.sigproxy.repository.SigtranRepository;
import com.unifun.sigproxy.service.M3uaService;
import com.unifun.sigproxy.service.SctpService;
import com.unifun.sigproxy.service.SigtranService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@AllArgsConstructor
public class SigtranServiceImpl implements SigtranService {


    private static final Logger LOGGER = LoggerFactory.getLogger(SigtranServiceImpl.class);

    private final SigtranRepository sigtranRepository;
    private SctpService sctpService;
    private M3uaService m3uaService;

    @PostConstruct
    private void init() {
        sigtranRepository.init();
        LOGGER.info("Initializing Sigtran stack.");
        try {
            initSctp();
            initM3ua();
        } catch (NoConfigurationException | InitializingException e) {
            LOGGER.error("Can't initialize Sigtran Stack: ", e);
        }
    }

    private void initM3ua() throws NoConfigurationException, InitializingException {
        try {
            LOGGER.info("Initializing M3UA Layer.");
            m3uaService.initialize(sigtranRepository.getM3uaConfig());
        } catch (NoConfigurationException | InitializingException e) {
            throw e;
        }
    }

    private void initSctp() throws NoConfigurationException, InitializingException {
        try {
            LOGGER.info("Initializing SCTP Layer.");
            sctpService.initialize(sigtranRepository.getSctpConfig());
        } catch (NoConfigurationException | InitializingException e) {
            throw e;
        }
    }
}
