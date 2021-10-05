package com.unifun.sigproxy.controller.dto.service;

import com.unifun.sigproxy.controller.dto.SigtranStackDto;
import com.unifun.sigproxy.controller.dto.m3ua.M3uaAsConfigDto;
import com.unifun.sigproxy.controller.dto.m3ua.M3uaAspConfigDto;
import com.unifun.sigproxy.controller.dto.m3ua.M3uaRouteConfigDto;
import com.unifun.sigproxy.controller.dto.m3ua.M3uaStackSettingsConfigDto;
import com.unifun.sigproxy.controller.dto.sccp.*;
import com.unifun.sigproxy.controller.dto.sctp.SctpClientAssociationConfigDto;
import com.unifun.sigproxy.controller.dto.sctp.SctpServerAssociationConfigDto;
import com.unifun.sigproxy.controller.dto.sctp.SctpServerConfigDto;
import com.unifun.sigproxy.controller.dto.sctp.SctpStackSettingsConfigDto;
import com.unifun.sigproxy.controller.dto.tcap.TcapConfigDto;
import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.models.config.m3ua.M3uaAsConfig;
import com.unifun.sigproxy.models.config.m3ua.M3uaAspConfig;
import com.unifun.sigproxy.models.config.m3ua.M3uaRouteConfig;
import com.unifun.sigproxy.models.config.m3ua.M3uaStackSettingsConfig;
import com.unifun.sigproxy.models.config.sccp.*;
import com.unifun.sigproxy.models.config.sctp.SctpClientAssociationConfig;
import com.unifun.sigproxy.models.config.sctp.SctpServerAssociationConfig;
import com.unifun.sigproxy.models.config.sctp.SctpServerConfig;
import com.unifun.sigproxy.models.config.sctp.SctpStackSettingsConfig;
import com.unifun.sigproxy.models.config.tcap.TcapConfig;
import com.unifun.sigproxy.service.sctp.SctpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreatorDataObjectService {

    private final SctpService sctpService;

    public SctpClientAssociationConfigDto createSctpClientAssociationConfigDto(SctpClientAssociationConfig clientAssociation) {
        if (clientAssociation == null) return null;
        return SctpClientAssociationConfigDto.builder()
                .id(clientAssociation.getId())
                .linkName(clientAssociation.getLinkName())
                .localAddress(clientAssociation.getLocalAddress())
                .localPort(clientAssociation.getLocalPort())
                .multihomingAddresses(clientAssociation.getMultihomingAddresses())
                .remoteAddress(clientAssociation.getRemoteAddress())
                .remotePort(clientAssociation.getRemotePort())
                .status(sctpService.getLinkStatus(clientAssociation.getSigtranStack().getStackName(), clientAssociation.getLinkName()))
                .build();
    }

    public SctpServerAssociationConfigDto createSctpServerAssociationConfigDto(SctpServerAssociationConfig sctpServerAssociationConfigs) {
        if (sctpServerAssociationConfigs == null) return null;
        return SctpServerAssociationConfigDto.builder()
                .id(sctpServerAssociationConfigs.getId())
                .linkName(sctpServerAssociationConfigs.getLinkName())
                .remoteAddress(sctpServerAssociationConfigs.getRemoteAddress())
                .remotePort(sctpServerAssociationConfigs.getRemotePort())
                .status(sctpService.getLinkStatus(sctpServerAssociationConfigs.getSctpServerConfig().getSigtranStack().getStackName(), sctpServerAssociationConfigs.getLinkName()))
                .build();


    }


    public SctpServerConfigDto createSctpServerConfigDto(SctpServerConfig sctpServerConfig) {
        if (sctpServerConfig == null) return null;
        return SctpServerConfigDto.builder()
                .id(sctpServerConfig.getId())
                .localAddress(sctpServerConfig.getLocalAddress())
                .localPort(sctpServerConfig.getLocalPort())
                .multihomingAddresses(sctpServerConfig.getMultihomingAddresses())
                .name(sctpServerConfig.getName())
                .sctpServerAssociationConfigs(
                        Optional.ofNullable(sctpServerConfig.getSctpServerAssociationConfigs()).orElse(new HashSet<>())
                                .stream()
                                .map(this::createSctpServerAssociationConfigDto)
                                .collect(Collectors.toSet())
                )
                .build();

    }

    public SctpStackSettingsConfigDto createSctpStackSettingsConfigDto(SctpStackSettingsConfig sctpStackSettingsConfig) {
        if (sctpStackSettingsConfig == null) return null;
        return SctpStackSettingsConfigDto.builder()
                .id(sctpStackSettingsConfig.getId())
                .congControl_BackToNormalDelayThreshold_1(sctpStackSettingsConfig.getCongControl_BackToNormalDelayThreshold_1())
                .congControl_BackToNormalDelayThreshold_2(sctpStackSettingsConfig.getCongControl_BackToNormalDelayThreshold_2())
                .congControl_BackToNormalDelayThreshold_3(sctpStackSettingsConfig.getCongControl_BackToNormalDelayThreshold_3())
                .congControl_DelayThreshold_1(sctpStackSettingsConfig.getCongControl_DelayThreshold_1())
                .congControl_DelayThreshold_2(sctpStackSettingsConfig.getCongControl_DelayThreshold_2())
                .congControl_DelayThreshold_3(sctpStackSettingsConfig.getCongControl_DelayThreshold_3())
                .setConnectDelay(sctpStackSettingsConfig.getSetConnectDelay())
                .optionSctpDisableFragments(sctpStackSettingsConfig.isOptionSctpDisableFragments())
                .optionSctpFragmentInterleave(sctpStackSettingsConfig.getOptionSctpFragmentInterleave())
                .optionSctpInitMaxStreams_MaxInStreams(sctpStackSettingsConfig.getOptionSctpInitMaxStreams_MaxInStreams())
                .optionSctpInitMaxStreams_MaxOutStreams(sctpStackSettingsConfig.getOptionSctpInitMaxStreams_MaxOutStreams())
                .optionSctpNodelay(sctpStackSettingsConfig.isOptionSctpNodelay())
                .optionSoLinger(sctpStackSettingsConfig.getOptionSoLinger())
                .optionSoRcvbuf(sctpStackSettingsConfig.getOptionSoRcvbuf())
                .optionSoSndbuf(sctpStackSettingsConfig.getOptionSoSndbuf())
                .singleThread(sctpStackSettingsConfig.isSingleThread())
                .workerThreads(sctpStackSettingsConfig.getWorkerThreads())
                .build();

    }

    public M3uaAspConfigDto createM3uaAspConfigDto(M3uaAspConfig m3uaAspConfig) {
        if (m3uaAspConfig == null) return null;
        return M3uaAspConfigDto.builder()
                .id(m3uaAspConfig.getId())
                .name(m3uaAspConfig.getName())
                .sctpAssocName(m3uaAspConfig.getSctpAssocName())
                .heartbeat(m3uaAspConfig.isHeartbeat())
                .build();
    }


    public M3uaRouteConfigDto createM3uaRouteConfigDto(M3uaRouteConfig m3uaRouteConfig) {
        if (m3uaRouteConfig == null) return null;
        return M3uaRouteConfigDto.builder()
                .id(m3uaRouteConfig.getId())
                .opc(m3uaRouteConfig.getOpc())
                .dpc(m3uaRouteConfig.getDpc())
                .si(m3uaRouteConfig.getSi())
                .trafficModeType(m3uaRouteConfig.getTrafficModeType())
                .build();

    }

    public M3uaAsConfigDto createM3uaAsConfigDto(M3uaAsConfig m3uaAsConfig) {
        if (m3uaAsConfig == null) return null;
        return M3uaAsConfigDto.builder()
                .id(m3uaAsConfig.getId())
                .name(m3uaAsConfig.getName())
                .functionality(m3uaAsConfig.getFunctionality())
                .exchangeType(m3uaAsConfig.getExchangeType())
                .ipspType(m3uaAsConfig.getIpspType())
                .trafficModeType(m3uaAsConfig.getTrafficModeType())
                .networkIndicator(m3uaAsConfig.getNetworkIndicator())
                .networkAppearance(m3uaAsConfig.getNetworkAppearance())
                .routingContexts(m3uaAsConfig.getRoutingContexts())
                .applicationServerPointsDto(
                        Optional.ofNullable(m3uaAsConfig.getApplicationServerPoints()).orElse(new HashSet<>())
                                .stream()
                                .map(this::createM3uaAspConfigDto)
                                .collect(Collectors.toSet()))
                .routesDto(
                        Optional.ofNullable(m3uaAsConfig.getRoutes()).orElse(new HashSet<>())
                                .stream()
                                .map(this::createM3uaRouteConfigDto)
                                .collect(Collectors.toSet()))
                .build();

    }

    public M3uaStackSettingsConfigDto createM3uaStackSettingsConfigDto(M3uaStackSettingsConfig m3UaStackSettingsConfig) {
        if (m3UaStackSettingsConfig == null) return null;
        return M3uaStackSettingsConfigDto.builder()
                .id(m3UaStackSettingsConfig.getId())
                .deliveryMessageThreadCount(m3UaStackSettingsConfig.getDeliveryMessageThreadCount())
                .heartbeatTime(m3UaStackSettingsConfig.getHeartbeatTime())
                .maxAsForRoute(m3UaStackSettingsConfig.getMaxAsForRoute())
                .maxSequenceNumber(m3UaStackSettingsConfig.getMaxSequenceNumber())
                .routingKeyManagementEnabled(m3UaStackSettingsConfig.isRoutingKeyManagementEnabled())
                .routingLabelFormat(m3UaStackSettingsConfig.getRoutingLabelFormat())
                .statisticsEnabled(m3UaStackSettingsConfig.isStatisticsEnabled())
                .useLsbForLinksetSelection(m3UaStackSettingsConfig.isUseLsbForLinksetSelection())
                .build();

    }


    public SccpAddressRuleConfigDto createSccpAddressRuleConfigDto(SccpAddressRuleConfig sccpAddressRuleConfig) {
        if (sccpAddressRuleConfig == null) return null;
        return SccpAddressRuleConfigDto.builder()
                .id(sccpAddressRuleConfig.getId())
                .addressIndicator(sccpAddressRuleConfig.getAddressIndicator())
                .pointCode(sccpAddressRuleConfig.getPointCode())
                .ssn(sccpAddressRuleConfig.getSsn())
                .translationType(sccpAddressRuleConfig.getTranslationType())
                .numberingPlan(sccpAddressRuleConfig.getNumberingPlan())
                .natureOfAddress(sccpAddressRuleConfig.getNatureOfAddress())
                .digits(sccpAddressRuleConfig.getDigits())
                .build();
    }

    public SccpRuleConfigDto createSccpRuleConfigsDto(SccpRuleConfig sccpRuleConfig) {
        if (sccpRuleConfig == null) return null;
        return SccpRuleConfigDto.builder()
                .id(sccpRuleConfig.getId())
                .mask(sccpRuleConfig.getMask())
                .sccpAddressRuleConfig(createSccpAddressRuleConfigDto(sccpRuleConfig.getSccpAddressRuleConfig()))
                .ruleType(sccpRuleConfig.getRuleType())
                .primaryAddressId(sccpRuleConfig.getPrimaryAddressId())
                .loadSharingAlgorithm(sccpRuleConfig.getLoadSharingAlgorithm())
                .originationType(sccpRuleConfig.getOriginationType())
                .secondaryAddressId(sccpRuleConfig.getSecondaryAddressId())
                .newCallingPartyAddressAddressId(sccpRuleConfig.getNewCallingPartyAddressAddressId())
                .networkId(sccpRuleConfig.getNetworkId())
                .callingSccpAddressRuleConfig(createSccpAddressRuleConfigDto(sccpRuleConfig.getCallingSccpAddressRuleConfig()))
                .build();
    }

    public SccpRemoteSignalingPointConfigDto createSccpRemoteSignalingPointConfigsDto(SccpRemoteSignalingPointConfig sccpRemoteSignalingPointConfig) {
        if (sccpRemoteSignalingPointConfig == null) return null;
        return SccpRemoteSignalingPointConfigDto
                .builder()
                .id(sccpRemoteSignalingPointConfig.getId())
                .remoteSignalingPointCode(sccpRemoteSignalingPointConfig.getRemoteSignalingPointCode())
                .rspcFlag(sccpRemoteSignalingPointConfig.getRspcFlag())
                .mask(sccpRemoteSignalingPointConfig.getMask())
                .build();
    }

    public SccpAddressConfigDto createSccpAddressConfigsDto(SccpAddressConfig sccpAddressConfig) {
        if (sccpAddressConfig == null) return null;
        return SccpAddressConfigDto
                .builder()
                .id(sccpAddressConfig.getId())
                .addressIndicator(sccpAddressConfig.getAddressIndicator())
                .pointCode(sccpAddressConfig.getPointCode())
                .ssn(sccpAddressConfig.getSsn())
                .translationType(sccpAddressConfig.getTranslationType())
                .numberingPlan(sccpAddressConfig.getNumberingPlan())
                .natureOfAddress(sccpAddressConfig.getNatureOfAddress())
                .digits(sccpAddressConfig.getDigits())
                .build();
    }

    public SccpRemoteSubsystemConfigDto createSccpRemoteSubsystemConfigDto(SccpRemoteSubsystemConfig sccpRemoteSubsystemConfig) {
        if (sccpRemoteSubsystemConfig == null) return null;
        return SccpRemoteSubsystemConfigDto.builder()
                .id(sccpRemoteSubsystemConfig.getId())
                .remoteSignalingPointCode(sccpRemoteSubsystemConfig.getRemoteSignalingPointCode())
                .remoteSubsystemNumber(sccpRemoteSubsystemConfig.getRemoteSubsystemNumber())
                .remoteSubsystemFlag(sccpRemoteSubsystemConfig.getRemoteSubsystemFlag())
                .markProhibitedWhenSpcResuming(sccpRemoteSubsystemConfig.isMarkProhibitedWhenSpcResuming())
                .build();
    }

    public SccpMtp3DestinationConfigDto createSccpMtp3DestinationConfigDto(SccpMtp3DestinationConfig sccpMtp3DestinationConfig) {
        if (sccpMtp3DestinationConfig == null) return null;
        return SccpMtp3DestinationConfigDto.builder()
                .id(sccpMtp3DestinationConfig.getId())
                .firstSignalingPointCode(sccpMtp3DestinationConfig.getFirstSignalingPointCode())
                .lastSignalingPointCode(sccpMtp3DestinationConfig.getLastSignalingPointCode())
                .firstSls(sccpMtp3DestinationConfig.getFirstSls())
                .lastSls(sccpMtp3DestinationConfig.getLastSls())
                .slsMask(sccpMtp3DestinationConfig.getSlsMask())
                .build();

    }

    public SccpServiceAccessPointConfigDto createSccpServiceAccessPointConfigDto(SccpServiceAccessPointConfig sccpServiceAccessPointConfig) {
        if (sccpServiceAccessPointConfig == null) return null;
        return SccpServiceAccessPointConfigDto.builder()
                .id(sccpServiceAccessPointConfig.getId())
                .mtp3Id(sccpServiceAccessPointConfig.getMtp3Id())
                .opc(sccpServiceAccessPointConfig.getOpc())
                .ni(sccpServiceAccessPointConfig.getNi())
                .networkId(sccpServiceAccessPointConfig.getNetworkId())
                .localGlobalTitleDigits(sccpServiceAccessPointConfig.getLocalGlobalTitleDigits())
                .sccpMtp3DestinationConfigs(
                        Optional.ofNullable(sccpServiceAccessPointConfig.getSccpMtp3DestinationConfigs()).orElse(new HashSet<>())
                                .stream()
                                .map(this::createSccpMtp3DestinationConfigDto)
                                .collect(Collectors.toSet())
                )
                .build();
    }

    public SccpConcernedSignalingPointCodeConfigDto createSccpConcernedSignalingPointCodeConfigDto(SccpConcernedSignalingPointCodeConfig sccpConcernedSignalingPointCodeConfig) {
        if (sccpConcernedSignalingPointCodeConfig == null) return null;
        return SccpConcernedSignalingPointCodeConfigDto.builder()
                .id(sccpConcernedSignalingPointCodeConfig.getId())
                .signalingPointCode(sccpConcernedSignalingPointCodeConfig.getSignalingPointCode())
                .build();
    }

    public SccpSettingsConfigDto createSccpSettingsConfigDto(SccpSettingsConfig sccpSettingsConfig) {
        if (sccpSettingsConfig == null) return null;
        return SccpSettingsConfigDto.builder()
                .id(sccpSettingsConfig.getId())
                .zmarginxudtmessage(sccpSettingsConfig.getZmarginxudtmessage())
                .reassemblytimerdelay(sccpSettingsConfig.getReassemblytimerdelay())
                .maxdatamessage(sccpSettingsConfig.getMaxdatamessage())
                .periodoflogging(sccpSettingsConfig.getPeriodoflogging())
                .removespc(sccpSettingsConfig.isRemovespc())
                .previewmode(sccpSettingsConfig.isPreviewmode())
                .ssttimerduration_min(sccpSettingsConfig.getSsttimerduration_min())
                .ssttimerduration_max(sccpSettingsConfig.getSsttimerduration_max())
                .ssttimerduration_increasefactor(sccpSettingsConfig.getSsttimerduration_increasefactor())
                .sccpprotocolversion(sccpSettingsConfig.getSccpprotocolversion())
                .cc_timer_a(sccpSettingsConfig.getCc_timer_a())
                .cc_timer_d(sccpSettingsConfig.getCc_timer_d())
                .canrelay(sccpSettingsConfig.isCanrelay())
                .connesttimerdelay(sccpSettingsConfig.getConnesttimerdelay())
                .iastimerdelay(sccpSettingsConfig.getIastimerdelay())
                .iartimerdelay(sccpSettingsConfig.getIartimerdelay())
                .reltimerdelay(sccpSettingsConfig.getReltimerdelay())
                .repeatreltimerdelay(sccpSettingsConfig.getRepeatreltimerdelay())
                .inttimerdelay(sccpSettingsConfig.getInttimerdelay())
                .guardtimerdelay(sccpSettingsConfig.getGuardtimerdelay())
                .resettimerdelay(sccpSettingsConfig.getResettimerdelay())
                .timerexecutors_threadcount(sccpSettingsConfig.getTimerexecutors_threadcount())
                .cc_algo(sccpSettingsConfig.getCc_algo())
                .cc_blockingoutgoungsccpmessages(sccpSettingsConfig.isCc_blockingoutgoungsccpmessages())
                .build();
    }

    public TcapConfigDto createTcapConfigDto(TcapConfig tcapConfig) {
        if (tcapConfig == null) return null;
        return TcapConfigDto.builder()
                .id(tcapConfig.getId())
                .localSsn(tcapConfig.getLocalSsn())
                .additionalSsns(tcapConfig.getAdditionalSsns())
                .build();
    }

    public SigtranStackDto createSigtranStackDto(SigtranStack sigtranStack) {
        return SigtranStackDto.builder()
                .id(sigtranStack.getId())
                .stackName(sigtranStack.getStackName())
                .sctpServerConfigsDto(
                        Optional.ofNullable(sigtranStack.getSctpServerConfigs()).orElse(new HashSet<>())
                                .stream()
                                .map(this::createSctpServerConfigDto)
                                .collect(Collectors.toSet())
                )
                .associationsDto(
                        Optional.ofNullable(sigtranStack.getAssociations()).orElse(new HashSet<>())
                                .stream()
                                .map(this::createSctpClientAssociationConfigDto)
                                .collect(Collectors.toSet()))
                .sctpStackSettingsConfigDto(createSctpStackSettingsConfigDto(sigtranStack.getSctpStackSettingsConfig()))
                .applicationServersDto(
                        Optional.ofNullable(sigtranStack.getApplicationServers()).orElse(new HashSet<>())
                                .stream()
                                .map(this::createM3uaAsConfigDto)
                                .collect(Collectors.toSet())
                )
                .m3UaStackSettingsConfigDto(createM3uaStackSettingsConfigDto(sigtranStack.getM3UaStackSettingsConfig()))
                .sccpRuleConfigsDto(
                        Optional.ofNullable(sigtranStack.getSccpRuleConfigs()).orElse(new HashSet<>())
                                .stream()
                                .map(this::createSccpRuleConfigsDto)
                                .collect(Collectors.toSet()))
                .sccpRemoteSignalingPointConfigsDto(
                        Optional.ofNullable(sigtranStack.getSccpRemoteSignalingPointConfigs()).orElse(new HashSet<>())
                                .stream()
                                .map(this::createSccpRemoteSignalingPointConfigsDto)
                                .collect(Collectors.toSet())
                )
                .sccpAddressConfigsDto(
                        Optional.ofNullable(sigtranStack.getSccpAddressConfigs()).orElse(new HashSet<>())
                                .stream()
                                .map(this::createSccpAddressConfigsDto)
                                .collect(Collectors.toSet())
                )
                .sccpRemoteSubsystemConfigsDto(
                        Optional.ofNullable(sigtranStack.getSccpRemoteSubsystemConfigs()).orElse(new HashSet<>())
                                .stream()
                                .map(this::createSccpRemoteSubsystemConfigDto)
                                .collect(Collectors.toSet())
                )
                .sccpServiceAccessPointConfigsDto(
                        Optional.ofNullable(sigtranStack.getSccpServiceAccessPointConfigs()).orElse(new HashSet<>())
                                .stream()
                                .map(this::createSccpServiceAccessPointConfigDto)
                                .collect(Collectors.toSet())
                )
                .sccpConcernedSignalingPointCodeConfigsDto(
                        Optional.ofNullable(sigtranStack.getSccpConcernedSignalingPointCodeConfigs()).orElse(new HashSet<>())
                                .stream()
                                .map(this::createSccpConcernedSignalingPointCodeConfigDto)
                                .collect(Collectors.toSet())
                )
                .sccpSettingsConfigDto(createSccpSettingsConfigDto(sigtranStack.getSccpSettingsConfig()))
                .tcapConfigDto(createTcapConfigDto(sigtranStack.getTcapConfig()))
                .build();
    }

}
