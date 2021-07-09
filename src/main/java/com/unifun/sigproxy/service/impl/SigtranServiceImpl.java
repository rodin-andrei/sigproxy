package com.unifun.sigproxy.service.impl;

import com.unifun.sigproxy.exception.InitializingException;
import com.unifun.sigproxy.exception.NoConfigurationException;
import com.unifun.sigproxy.service.M3uaService;
import com.unifun.sigproxy.service.SctpService;
import com.unifun.sigproxy.service.SigtranService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SigtranServiceImpl implements SigtranService {
    private final SctpService sctpService;
    private final M3uaService m3uaService;

    @Override
    public void init() {
        log.info("Initializing Sigtran stack.");
        try {
            initSctp();
            initM3ua();
        } catch (NoConfigurationException | InitializingException e) {
            log.error("Can't initialize Sigtran Stack: " + e.getMessage(), e);
        }
    }

    private void initM3ua() throws NoConfigurationException, InitializingException {
        log.info("Initializing M3UA Layer.");
        m3uaService.initialize();
    }

    private void initSctp() throws NoConfigurationException, InitializingException {
        log.info("Initializing SCTP Layer.");
        sctpService.initialize();
    }
}
