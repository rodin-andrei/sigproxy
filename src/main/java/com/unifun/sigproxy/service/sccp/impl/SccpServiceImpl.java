package com.unifun.sigproxy.service.sccp.impl;

import com.unifun.sigproxy.exception.InitializingException;
import com.unifun.sigproxy.exception.NoConfigurationException;
import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.service.m3ua.impl.M3uaServiceImpl;
import com.unifun.sigproxy.service.sccp.SccpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImpl;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SccpServiceImpl implements SccpService {
    private final M3uaServiceImpl m3uaService;
    private SccpStackImpl sccpStack;

    @Override
    public void initialize(SigtranStack sigtranStack) throws NoConfigurationException, InitializingException {
        this.sccpStack = new SccpStackImpl(sigtranStack.getStackName(), null);
        sccpStack.setMtp3UserPart(1, m3uaService.getManagement(sigtranStack.getStackName()));
        sccpStack.start();
        sccpStack.removeAllResourses();


    }
}
