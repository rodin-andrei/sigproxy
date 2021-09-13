package com.unifun.sigproxy.service.tcap.impl;

import com.unifun.sigproxy.exception.InitializingException;
import com.unifun.sigproxy.exception.NoConfigurationException;
import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.models.config.tcap.TcapConfig;
import com.unifun.sigproxy.service.sccp.SccpService;
import com.unifun.sigproxy.service.tcap.TcapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.restcomm.protocols.ss7.sccp.SccpProvider;
import org.restcomm.protocols.ss7.tcap.TCAPStackImpl;
import org.restcomm.protocols.ss7.tcap.api.TCAPStack;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
        TcapConfig tcapConfig = sigtranStack.getTcapConfig();
        SccpProvider sccpProvider = sccpService.getSccpProvider(sigtranStack.getStackName());

        TCAPStackImpl tcapStack = new TCAPStackImpl(sigtranStack.getStackName(),
                sccpProvider,
                tcapConfig.getLocalSsn()
        );
        this.tcapStacks.put(sigtranStack.getStackName(), tcapStack);
        tcapStack.setPersistDir(jssPersistDir);
        IntStream stream = Arrays.stream(tcapConfig.getAdditionalSsns());
        Stream<Integer> boxed = stream.boxed();
        List<Integer> collect = boxed.collect(Collectors.toList());
        try {
            tcapStack.setExtraSsns(collect);
            tcapStack.start();
        } catch (Exception e) {
            throw new InitializingException("Can't initialize tcap management: " + sigtranStack.getStackName(), e);
        }
    }

    @Override
    public TCAPStack getTcapStack(String stackName) {
        return this.tcapStacks.get(stackName);
    }

}
