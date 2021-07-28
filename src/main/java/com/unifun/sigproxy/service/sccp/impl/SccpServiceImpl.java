package com.unifun.sigproxy.service.sccp.impl;

import com.unifun.sigproxy.exception.InitializingException;
import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.models.config.sccp.SccpServiceAccessPointConfig;
import com.unifun.sigproxy.repository.sccp.SccpServiceAccessPointConfigRepository;
import com.unifun.sigproxy.service.m3ua.impl.M3uaServiceImpl;
import com.unifun.sigproxy.service.sccp.SccpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.restcomm.protocols.ss7.sccp.Router;
import org.restcomm.protocols.ss7.sccp.SccpProvider;
import org.restcomm.protocols.ss7.sccp.SccpResource;
import org.restcomm.protocols.ss7.sccp.impl.SccpRoutingControl;
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

    private final SccpServiceAccessPointConfigRepository sccpServiceAccessPointConfigRepository;

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
        SccpStackImpl sccpStack = this.sccpStacks.get(sigtranStack.getStackName());

        sigtranStack.getSccpServiceAccessPointConfigs().forEach(sccpServiceAccessPointConfig -> {
            this.addMtp3ServiceAccessPoint(sccpServiceAccessPointConfig, sigtranStack.getStackName());
        });


        SccpProvider sccpProvider = sccpStack.getSccpProvider();


        Router router = sccpStack.getRouter();
        SccpResource sccpResource = sccpStack.getSccpResource();
        SccpRoutingControl sccpRoutingControl = sccpStack.getSccpRoutingControl();

    }

    public void addMtp3ServiceAccessPoint(SccpServiceAccessPointConfig sccpServiceAccessPointConfig, String stackName) {
        try {
            sccpStacks.get(stackName).getRouter().addMtp3ServiceAccessPoint(sccpServiceAccessPointConfig.getId(),
                    sccpServiceAccessPointConfig.getMtp3Id(),
                    sccpServiceAccessPointConfig.getOpc(),
                    sccpServiceAccessPointConfig.getNi(),
                    sccpServiceAccessPointConfig.getNetworkId(),
                    sccpServiceAccessPointConfig.getLocalGlobalTitleDigits());
            log.info("Added ServiceAccessPoint: {} to sigtran stack: {}", sccpServiceAccessPointConfig, stackName);
        } catch (Exception e) {
            log.warn("Error add {}, cause: {}", sccpServiceAccessPointConfig, e.getMessage(), e);
        }
    }

    @Override
    public SccpProvider getSccpProvider(String stackName) {
        return sccpStacks.get(stackName).getSccpProvider();
    }
}
