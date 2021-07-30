package com.unifun.sigproxy.service.tcap.impl;

import com.unifun.sigproxy.exception.InitializingException;
import com.unifun.sigproxy.exception.NoConfigurationException;
import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.service.sccp.SccpService;
import com.unifun.sigproxy.service.tcap.TcapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.restcomm.protocols.ss7.tcap.TCAPStackImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TcapServiceImpl implements TcapService {
    private final SccpService sccpService;
    private final Map<String, TCAPStackImpl> tcapStacks = new HashMap<>();

    @Value("${jss.persist.dir}")
    private String jssPersistDir;

    @Override
    public void initialize(SigtranStack sigtranStack) throws NoConfigurationException, InitializingException {
        if (tcapStacks.containsKey(sigtranStack.getStackName())) {
            throw new InitializingException("TcapManagement: " + sigtranStack.getStackName() + " already exist");
        }
        TCAPStackImpl tcapStack = new TCAPStackImpl(sigtranStack.getStackName(),
                sccpService.getSccpProvider(sigtranStack.getStackName()),
                1);
        this.tcapStacks.put(sigtranStack.getStackName(), tcapStack);
        tcapStack.setPersistDir(jssPersistDir);
        try {
            tcapStack.start();
        } catch (Exception e) {
            throw new InitializingException("Can't initialize sctp management: " + sigtranStack.getStackName(), e);
        }


    }

}
