package com.unifun.sigproxy.service.map.impl;

import com.unifun.sigproxy.exception.InitializingException;
import com.unifun.sigproxy.exception.NoConfigurationException;
import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.service.map.MapService;
import com.unifun.sigproxy.service.map.listeners.MAPDialogListenerImpl;
import com.unifun.sigproxy.service.map.listeners.MAPServiceSmsListenerImpl;
import com.unifun.sigproxy.service.map.listeners.MAPServiceSupplementaryListenerImpl;
import com.unifun.sigproxy.service.rabbit.ProducerService;
import com.unifun.sigproxy.service.rabbit.pojo.MapSupplementaryMessageRabbit;
import com.unifun.sigproxy.service.sccp.SccpService;
import com.unifun.sigproxy.service.sccp.impl.SccpParametersServiceImpl;
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
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
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
    private final SccpParametersServiceImpl sccpParametersService;
    private final ProducerService producerService;

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

        MAPProvider mapProvider = mapStack.getMAPProvider();

        mapProvider.addMAPDialogListener(new MAPDialogListenerImpl(mapStack));
        mapProvider.getMAPServiceSms().acivate();
        mapProvider.getMAPServiceSms().addMAPServiceListener(new MAPServiceSmsListenerImpl(mapStack));
        mapProvider.getMAPServiceSupplementary().acivate();
        mapProvider.getMAPServiceSupplementary()
                .addMAPServiceListener(new MAPServiceSupplementaryListenerImpl(mapStack, this.producerService, this.sccpParametersService));
    }

    public MAPStackImpl getMapStack(String stackName){
        return this.mapStacks.get(stackName);
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


    //TODO delete
    @Deprecated
    public MAPDialogSupplementary createDialogProcessUnstructuredSSRequest(MapSupplementaryMessageRabbit mapSupplementaryMessageRabbit) {
        final String stackName = mapSupplementaryMessageRabbit.getStackName();
        MAPProvider mapProvider = this.mapStacks.get(stackName).getMAPProvider();
        MAPParameterFactory mapParameterFactory = mapProvider.getMAPParameterFactory();
        MAPApplicationContextName networkUnstructuredSsContext = MAPApplicationContextName.networkUnstructuredSsContext; //TODO check params
        MAPApplicationContextVersion version = MAPApplicationContextVersion.version2;
        MAPApplicationContext mapContext = MAPApplicationContext.getInstance(networkUnstructuredSsContext, version);
        SccpAddress callingParty = this.sccpParametersService.createSccpAddress(mapSupplementaryMessageRabbit.getCallingParty(), stackName);
        SccpAddress calledParty = this.sccpParametersService.createSccpAddress(mapSupplementaryMessageRabbit.getCalledParty(), stackName);

        ISDNAddressString isdnAddressStringA = mapParameterFactory
                .createISDNAddressString(AddressNature.international_number,
                        NumberingPlan.ISDN,
                        callingParty.getGlobalTitle().getDigits());
        ISDNAddressString isdnAddressStringB = mapParameterFactory
                .createISDNAddressString(AddressNature.international_number,
                        NumberingPlan.ISDN,
                        calledParty.getGlobalTitle().getDigits());

        try {
            MAPDialogSupplementary newDialog = mapProvider.getMAPServiceSupplementary().createNewDialog(
                    mapContext,
                    callingParty,
                    isdnAddressStringA,
                    calledParty,
                    isdnAddressStringB);

            ISDNAddressString msisdn = new ISDNAddressStringImpl(
                    AddressNature.international_number,
                    NumberingPlan.ISDN,
                    mapSupplementaryMessageRabbit.getMsisdn()); //TODO investigate if msisdn is by deafult with this parameters only
            CBSDataCodingSchemeImpl cbsDataCodingScheme = new CBSDataCodingSchemeImpl(15); //TODO
            newDialog.addProcessUnstructuredSSRequest(cbsDataCodingScheme,
                    new USSDStringImpl(mapSupplementaryMessageRabbit.getUssdString(), cbsDataCodingScheme, StandardCharsets.UTF_8),
                    new AlertingPatternImpl(),
                    msisdn
                    );
            //newDialog.send();
            return newDialog;
        } catch (MAPException e) {
            log.warn(e.getMessage(), e);
        }
        return null;
    }
}
