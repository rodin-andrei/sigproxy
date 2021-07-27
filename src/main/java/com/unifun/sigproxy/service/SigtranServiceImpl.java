package com.unifun.sigproxy.service;

import com.unifun.sigproxy.exception.InitializingException;
import com.unifun.sigproxy.exception.NoConfigurationException;
import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.repository.SigtranStackRepository;
import com.unifun.sigproxy.service.m3ua.M3uaService;
import com.unifun.sigproxy.service.sccp.SccpService;
import com.unifun.sigproxy.service.sctp.SctpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SigtranServiceImpl implements SigtranService {
    private final SigtranStackRepository sigtranStackRepository;
    private final M3uaService m3uaService;
    private final SctpService sctpService;
    private final SccpService sccpService;

    @Override
    public void init() {
        log.info("Initializing Sigtran stacks.");
        sigtranStackRepository.findAll()
                .forEach(sigtranStack -> {
                    try {
                        initSctp(sigtranStack);
                        initM3ua(sigtranStack);
                        initSccp(sigtranStack);
                    } catch (NoConfigurationException | InitializingException e) {
                        log.error("Can't initialize Sigtran Stack:{}, cause: {} ",
                                sigtranStack.getStackName(), e.getMessage(), e);
                    }
                });
    }

    private void initSccp(final SigtranStack sigtranStack) throws NoConfigurationException, InitializingException {
        log.info("Initializing SCCP Layer, sigtranStack: {}", sigtranStack.getStackName());
        sccpService.initialize(sigtranStack);
    }

    private void initM3ua(final SigtranStack sigtranStack) throws NoConfigurationException, InitializingException {
        log.info("Initializing M3UA Layer, sigtranStack: {}", sigtranStack.getStackName());
        m3uaService.initialize(sigtranStack);
    }

    private void initSctp(final SigtranStack sigtranStack) throws NoConfigurationException, InitializingException {
        log.info("Initializing SCTP Layer, sigtranStack: {}", sigtranStack.getStackName());
        sctpService.initialize(sigtranStack);
    }
}
