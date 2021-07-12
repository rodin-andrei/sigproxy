package com.unifun.sigproxy.service.impl;

import com.unifun.sigproxy.exception.InitializingException;
import com.unifun.sigproxy.exception.NoConfigurationException;
import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.repository.SigtranStackRepository;
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
    private final SigtranStackRepository sigtranStackRepository;
    private final M3uaService m3uaService;
    private final SctpService sctpService;

    @Override
    public void init() {
        log.info("Initializing Sigtran stacks.");
        sigtranStackRepository.findAll()
                .forEach(sigtranStack -> {
                    try {
                        initSctp(sigtranStack);
                        initM3ua(sigtranStack);
                    } catch (NoConfigurationException | InitializingException e) {
                        log.error("Can't initialize Sigtran Stack:{}, cause: {} ",
                                sigtranStack.getStackName(), e.getMessage(), e);
                    }
                });
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
