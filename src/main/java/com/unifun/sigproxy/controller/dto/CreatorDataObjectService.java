package com.unifun.sigproxy.controller.dto;

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
import com.unifun.sigproxy.service.sctp.SctpConfigService;
import com.unifun.sigproxy.service.sctp.SctpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreatorDataObjectService {

    private final SctpConfigService sctpConfigService;
    private final SctpService sctpService;


    public SctpClientAssociationConfigDto createSctpClientAssociationConfigDto(SctpClientAssociationConfig clientAssociation) {
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
        SctpServerAssociationConfigDto sctpServerAssociationConfigDto = new SctpServerAssociationConfigDto();
        sctpServerAssociationConfigDto.setId(sctpServerAssociationConfigs.getId());
        sctpServerAssociationConfigDto.setLinkName(sctpServerAssociationConfigs.getLinkName());
        sctpServerAssociationConfigDto.setRemoteAddress(sctpServerAssociationConfigs.getRemoteAddress());
        sctpServerAssociationConfigDto.setRemotePort(sctpServerAssociationConfigs.getRemotePort());
        try {
            sctpServerAssociationConfigDto.setStatus(sctpService.getTransportManagement(sctpServerAssociationConfigs.getSctpServerConfig().getSigtranStack().getStackName())
                    .getAssociation(sctpServerAssociationConfigs.getLinkName()).isConnected());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sctpServerAssociationConfigDto;

    }


    public SctpServerConfigDto createSctpServerConfigDto(SctpServerConfig sctpServerConfig) {
        SctpServerConfigDto sctpServerConfigDto = new SctpServerConfigDto();
        sctpServerConfigDto.setLocalAddress(sctpServerConfig.getLocalAddress());
        sctpServerConfigDto.setLocalPort(sctpServerConfig.getLocalPort());
        sctpServerConfigDto.setMultihomingAddresses(sctpServerConfig.getMultihomingAddresses());
        sctpServerConfigDto.setName(sctpServerConfig.getName());
        Set<SctpServerAssociationConfig> sctpServerAssociationConfigs = sctpConfigService.getServerLinksBySctpServerId(sctpServerConfig.getId());
        sctpServerConfigDto.setSctpServerAssociationConfigs(
                sctpServerAssociationConfigs
                        .stream()
                        .map(this::createSctpServerAssociationConfigDto)
                        .collect(Collectors.toList())
        );
        sctpServerConfigDto.setId(sctpServerConfig.getId());

        return sctpServerConfigDto;
    }

    public SctpStackSettingsConfigDto createSctpStackSettingsConfigDto(SctpStackSettingsConfig sctpStackSettingsConfig) {
        if (sctpStackSettingsConfig == null) return null;
        SctpStackSettingsConfigDto sctpStackSettingsConfigDto = new SctpStackSettingsConfigDto();

        return sctpStackSettingsConfigDto;
    }

    private M3uaAspConfigDto createM3uaAspConfigDto(M3uaAspConfig m3uaAspConfig) {

        M3uaAspConfigDto m3uaAspConfigDto = new M3uaAspConfigDto();
        m3uaAspConfigDto.setId(m3uaAspConfig.getId());
        m3uaAspConfigDto.setName(m3uaAspConfig.getName());
        m3uaAspConfigDto.setSctpAssocName(m3uaAspConfig.getSctpAssocName());
        m3uaAspConfigDto.setHeartbeat(m3uaAspConfig.isHeartbeat());
        return m3uaAspConfigDto;
    }


    private M3uaRouteConfigDto createM3uaRouteConfigDto(M3uaRouteConfig m3uaRouteConfig) {
        M3uaRouteConfigDto m3uaRouteConfigDto = new M3uaRouteConfigDto();
        m3uaRouteConfigDto.setId(m3uaRouteConfig.getId());
        m3uaRouteConfigDto.setOpc(m3uaRouteConfig.getOpc());
        m3uaRouteConfigDto.setDpc(m3uaRouteConfig.getDpc());
        m3uaRouteConfigDto.setSi(m3uaRouteConfig.getSi());
        m3uaRouteConfigDto.setTrafficModeType(m3uaRouteConfig.getTrafficModeType());
        return m3uaRouteConfigDto;
    }

    private M3uaAsConfigDto createM3uaAsConfigDto(M3uaAsConfig m3uaAsConfig) {
        M3uaAsConfigDto m3uaAsConfigDto = new M3uaAsConfigDto();
        m3uaAsConfigDto.setId(m3uaAsConfig.getId());
        m3uaAsConfigDto.setName(m3uaAsConfig.getName());
        m3uaAsConfigDto.setFunctionality(m3uaAsConfig.getFunctionality());
        m3uaAsConfigDto.setExchangeType(m3uaAsConfig.getExchangeType());
        m3uaAsConfigDto.setIpspType(m3uaAsConfig.getIpspType());
        m3uaAsConfigDto.setTrafficModeType(m3uaAsConfig.getTrafficModeType());
        m3uaAsConfigDto.setNetworkIndicator(m3uaAsConfig.getNetworkIndicator());
        m3uaAsConfigDto.setNetworkAppearance(m3uaAsConfig.getNetworkAppearance());
        m3uaAsConfigDto.setRoutingContexts(m3uaAsConfig.getRoutingContexts());
        m3uaAsConfigDto.setApplicationServerPointsDto(
                m3uaAsConfig.getApplicationServerPoints()
                        .stream()
                        .map(this::createM3uaAspConfigDto)
                        .collect(Collectors.toList()));
        m3uaAsConfigDto.setRoutesDto(
                m3uaAsConfig.getRoutes()
                        .stream()
                        .map(this::createM3uaRouteConfigDto)
                        .collect(Collectors.toList()));

        return m3uaAsConfigDto;
    }

    private M3uaStackSettingsConfigDto createM3UaStackSettingsConfigDto(M3uaStackSettingsConfig m3UaStackSettingsConfig) {
        M3uaStackSettingsConfigDto m3uaStackSettingsConfigDto = new M3uaStackSettingsConfigDto();
        if (m3UaStackSettingsConfig != null) {
            m3uaStackSettingsConfigDto.setId(m3uaStackSettingsConfigDto.getId());
            m3uaStackSettingsConfigDto.setDeliveryMessageThreadCount(m3UaStackSettingsConfig.getDeliveryMessageThreadCount());
            m3uaStackSettingsConfigDto.setHeartbeatTime(m3UaStackSettingsConfig.getHeartbeatTime());
            m3uaStackSettingsConfigDto.setMaxAsForRoute(m3UaStackSettingsConfig.getMaxAsForRoute());
            m3uaStackSettingsConfigDto.setMaxSequenceNumber(m3UaStackSettingsConfig.getMaxSequenceNumber());
            m3uaStackSettingsConfigDto.setRoutingKeyManagementEnabled(m3UaStackSettingsConfig.isRoutingKeyManagementEnabled());
            m3uaStackSettingsConfigDto.setRoutingLabelFormat(m3UaStackSettingsConfig.getRoutingLabelFormat());
            m3uaStackSettingsConfigDto.setStatisticsEnabled(m3UaStackSettingsConfig.isStatisticsEnabled());
            m3uaStackSettingsConfigDto.setUseLsbForLinksetSelection(m3UaStackSettingsConfig.isUseLsbForLinksetSelection());
        }
        return m3uaStackSettingsConfigDto;
    }


    private SccpAddressRuleConfigDto createSccpAddressRuleConfigDto(SccpAddressRuleConfig sccpAddressRuleConfig) {
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

    private SccpRuleConfigDto createSccpRuleConfigsDto(SccpRuleConfig sccpRuleConfig) {
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

    private SccpRemoteSignalingPointConfigDto createSccpRemoteSignalingPointConfigsDto(SccpRemoteSignalingPointConfig sccpRemoteSignalingPointConfig) {
        return SccpRemoteSignalingPointConfigDto
                .builder()
                .id(sccpRemoteSignalingPointConfig.getId())
                .remoteSignalingPointCode(sccpRemoteSignalingPointConfig.getRemoteSignalingPointCode())
                .rspcFlag(sccpRemoteSignalingPointConfig.getRspcFlag())
                .mask(sccpRemoteSignalingPointConfig.getMask())
                .build();
    }

    private SccpAddressConfigDto createSccpAddressConfigsDto(SccpAddressConfig sccpAddressConfig) {
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

    private SccpRemoteSubsystemConfigDto createSccpRemoteSubsystemConfigDto(SccpRemoteSubsystemConfig sccpRemoteSubsystemConfig) {
        return SccpRemoteSubsystemConfigDto.builder()
                .id(sccpRemoteSubsystemConfig.getId())
                .remoteSignalingPointCode(sccpRemoteSubsystemConfig.getRemoteSignalingPointCode())
                .remoteSubsystemNumber(sccpRemoteSubsystemConfig.getRemoteSubsystemNumber())
                .remoteSubsystemFlag(sccpRemoteSubsystemConfig.getRemoteSubsystemFlag())
                .markProhibitedWhenSpcResuming(sccpRemoteSubsystemConfig.isMarkProhibitedWhenSpcResuming())
                .build();
    }

    private SccpMtp3DestinationConfigDto createSccpMtp3DestinationConfigDto(SccpMtp3DestinationConfig sccpMtp3DestinationConfig) {
        return SccpMtp3DestinationConfigDto.builder()
                .id(sccpMtp3DestinationConfig.getId())
                .firstSignalingPointCode(sccpMtp3DestinationConfig.getFirstSignalingPointCode())
                .lastSignalingPointCode(sccpMtp3DestinationConfig.getLastSignalingPointCode())
                .firstSls(sccpMtp3DestinationConfig.getFirstSls())
                .lastSls(sccpMtp3DestinationConfig.getLastSls())
                .slsMask(sccpMtp3DestinationConfig.getSlsMask())
                .build();

    }

    private SccpServiceAccessPointConfigDto createSccpServiceAccessPointConfigDto(SccpServiceAccessPointConfig sccpServiceAccessPointConfig) {
        return SccpServiceAccessPointConfigDto.builder()
                .id(sccpServiceAccessPointConfig.getId())
                .mtp3Id(sccpServiceAccessPointConfig.getMtp3Id())
                .opc(sccpServiceAccessPointConfig.getOpc())
                .ni(sccpServiceAccessPointConfig.getNi())
                .networkId(sccpServiceAccessPointConfig.getNetworkId())
                .localGlobalTitleDigits(sccpServiceAccessPointConfig.getLocalGlobalTitleDigits())
                .sccpMtp3DestinationConfigs(
                        sccpServiceAccessPointConfig.getSccpMtp3DestinationConfigs()
                                .stream()
                                .map(this::createSccpMtp3DestinationConfigDto)
                                .collect(Collectors.toSet())
                )
                .build();
    }

    private SccpConcernedSignalingPointCodeConfigDto createSccpConcernedSignalingPointCodeConfigDto(SccpConcernedSignalingPointCodeConfig sccpConcernedSignalingPointCodeConfig) {
        return SccpConcernedSignalingPointCodeConfigDto.builder()
                .id(sccpConcernedSignalingPointCodeConfig.getId())
                .signalingPointCode(sccpConcernedSignalingPointCodeConfig.getSignalingPointCode())
                .build();
    }

    private SccpSettingsConfigDto createSccpSettingsConfigDto(SccpSettingsConfig sccpSettingsConfig) {
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

    private TcapConfigDto createTcapConfigDto(TcapConfig tcapConfig) {
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
                        sigtranStack.getSctpServerConfigs()
                                .stream()
                                .map(this::createSctpServerConfigDto)
                                .collect(Collectors.toSet())
                )
                .associationsDto(
                        sigtranStack.getAssociations()
                                .stream()
                                .map(this::createSctpClientAssociationConfigDto)
                                .collect(Collectors.toSet()))
                .sctpStackSettingsConfigDto(createSctpStackSettingsConfigDto(sigtranStack.getSctpStackSettingsConfig()))
                .applicationServersDto(
                        sigtranStack.getApplicationServers()
                                .stream()
                                .map(this::createM3uaAsConfigDto)
                                .collect(Collectors.toSet())
                )
                .m3UaStackSettingsConfigDto(createM3UaStackSettingsConfigDto(sigtranStack.getM3UaStackSettingsConfig()))
                .sccpRuleConfigsDto(
                        sigtranStack.getSccpRuleConfigs()
                                .stream()
                                .map(this::createSccpRuleConfigsDto)
                                .collect(Collectors.toSet()))
                .sccpRemoteSignalingPointConfigsDto(
                        sigtranStack.getSccpRemoteSignalingPointConfigs()
                                .stream()
                                .map(this::createSccpRemoteSignalingPointConfigsDto)
                                .collect(Collectors.toSet())
                )
                .sccpAddressConfigsDto(
                        sigtranStack.getSccpAddressConfigs()
                                .stream()
                                .map(this::createSccpAddressConfigsDto)
                                .collect(Collectors.toSet())
                )
                .sccpRemoteSubsystemConfigsDto(
                        sigtranStack.getSccpRemoteSubsystemConfigs()
                                .stream()
                                .map(this::createSccpRemoteSubsystemConfigDto)
                                .collect(Collectors.toSet())
                )
                .sccpServiceAccessPointConfigsDto(
                        sigtranStack.getSccpServiceAccessPointConfigs()
                                .stream()
                                .map(this::createSccpServiceAccessPointConfigDto)
                                .collect(Collectors.toSet())
                )
                .sccpConcernedSignalingPointCodeConfigsDto(
                        sigtranStack.getSccpConcernedSignalingPointCodeConfigs()
                                .stream()
                                .map(this::createSccpConcernedSignalingPointCodeConfigDto)
                                .collect(Collectors.toSet())
                )
                .sccpSettingsConfigDto(createSccpSettingsConfigDto(sigtranStack.getSccpSettingsConfig()))
                .tcapConfigDto(createTcapConfigDto(sigtranStack.getTcapConfig()))
                .build();
    }

}
