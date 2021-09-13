package com.unifun.sigproxy.service.map.impl;

import com.unifun.sigproxy.exception.InitializingException;
import com.unifun.sigproxy.exception.NoConfigurationException;
import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.service.map.MapService;
import com.unifun.sigproxy.service.sccp.SccpService;
import com.unifun.sigproxy.service.sccp.impl.SccpServiceImpl;
import com.unifun.sigproxy.service.tcap.TcapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.restcomm.protocols.ss7.map.MAPStackImpl;
import org.restcomm.protocols.ss7.map.api.*;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.supplementary.MAPDialogSupplementary;
import org.restcomm.protocols.ss7.map.datacoding.CBSDataCodingSchemeImpl;
import org.restcomm.protocols.ss7.map.primitives.AlertingPatternImpl;
import org.restcomm.protocols.ss7.map.primitives.USSDStringImpl;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class MapServiceImpl implements MapService {
    private final TcapService tcapService;
    private final SccpService sccpService;

    private final Map<String, MAPStackImpl> mapStacks = new HashMap<>();

    @Override
    public void initialize(SigtranStack sigtranStack) throws NoConfigurationException, InitializingException {
        log.info("Initializing MAP management...");
        if (this.mapStacks.containsKey(sigtranStack.getStackName())) {
            throw new InitializingException("MAP: " + sigtranStack.getStackName() + " already exist");
        }

        MAPStackImpl mapStack = new MAPStackImpl(sigtranStack.getStackName(), tcapService.getTcapStack(sigtranStack.getStackName()).getProvider());
        this.mapStacks.put(sigtranStack.getStackName(), mapStack);

        try {
            mapStack.start();
            log.info("Created MAP management: {}", sigtranStack.getStackName());
        } catch (Exception e) {
            throw new InitializingException("Can't initialize MAP Layer. ", e);
        }
    }

    @Override
    public void test(String stackName, int addrA, int addrB) {
        MAPProvider mapProvider = this.mapStacks.get(stackName).getMAPProvider();
        MAPParameterFactory mapParameterFactory = mapProvider.getMAPParameterFactory();
        MAPApplicationContextName networkUnstructuredSsContext = MAPApplicationContextName.networkUnstructuredSsContext;
        MAPApplicationContextVersion version = MAPApplicationContextVersion.version2;
        MAPApplicationContext mapContext = MAPApplicationContext.getInstance(networkUnstructuredSsContext, version);

        SccpAddress sccpAddressA = ((SccpServiceImpl) this.sccpService).addressMap.get(addrA);
        SccpAddress sccpAddressB = ((SccpServiceImpl) this.sccpService).addressMap.get(addrB);

        ISDNAddressString isdnAddressStringA = mapParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, sccpAddressA.getGlobalTitle().getDigits());
        ISDNAddressString isdnAddressStringB = mapParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, sccpAddressB.getGlobalTitle().getDigits());


        try {
            MAPDialogSupplementary newDialog = mapProvider.getMAPServiceSupplementary().createNewDialog(mapContext,
                    sccpAddressA,
                    isdnAddressStringA,
                    sccpAddressB,
                    isdnAddressStringB);

            CBSDataCodingSchemeImpl cbsDataCodingScheme = new CBSDataCodingSchemeImpl(15);
            newDialog.addProcessUnstructuredSSRequest(cbsDataCodingScheme,
                    new USSDStringImpl("HelloUSSD", cbsDataCodingScheme, StandardCharsets.UTF_8),
                    new AlertingPatternImpl(),
                    isdnAddressStringB);

            newDialog.send();
        } catch (MAPException e) {
            log.warn(e.getMessage(), e);
        }

    }
}
