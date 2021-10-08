package com.unifun.sigproxy.service.map.impl;

import com.unifun.sigproxy.service.map.MapService;
import com.unifun.sigproxy.service.map.SuplementaryDialogFactory;
import com.unifun.sigproxy.service.rabbit.pojo.MapSupplementaryMessageRabbit;
import com.unifun.sigproxy.service.sccp.impl.SccpParametersServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class SupplementaryDialogFactoryImpl implements SuplementaryDialogFactory {
    private final MapService mapService;
    private final SccpParametersServiceImpl sccpParametersService;;

    //TODO fill default if empty message parameters
    //TODO create factory for messages
    @Override
    public MAPDialogSupplementary createDialogProcessUnstructuredSSRequest(MapSupplementaryMessageRabbit mapSupplementaryMessageRabbit) {
        final String stackName = mapSupplementaryMessageRabbit.getStackName();
        MAPProvider mapProvider = mapService.getMapStack(stackName).getMAPProvider();
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

    @Override
    public MAPDialogSupplementary createDialogUnstructuredSSRequest(MapSupplementaryMessageRabbit mapSupplementaryMessageRabbit) {
        return null;
    }

    @Override
    public MAPDialogSupplementary createDialogProcessUnstructuredSSResponse(MapSupplementaryMessageRabbit mapSupplementaryMessageRabbit) {
        return null;
    }

    @Override
    public MAPDialogSupplementary createDialogUnstructuredSSResponse(MapSupplementaryMessageRabbit mapSupplementaryMessageRabbit) {
        return null;
    }

    @Override
    public MAPDialogSupplementary createDialogProcessUnstructuredSSNotifyRequest(MapSupplementaryMessageRabbit mapSupplementaryMessageRabbit) {
        return null;
    }

    @Override
    public MAPDialogSupplementary createDialogProcessUnstructuredSSNotifyResponse(MapSupplementaryMessageRabbit mapSupplementaryMessageRabbit) {
        return null;
    }
}
