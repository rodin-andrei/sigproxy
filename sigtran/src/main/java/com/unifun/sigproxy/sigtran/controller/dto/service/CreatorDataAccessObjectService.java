package com.unifun.sigproxy.sigtran.controller.dto.service;

import com.unifun.sigproxy.sigtran.controller.dto.SigtranStackDto;
import com.unifun.sigproxy.sigtran.controller.dto.m3ua.M3uaAsConfigDto;
import com.unifun.sigproxy.sigtran.controller.dto.m3ua.M3uaAspConfigDto;
import com.unifun.sigproxy.sigtran.controller.dto.m3ua.M3uaRouteConfigDto;
import com.unifun.sigproxy.sigtran.controller.dto.m3ua.M3uaStackSettingsConfigDto;
import com.unifun.sigproxy.sigtran.controller.dto.sccp.*;
import com.unifun.sigproxy.sigtran.controller.dto.sctp.SctpClientAssociationConfigDto;
import com.unifun.sigproxy.sigtran.controller.dto.sctp.SctpServerAssociationConfigDto;
import com.unifun.sigproxy.sigtran.controller.dto.sctp.SctpServerConfigDto;
import com.unifun.sigproxy.sigtran.controller.dto.tcap.TcapConfigDto;
import com.unifun.sigproxy.sigtran.models.config.SigtranStack;
import com.unifun.sigproxy.sigtran.models.config.m3ua.M3uaAsConfig;
import com.unifun.sigproxy.sigtran.models.config.m3ua.M3uaAspConfig;
import com.unifun.sigproxy.sigtran.models.config.m3ua.M3uaRouteConfig;
import com.unifun.sigproxy.sigtran.models.config.m3ua.M3uaStackSettingsConfig;
import com.unifun.sigproxy.sigtran.models.config.sccp.*;
import com.unifun.sigproxy.sigtran.models.config.sctp.SctpClientAssociationConfig;
import com.unifun.sigproxy.sigtran.models.config.sctp.SctpServerAssociationConfig;
import com.unifun.sigproxy.sigtran.models.config.sctp.SctpServerConfig;
import com.unifun.sigproxy.sigtran.models.config.tcap.TcapConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreatorDataAccessObjectService {

    public SctpServerAssociationConfig createSctpServerAssociationConfigDao(SctpServerAssociationConfigDto sctpServerAssociationConfigDto, SctpServerConfig sctpServerConfig) {
        SctpServerAssociationConfig sctpServerAssociationConfig = new SctpServerAssociationConfig();
        sctpServerAssociationConfig.setLinkName(sctpServerAssociationConfigDto.getLinkName());
        sctpServerAssociationConfig.setRemotePort(sctpServerAssociationConfigDto.getRemotePort());
        sctpServerAssociationConfig.setRemoteAddress(sctpServerAssociationConfigDto.getRemoteAddress());
        sctpServerAssociationConfig.setSctpServerConfig(sctpServerConfig);
        return sctpServerAssociationConfig;
    }

    public SigtranStack createSigtranStackDao(SigtranStackDto sigtranStackDto) {
        SigtranStack sigtranStack = new SigtranStack();
        sigtranStack.setStackName(sigtranStackDto.getStackName());
        return sigtranStack;
    }

    public TcapConfig createTcapConfigDao(TcapConfigDto tcapConfigDto, SigtranStack sigtranStack) {
        TcapConfig tcapConfig = new TcapConfig();
        tcapConfig.setLocalSsn(tcapConfigDto.getLocalSsn());
        tcapConfig.setAdditionalSsns(tcapConfigDto.getAdditionalSsns());
        tcapConfig.setSigtranStack(sigtranStack);
        return tcapConfig;
    }

    public SctpServerConfig createSctpServerDao(SctpServerConfigDto sctpServerConfigDto, SigtranStack sigtranStack) {
        SctpServerConfig sctpServerConfig = new SctpServerConfig();
        sctpServerConfig.setName(sctpServerConfigDto.getName());
        sctpServerConfig.setLocalAddress(sctpServerConfigDto.getLocalAddress());
        sctpServerConfig.setLocalPort(sctpServerConfigDto.getLocalPort());
        sctpServerConfig.setMultihomingAddresses(sctpServerConfigDto.getMultihomingAddresses());
        sctpServerConfig.setSigtranStack(sigtranStack);
        return sctpServerConfig;
    }

    public SctpClientAssociationConfig createSctpClientAssociationConfigDao(SctpClientAssociationConfigDto sctpClient, SigtranStack sigtranStack) {
        SctpClientAssociationConfig sctpClientAssociationConfig = new SctpClientAssociationConfig();
        sctpClientAssociationConfig.setLinkName(sctpClient.getLinkName());
        sctpClientAssociationConfig.setLocalAddress(sctpClient.getLocalAddress());
        sctpClientAssociationConfig.setLocalPort(sctpClient.getLocalPort());
        sctpClientAssociationConfig.setRemoteAddress(sctpClient.getRemoteAddress());
        sctpClientAssociationConfig.setRemotePort(sctpClient.getRemotePort());
        sctpClientAssociationConfig.setSigtranStack(sigtranStack);
        return sctpClientAssociationConfig;
    }

    public M3uaAspConfig createM3uaAspConfigDao(M3uaAspConfigDto m3uaAspConfigDto, M3uaAsConfig m3uaAsConfig) {
        M3uaAspConfig m3uaAspConfig = new M3uaAspConfig();
        m3uaAspConfig.setName(m3uaAspConfigDto.getName());
        m3uaAspConfig.setHeartbeat(m3uaAspConfigDto.isHeartbeat());
        m3uaAspConfig.setSctpAssocName(m3uaAspConfigDto.getSctpAssocName());
        if (Optional.ofNullable(m3uaAspConfig.getApplicationServers()).isEmpty()) {
            m3uaAspConfig.setApplicationServers(new HashSet<>());
        }
        m3uaAspConfig.getApplicationServers().add(m3uaAsConfig);
        return m3uaAspConfig;


    }

    public M3uaAsConfig createM3uaAsConfigDao(M3uaAsConfigDto m3uaAsConfigDto, SigtranStack sigtranStack) {
        M3uaAsConfig m3uaAsConfig = new M3uaAsConfig();
        m3uaAsConfig.setName(m3uaAsConfigDto.getName());
        m3uaAsConfig.setFunctionality(m3uaAsConfigDto.getFunctionality());
        m3uaAsConfig.setExchangeType(m3uaAsConfigDto.getExchangeType());
        m3uaAsConfig.setIpspType(m3uaAsConfigDto.getIpspType());
        m3uaAsConfig.setTrafficModeType(m3uaAsConfigDto.getTrafficModeType());
        m3uaAsConfig.setNetworkIndicator(m3uaAsConfigDto.getNetworkIndicator());
        m3uaAsConfig.setNetworkAppearance(m3uaAsConfigDto.getNetworkAppearance());
        m3uaAsConfig.setRoutingContexts(m3uaAsConfigDto.getRoutingContexts());
        m3uaAsConfig.setSigtranStack(sigtranStack);
        return m3uaAsConfig;
    }

    public M3uaRouteConfig createM3uaRouteConfigDao(M3uaRouteConfigDto m3uaRouteConfigDto, M3uaAsConfig m3uaAsConfig) {
        M3uaRouteConfig m3uaRouteConfig = new M3uaRouteConfig();
        m3uaRouteConfig.setAs(m3uaAsConfig);
        m3uaRouteConfig.setDpc(m3uaRouteConfigDto.getDpc());
        m3uaRouteConfig.setOpc(m3uaRouteConfigDto.getOpc());
        m3uaRouteConfig.setTrafficModeType(m3uaRouteConfigDto.getTrafficModeType());
        m3uaRouteConfig.setSi(m3uaRouteConfigDto.getSi());
        return m3uaRouteConfig;
    }

    public M3uaStackSettingsConfig createM3uaStackSettingsConfigDao(M3uaStackSettingsConfigDto m3uaStackSettingsConfigDto, SigtranStack sigtranStack) {
        M3uaStackSettingsConfig m3uaStackSettingsConfig = new M3uaStackSettingsConfig();
        m3uaStackSettingsConfig.setId(m3uaStackSettingsConfigDto.getId());
        m3uaStackSettingsConfig.setDeliveryMessageThreadCount(m3uaStackSettingsConfigDto.getDeliveryMessageThreadCount());
        m3uaStackSettingsConfig.setHeartbeatTime(m3uaStackSettingsConfigDto.getHeartbeatTime());
        m3uaStackSettingsConfig.setMaxAsForRoute(m3uaStackSettingsConfigDto.getMaxAsForRoute());
        m3uaStackSettingsConfig.setMaxSequenceNumber(m3uaStackSettingsConfigDto.getMaxSequenceNumber());
        m3uaStackSettingsConfig.setRoutingKeyManagementEnabled(m3uaStackSettingsConfigDto.isRoutingKeyManagementEnabled());
        m3uaStackSettingsConfig.setRoutingLabelFormat(m3uaStackSettingsConfigDto.getRoutingLabelFormat());
        m3uaStackSettingsConfig.setStatisticsEnabled(m3uaStackSettingsConfigDto.isStatisticsEnabled());
        m3uaStackSettingsConfig.setUseLsbForLinksetSelection(m3uaStackSettingsConfigDto.isUseLsbForLinksetSelection());
        m3uaStackSettingsConfig.setSigtranStack(sigtranStack);
        return m3uaStackSettingsConfig;
    }


    public SccpAddressConfig createSccpAddressConfigDao(SccpAddressConfigDto sccpAddressConfigDto, SigtranStack sigtranStack) {
        SccpAddressConfig sccpAddressConfig = new SccpAddressConfig();
        sccpAddressConfig.setAddressIndicator(sccpAddressConfigDto.getAddressIndicator());
        sccpAddressConfig.setPointCode(sccpAddressConfigDto.getPointCode());
        sccpAddressConfig.setSsn(sccpAddressConfigDto.getSsn());
        sccpAddressConfig.setTranslationType(sccpAddressConfigDto.getTranslationType());
        sccpAddressConfig.setNumberingPlan(sccpAddressConfigDto.getNumberingPlan());
        sccpAddressConfig.setNatureOfAddress(sccpAddressConfigDto.getNatureOfAddress());
        sccpAddressConfig.setDigits(sccpAddressConfigDto.getDigits());
        sccpAddressConfig.setSigtranStack(sigtranStack);
        return sccpAddressConfig;
    }

    public SccpAddressRuleConfig createSccpAddressRuleConfigDao(SccpAddressRuleConfigDto sccpAddressRuleConfigDto) {
        SccpAddressRuleConfig sccpAddressRuleConfig = new SccpAddressRuleConfig();
        sccpAddressRuleConfig.setAddressIndicator(sccpAddressRuleConfigDto.getAddressIndicator());
        sccpAddressRuleConfig.setPointCode(sccpAddressRuleConfigDto.getPointCode());
        sccpAddressRuleConfig.setSsn(sccpAddressRuleConfigDto.getSsn());
        sccpAddressRuleConfig.setTranslationType(sccpAddressRuleConfigDto.getTranslationType());
        sccpAddressRuleConfig.setNumberingPlan(sccpAddressRuleConfigDto.getNumberingPlan());
        sccpAddressRuleConfig.setNatureOfAddress(sccpAddressRuleConfigDto.getNatureOfAddress());
        sccpAddressRuleConfig.setDigits(sccpAddressRuleConfigDto.getDigits());
        return sccpAddressRuleConfig;
    }

    public SccpConcernedSignalingPointCodeConfig createSccpConcernedSignalingPointCodeConfigDao(SccpConcernedSignalingPointCodeConfigDto sccpConcernedSignalingPointCodeConfigDto, SigtranStack sigtranStack) {
        SccpConcernedSignalingPointCodeConfig sccpConcernedSignalingPointCodeConfig = new SccpConcernedSignalingPointCodeConfig();
        sccpConcernedSignalingPointCodeConfig.setSignalingPointCode(sccpConcernedSignalingPointCodeConfigDto.getSignalingPointCode());
        sccpConcernedSignalingPointCodeConfig.setSigtranStack(sigtranStack);
        return sccpConcernedSignalingPointCodeConfig;

    }

    public SccpLongMessageRuleConfig createSccpLongMessageRuleConfigDao(SccpLongMessageRuleConfigDto sccpLongMessageRuleConfigDto, SigtranStack sigtranStack) {
        SccpLongMessageRuleConfig sccpLongMessageRuleConfig = new SccpLongMessageRuleConfig();
        sccpLongMessageRuleConfig.setFirstSignalingPointCode(sccpLongMessageRuleConfigDto.getFirstSignalingPointCode());
        sccpLongMessageRuleConfig.setLastSignalingPointCode(sccpLongMessageRuleConfigDto.getLastSignalingPointCode());
        sccpLongMessageRuleConfig.setLongMessageRuleType(sccpLongMessageRuleConfigDto.getLongMessageRuleType());
        sccpLongMessageRuleConfig.setSigtranStack(sigtranStack);
        return sccpLongMessageRuleConfig;
    }

    public SccpMtp3DestinationConfig createSccpMtp3DestinationConfigDao(SccpMtp3DestinationConfigDto sccpMtp3DestinationConfigDto, SccpServiceAccessPointConfig sccpServiceAccessPointConfig) {
        SccpMtp3DestinationConfig sccpMtp3DestinationConfig = new SccpMtp3DestinationConfig();
        sccpMtp3DestinationConfig.setSccpServiceAccessPointConfig(sccpServiceAccessPointConfig);
        sccpMtp3DestinationConfig.setFirstSignalingPointCode(sccpMtp3DestinationConfigDto.getFirstSignalingPointCode());
        sccpMtp3DestinationConfig.setLastSignalingPointCode(sccpMtp3DestinationConfigDto.getLastSignalingPointCode());
        sccpMtp3DestinationConfig.setFirstSls(sccpMtp3DestinationConfigDto.getFirstSls());
        sccpMtp3DestinationConfig.setLastSls(sccpMtp3DestinationConfigDto.getLastSls());
        sccpMtp3DestinationConfig.setSlsMask(sccpMtp3DestinationConfigDto.getSlsMask());
        return sccpMtp3DestinationConfig;
    }

    public SccpRemoteSignalingPointConfig createSccpRemoteSignalingPointConfigDao(SccpRemoteSignalingPointConfigDto sccpRemoteSignalingPointConfigDto, SigtranStack sigtranStack) {
        SccpRemoteSignalingPointConfig sccpRemoteSignalingPointConfig = new SccpRemoteSignalingPointConfig();
        sccpRemoteSignalingPointConfig.setRemoteSignalingPointCode(sccpRemoteSignalingPointConfigDto.getRemoteSignalingPointCode());
        sccpRemoteSignalingPointConfig.setMask(sccpRemoteSignalingPointConfigDto.getMask());
        sccpRemoteSignalingPointConfig.setRspcFlag(sccpRemoteSignalingPointConfigDto.getRspcFlag());
        sccpRemoteSignalingPointConfig.setSigtranStack(sigtranStack);
        return sccpRemoteSignalingPointConfig;
    }

    public SccpRemoteSubsystemConfig createSccpRemoteSubsystemConfigDao(SccpRemoteSubsystemConfigDto sccpRemoteSubsystemConfigDto, SigtranStack sigtranStack) {
        SccpRemoteSubsystemConfig sccpRemoteSubsystemConfig = new SccpRemoteSubsystemConfig();
        sccpRemoteSubsystemConfig.setRemoteSignalingPointCode(sccpRemoteSubsystemConfigDto.getRemoteSignalingPointCode());
        sccpRemoteSubsystemConfig.setRemoteSubsystemNumber(sccpRemoteSubsystemConfigDto.getRemoteSubsystemNumber());
        sccpRemoteSubsystemConfig.setMarkProhibitedWhenSpcResuming(sccpRemoteSubsystemConfigDto.isMarkProhibitedWhenSpcResuming());
        sccpRemoteSubsystemConfig.setRemoteSubsystemFlag(sccpRemoteSubsystemConfigDto.getRemoteSubsystemFlag());
        sccpRemoteSubsystemConfig.setSigtranStack(sigtranStack);
        return sccpRemoteSubsystemConfig;
    }

    public SccpRuleConfig createSccpRuleConfigDao(SccpRuleConfigDto sccpRuleConfigDto, SigtranStack sigtranStack, SccpAddressRuleConfig sccpAddressRuleConfig, SccpAddressRuleConfig callingSccpAddressRuleConfigId) {
        SccpRuleConfig sccpRuleConfig = new SccpRuleConfig();
        sccpRuleConfig.setMask(sccpRuleConfigDto.getMask());
        sccpRuleConfig.setSccpAddressRuleConfig(sccpAddressRuleConfig);
        sccpRuleConfig.setRuleType(sccpRuleConfigDto.getRuleType());
        sccpRuleConfig.setPrimaryAddressId(sccpRuleConfigDto.getPrimaryAddressId());
        sccpRuleConfig.setLoadSharingAlgorithm(sccpRuleConfigDto.getLoadSharingAlgorithm());
        sccpRuleConfig.setOriginationType(sccpRuleConfigDto.getOriginationType());
        sccpRuleConfig.setSecondaryAddressId(sccpRuleConfigDto.getSecondaryAddressId());
        sccpRuleConfig.setNewCallingPartyAddressAddressId(sccpRuleConfigDto.getNewCallingPartyAddressAddressId());
        sccpRuleConfig.setNetworkId(sccpRuleConfigDto.getNetworkId());
        sccpRuleConfig.setCallingSccpAddressRuleConfig(callingSccpAddressRuleConfigId);
        sccpRuleConfig.setSigtranStack(sigtranStack);
        return sccpRuleConfig;
    }

    public SccpServiceAccessPointConfig createSccpServiceAccessPointConfigDao(SccpServiceAccessPointConfigDto sccpServiceAccessPointConfigDto, SigtranStack sigtranStack) {
        SccpServiceAccessPointConfig sccpServiceAccessPointConfig = new SccpServiceAccessPointConfig();
        sccpServiceAccessPointConfig.setMtp3Id(sccpServiceAccessPointConfigDto.getMtp3Id());
        sccpServiceAccessPointConfig.setOpc(sccpServiceAccessPointConfigDto.getOpc());
        sccpServiceAccessPointConfig.setNi(sccpServiceAccessPointConfigDto.getNi());
        sccpServiceAccessPointConfig.setNetworkId(sccpServiceAccessPointConfigDto.getNetworkId());
        sccpServiceAccessPointConfig.setLocalGlobalTitleDigits(sccpServiceAccessPointConfigDto.getLocalGlobalTitleDigits());
        sccpServiceAccessPointConfig.setSigtranStack(sigtranStack);
        return sccpServiceAccessPointConfig;
    }

    public SccpSettingsConfig createSccpSettingsConfigDao(SccpSettingsConfigDto sccpSettingsConfigDto, SigtranStack sigtranStack) {
        SccpSettingsConfig sccpSettingsConfig = new SccpSettingsConfig();
        sccpSettingsConfig.setZmarginxudtmessage(sccpSettingsConfigDto.getZmarginxudtmessage());
        sccpSettingsConfig.setReassemblytimerdelay(sccpSettingsConfigDto.getReassemblytimerdelay());
        sccpSettingsConfig.setMaxdatamessage(sccpSettingsConfigDto.getMaxdatamessage());
        sccpSettingsConfig.setPeriodoflogging(sccpSettingsConfigDto.getPeriodoflogging());
        sccpSettingsConfig.setRemovespc(sccpSettingsConfigDto.isRemovespc());
        sccpSettingsConfig.setPreviewmode(sccpSettingsConfigDto.isPreviewmode());
        sccpSettingsConfig.setSsttimerduration_min(sccpSettingsConfigDto.getSsttimerduration_min());
        sccpSettingsConfig.setSsttimerduration_max(sccpSettingsConfigDto.getSsttimerduration_max());
        sccpSettingsConfig.setSsttimerduration_increasefactor(sccpSettingsConfigDto.getSsttimerduration_increasefactor());
        sccpSettingsConfig.setSccpprotocolversion(sccpSettingsConfigDto.getSccpprotocolversion());
        sccpSettingsConfig.setCc_timer_a(sccpSettingsConfigDto.getCc_timer_a());
        sccpSettingsConfig.setCc_timer_d(sccpSettingsConfigDto.getCc_timer_d());
        sccpSettingsConfig.setCanrelay(sccpSettingsConfigDto.isCanrelay());
        sccpSettingsConfig.setConnesttimerdelay(sccpSettingsConfigDto.getConnesttimerdelay());
        sccpSettingsConfig.setIastimerdelay(sccpSettingsConfigDto.getIastimerdelay());
        sccpSettingsConfig.setIartimerdelay(sccpSettingsConfigDto.getIartimerdelay());
        sccpSettingsConfig.setReltimerdelay(sccpSettingsConfigDto.getReltimerdelay());
        sccpSettingsConfig.setRepeatreltimerdelay(sccpSettingsConfigDto.getRepeatreltimerdelay());
        sccpSettingsConfig.setInttimerdelay(sccpSettingsConfigDto.getInttimerdelay());
        sccpSettingsConfig.setGuardtimerdelay(sccpSettingsConfigDto.getGuardtimerdelay());
        sccpSettingsConfig.setResettimerdelay(sccpSettingsConfigDto.getResettimerdelay());
        sccpSettingsConfig.setTimerexecutors_threadcount(sccpSettingsConfigDto.getTimerexecutors_threadcount());
        sccpSettingsConfig.setCc_algo(sccpSettingsConfigDto.getCc_algo());
        sccpSettingsConfig.setCc_blockingoutgoungsccpmessages(sccpSettingsConfigDto.isCc_blockingoutgoungsccpmessages());
        sccpSettingsConfig.setSigtranStack(sigtranStack);
        return sccpSettingsConfig;
    }
}
