package com.unifun.sigproxy.service;

import com.unifun.sigproxy.exception.InitializingException;
import com.unifun.sigproxy.exception.NoConfigurationException;
import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.repository.SigtranStackRepository;
import com.unifun.sigproxy.service.m3ua.M3uaService;
import com.unifun.sigproxy.service.map.MapService;
import com.unifun.sigproxy.service.sccp.SccpService;
import com.unifun.sigproxy.service.sctp.SctpService;
import com.unifun.sigproxy.service.tcap.TcapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SigtranServiceImpl implements SigtranService {
    private final SigtranStackRepository sigtranStackRepository;
    private final M3uaService m3uaService;
    private final SctpService sctpService;
    private final SccpService sccpService;
    private final TcapService tcapService;
    private final MapService mapService;


    @EventListener
    public void init(ContextStartedEvent event) {
        log.info("Initializing Sigtran stacks.");
        sigtranStackRepository.findAll()
                .forEach(sigtranStack -> {
                    try {
                        initSctp(sigtranStack);
                        initM3ua(sigtranStack);
                        initSccp(sigtranStack);
                        initTcap(sigtranStack);
                        initMap(sigtranStack);
                    } catch (NoConfigurationException | InitializingException e) {
                        log.error("Can't initialize Sigtran Stack:{}, cause: {} ",
                                sigtranStack.getStackName(), e.getMessage(), e);
                    }
                });
    }

    private void initMap(SigtranStack sigtranStack) throws NoConfigurationException, InitializingException {
        log.info("Initializing Map Layer, sigtranStack: {}", sigtranStack.getStackName());
        mapService.initialize(sigtranStack);
    }

    private void initTcap(SigtranStack sigtranStack) throws NoConfigurationException, InitializingException {
        log.info("Initializing Tcap Layer, sigtranStack: {}", sigtranStack.getStackName());
        tcapService.initialize(sigtranStack);
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
