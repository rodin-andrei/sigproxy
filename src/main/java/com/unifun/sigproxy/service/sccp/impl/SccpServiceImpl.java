package com.unifun.sigproxy.service.sccp.impl;

import com.unifun.sigproxy.exception.InitializingException;
import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.service.m3ua.impl.M3uaServiceImpl;
import com.unifun.sigproxy.service.sccp.SccpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.restcomm.protocols.ss7.sccp.SccpProvider;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SccpServiceImpl implements SccpService {
    private final M3uaServiceImpl m3uaService;
    private final Map<String, SccpStackImpl> sccpStacks = new HashMap<>();

    @Value("${jss.persist.dir}")
    private String jssPersistDir;

    @Override
    public void initialize(SigtranStack sigtranStack) throws InitializingException {
        log.info("Initializing SCCP management...");
        if (sccpStacks.containsKey(sigtranStack.getStackName())) {
            throw new InitializingException("SCCP: " + sigtranStack.getStackName() + " already exist");
        }
        try {
            SccpStackImpl sccpStack = new SccpStackImpl(sigtranStack.getStackName(), null);
            sccpStacks.put(sigtranStack.getStackName(), sccpStack);

            sccpStack.setMtp3UserPart(1, m3uaService.getManagement(sigtranStack.getStackName()));
            sccpStack.setPersistDir(jssPersistDir);
            sccpStack.start();
            sccpStack.removeAllResourses();
            log.info("Created sccp management: {}", sigtranStack.getStackName());


        } catch (Exception e) {
            throw new InitializingException("Can't initialize SCCP Layer. ", e);
        }


    }

    @Override
    public SccpProvider getSccpProvider(String stackName) {
        return sccpStacks.get(stackName).getSccpProvider();
    }
}
