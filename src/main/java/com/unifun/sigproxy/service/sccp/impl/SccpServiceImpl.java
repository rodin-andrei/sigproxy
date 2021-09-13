package com.unifun.sigproxy.service.sccp.impl;


import com.unifun.sigproxy.exception.InitializingException;
import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.models.config.sccp.*;
import com.unifun.sigproxy.repository.sccp.SccpServiceAccessPointConfigRepository;
import com.unifun.sigproxy.service.m3ua.impl.M3uaServiceImpl;
import com.unifun.sigproxy.service.sccp.SccpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.restcomm.protocols.ss7.indicator.AddressIndicator;
import org.restcomm.protocols.ss7.indicator.NatureOfAddress;
import org.restcomm.protocols.ss7.indicator.NumberingPlan;
import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;
import org.restcomm.protocols.ss7.sccp.SccpProvider;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.HopCounterImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.ImportanceImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.restcomm.protocols.ss7.sccp.message.SccpDataMessage;
import org.restcomm.protocols.ss7.sccp.parameter.GlobalTitle;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SccpServiceImpl implements SccpService {
    public final Map<Integer, SccpAddress> addressMap = new HashMap<>();
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
            SccpStackImpl sccpStack = new SccpStackImpl(sigtranStack.getStackName());
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

            sccpServiceAccessPointConfig.getSccpMtp3DestinationConfigs()
                    .forEach(mtp3DestinationConfig -> addMtp3Destination(sigtranStack.getStackName(), mtp3DestinationConfig, sccpServiceAccessPointConfig.getId()));
        });

        sigtranStack.getSccpRemoteSignalingPointConfigs()
                .forEach(sccpRemoteSignalingPointConfig -> addRemoteSpc(sigtranStack.getStackName(), sccpRemoteSignalingPointConfig));

        sigtranStack.getSccpRemoteSubsystemConfigs()
                .forEach(sccpRemoteSubsystemConfig -> addRemoteSsn(sccpStack.getName(), sccpRemoteSubsystemConfig));

        sigtranStack.getSccpAddressConfigs()
                .forEach(sccpAddressConfig -> addAddress(sigtranStack.getStackName(), sccpAddressConfig));

        sigtranStack.getSccpRuleConfigs()
                .forEach(sccpRuleConfig -> addRule(sigtranStack.getStackName(), sccpRuleConfig));

        sigtranStack.getSccpConcernedSignalingPointCodeConfigs()
                .forEach(sccpConcernedSignalingPointCodeConfig -> addCSPC(sigtranStack.getStackName(), sccpConcernedSignalingPointCodeConfig));

        sigtranStack.getSccpLongMessageRuleConfigs()
                .forEach(sccpLongMessageRuleConfig -> addLmr(sigtranStack.getStackName(), sccpLongMessageRuleConfig));

    }

    private void addLmr(String stackName, SccpLongMessageRuleConfig sccpLongMessageRuleConfig) {
        try {
            this.sccpStacks.get(stackName).getRouter().addLongMessageRule(sccpLongMessageRuleConfig.getId(),
                    sccpLongMessageRuleConfig.getFirstSignalingPointCode(),
                    sccpLongMessageRuleConfig.getLastSignalingPointCode(),
                    sccpLongMessageRuleConfig.getLongMessageRuleType());
            log.info("Added {} to stack: {}", sccpLongMessageRuleConfig, stackName);
        } catch (Exception e) {
            log.warn("Error added: {} to stack: {}, cause: {}", sccpLongMessageRuleConfig, stackName, e.getMessage(), e);
        }
    }

    private void addCSPC(String stackName, SccpConcernedSignalingPointCodeConfig sccpConcernedSignalingPointCodeConfig) {
        try {
            sccpStacks.get(stackName).getSccpResource().addConcernedSpc(sccpConcernedSignalingPointCodeConfig.getId(),
                    sccpConcernedSignalingPointCodeConfig.getSignalingPointCode());
            log.info("Added {} to stack: {}", sccpConcernedSignalingPointCodeConfig, stackName);
        } catch (Exception e) {
            log.warn("Error added: {} to stack: {}, cause: {}", sccpConcernedSignalingPointCodeConfig, stackName, e.getMessage(), e);
        }
    }

    @Override
    public void test(String stackName, int addressA, int addressB) {
        SccpStackImpl sccpStack = this.sccpStacks.get(stackName);

        SccpDataMessage dataMessageClass0 = sccpStack.getSccpProvider().getMessageFactory().createDataMessageClass0(
                addressMap.get(addressB),
                addressMap.get(addressA),
                "Hello".getBytes(StandardCharsets.UTF_8),
                8,
                false,
                new HopCounterImpl(10),
                new ImportanceImpl((byte) 1)
        );


        try {
            sccpStack.getSccpProvider().send(dataMessageClass0);
            log.info("Sended");
        } catch (IOException e) {
            log.error("Error send sccp message", e);
        }


    }

    private void addRule(String stackName, SccpRuleConfig sccpRuleConfig) {
        SccpStackImpl sccpStack = this.sccpStacks.get(stackName);
        try {
            sccpStack.getRouter().addRule(sccpRuleConfig.getId(),
                    sccpRuleConfig.getRuleType(),
                    sccpRuleConfig.getLoadSharingAlgorithm(),
                    sccpRuleConfig.getOriginationType(),
                    createAddressByRuleAddress(sccpStack, sccpRuleConfig.getSccpAddressRuleConfig()),
                    sccpRuleConfig.getMask(),
                    sccpRuleConfig.getPrimaryAddressId(),
                    sccpRuleConfig.getSecondaryAddressId(),
                    sccpRuleConfig.getNewCallingPartyAddressAddressId(),
                    sccpRuleConfig.getNetworkId(),
                    createAddressByRuleAddress(sccpStack, sccpRuleConfig.getCallingSccpAddressRuleConfig())
            );
            log.info("Added {} to stack: {}", sccpRuleConfig, stackName);

        } catch (Exception e) {

            log.warn("Error added: {} to stack: {}, cause: {}", sccpRuleConfig, stackName, e.getMessage(), e);
        }
    }

    private SccpAddress createAddressByRuleAddress(SccpStackImpl sccpStack, SccpAddressRuleConfig sccpAddressRuleConfig) {
        if (sccpAddressRuleConfig == null) return null;
        AddressIndicator aiObj = new AddressIndicator(sccpAddressRuleConfig.getAddressIndicator(), SccpProtocolVersion.ITU);

        NumberingPlan np = sccpAddressRuleConfig.getNumberingPlan();
        NatureOfAddress nai = sccpAddressRuleConfig.getNatureOfAddress();

        GlobalTitle gt = null;

        switch (aiObj.getGlobalTitleIndicator()) {
            case GLOBAL_TITLE_INCLUDES_NATURE_OF_ADDRESS_INDICATOR_ONLY:
                gt = sccpStack.getSccpProvider().getParameterFactory().createGlobalTitle(sccpAddressRuleConfig.getDigits(), nai);
                break;
            case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_ONLY:
                gt = sccpStack.getSccpProvider().getParameterFactory().createGlobalTitle(sccpAddressRuleConfig.getDigits(), sccpAddressRuleConfig.getTranslationType());
                break;
            case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_AND_ENCODING_SCHEME:
                gt = sccpStack.getSccpProvider().getParameterFactory().createGlobalTitle(sccpAddressRuleConfig.getDigits(), sccpAddressRuleConfig.getTranslationType(), np, null);
                break;
            case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_ENCODING_SCHEME_AND_NATURE_OF_ADDRESS:
                gt = sccpStack.getSccpProvider().getParameterFactory().createGlobalTitle(sccpAddressRuleConfig.getDigits(), sccpAddressRuleConfig.getTranslationType(), np, null, nai);
                break;

            case NO_GLOBAL_TITLE_INCLUDED:
                gt = sccpStack.getSccpProvider().getParameterFactory().createGlobalTitle(sccpAddressRuleConfig.getDigits());
                break;
        }

        SccpAddress sccpAddress = new SccpAddressImpl(aiObj.getRoutingIndicator(), gt, sccpAddressRuleConfig.getPointCode(), sccpAddressRuleConfig.getSsn());
        return sccpAddress;
    }

    private void addAddress(String stackName, SccpAddressConfig sccpAddressConfig) {
        SccpStackImpl sccpStack = sccpStacks.get(stackName);

        AddressIndicator aiObj = new AddressIndicator(sccpAddressConfig.getAddressIndicator(), SccpProtocolVersion.ITU);
        NumberingPlan np = sccpAddressConfig.getNumberingPlan();
        NatureOfAddress nai = sccpAddressConfig.getNatureOfAddress();

        GlobalTitle gt = null;

        switch (aiObj.getGlobalTitleIndicator()) {
            case GLOBAL_TITLE_INCLUDES_NATURE_OF_ADDRESS_INDICATOR_ONLY:
                gt = sccpStack.getSccpProvider().getParameterFactory().createGlobalTitle(sccpAddressConfig.getDigits(), nai);
                break;
            case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_ONLY:
                gt = sccpStack.getSccpProvider().getParameterFactory().createGlobalTitle(sccpAddressConfig.getDigits(), sccpAddressConfig.getTranslationType());
                break;
            case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_AND_ENCODING_SCHEME:
                gt = sccpStack.getSccpProvider().getParameterFactory().createGlobalTitle(sccpAddressConfig.getDigits(), sccpAddressConfig.getTranslationType(), np, null);
                break;
            case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_ENCODING_SCHEME_AND_NATURE_OF_ADDRESS:
                gt = sccpStack.getSccpProvider().getParameterFactory().createGlobalTitle(sccpAddressConfig.getDigits(), sccpAddressConfig.getTranslationType(), np, null, nai);
                break;

            case NO_GLOBAL_TITLE_INCLUDED:
                gt = sccpStack.getSccpProvider().getParameterFactory().createGlobalTitle(sccpAddressConfig.getDigits());
                break;
        }

        SccpAddress sccpAddress = new SccpAddressImpl(aiObj.getRoutingIndicator(), gt, sccpAddressConfig.getPointCode(), sccpAddressConfig.getSsn());
        try {
            sccpStack.getRouter().addRoutingAddress(sccpAddressConfig.getId(), sccpAddress);
            addressMap.put(sccpAddressConfig.getId(), sccpAddress);
            log.info("Added {} to stack: {}", sccpAddressConfig, stackName);
        } catch (Exception e) {
            log.warn("Error added: {} to stack: {}, cause: {}", sccpAddressConfig, stackName, e.getMessage(), e);
        }
    }

    private void addRemoteSsn(String stackName, SccpRemoteSubsystemConfig sccpRemoteSubsystemConfig) {
        try {
            sccpStacks.get(stackName).getSccpResource().addRemoteSsn(sccpRemoteSubsystemConfig.getId(),
                    sccpRemoteSubsystemConfig.getRemoteSignalingPointCode(),
                    sccpRemoteSubsystemConfig.getRemoteSubsystemNumber(),
                    sccpRemoteSubsystemConfig.getRemoteSubsystemFlag(),
                    sccpRemoteSubsystemConfig.isMarkProhibitedWhenSpcResuming());
            log.info("Added {} to stack: {}", sccpRemoteSubsystemConfig, stackName);
        } catch (Exception e) {
            log.warn("Error added: {} to stack: {}, cause: {}", sccpRemoteSubsystemConfig, stackName, e.getMessage(), e);
        }
    }

    private void addRemoteSpc(String stackName, SccpRemoteSignalingPointConfig sccpRemoteSignalingPointConfig) {
        try {
            sccpStacks.get(stackName).getSccpResource().addRemoteSpc(
                    sccpRemoteSignalingPointConfig.getId(),
                    sccpRemoteSignalingPointConfig.getRemoteSignalingPointCode(),
                    sccpRemoteSignalingPointConfig.getRspcFlag(),
                    sccpRemoteSignalingPointConfig.getMask()
            );
            log.info("Added {} to stack {}", sccpRemoteSignalingPointConfig, stackName);
        } catch (Exception e) {
            log.warn("Error added {} to stack: {}, cause: {}",
                    sccpRemoteSignalingPointConfig, stackName, e.getMessage(), e);
        }
    }

    private void addMtp3Destination(String stackname, SccpMtp3DestinationConfig mtp3DestinationConfig, Integer sapId) {
        try {
            this.sccpStacks.get(stackname).getRouter().addMtp3Destination(sapId,
                    mtp3DestinationConfig.getId(),
                    mtp3DestinationConfig.getFirstSignalingPointCode(),
                    mtp3DestinationConfig.getLastSignalingPointCode(),
                    mtp3DestinationConfig.getFirstSls(),
                    mtp3DestinationConfig.getLastSls(),
                    mtp3DestinationConfig.getSlsMask());
            log.info("Added {} to stack {}", mtp3DestinationConfig, stackname);
        } catch (Exception e) {
            log.warn("Error added {} to stack {}", mtp3DestinationConfig, stackname, e);
        }
    }

    public void addMtp3ServiceAccessPoint(SccpServiceAccessPointConfig sccpServiceAccessPointConfig, String stackName) {
        try {
            this.sccpStacks.get(stackName).getRouter().addMtp3ServiceAccessPoint(sccpServiceAccessPointConfig.getId(),
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
