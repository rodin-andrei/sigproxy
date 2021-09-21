package com.unifun.sigproxy.controller.dto.service;

import com.unifun.sigproxy.controller.dto.SigtranStackDto;
import com.unifun.sigproxy.controller.dto.m3ua.M3uaAsConfigDto;
import com.unifun.sigproxy.controller.dto.m3ua.M3uaAspConfigDto;
import com.unifun.sigproxy.controller.dto.sctp.SctpClientAssociationConfigDto;
import com.unifun.sigproxy.controller.dto.sctp.SctpServerAssociationConfigDto;
import com.unifun.sigproxy.controller.dto.sctp.SctpServerConfigDto;
import com.unifun.sigproxy.controller.dto.tcap.TcapConfigDto;
import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.models.config.m3ua.M3uaAsConfig;
import com.unifun.sigproxy.models.config.m3ua.M3uaAspConfig;
import com.unifun.sigproxy.models.config.m3ua.TrafficModeType;
import com.unifun.sigproxy.models.config.sctp.SctpClientAssociationConfig;
import com.unifun.sigproxy.models.config.sctp.SctpServerAssociationConfig;
import com.unifun.sigproxy.models.config.sctp.SctpServerConfig;
import com.unifun.sigproxy.models.config.tcap.TcapConfig;
import lombok.RequiredArgsConstructor;
import org.restcomm.protocols.ss7.m3ua.ExchangeType;
import org.restcomm.protocols.ss7.m3ua.IPSPType;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatorDataAccessObjectService {

    public SctpServerAssociationConfig createSctpServerAssociationConfigDao(SctpServerAssociationConfigDto sctpServerAssociationConfigDto, SctpServerConfig sctpServerConfig){
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

    public M3uaAspConfig createM3uaAspConfigDao(M3uaAspConfigDto m3uaAspConfigDto, SigtranStack sigtranStack){
        M3uaAspConfig m3uaAspConfig = new M3uaAspConfig();
        m3uaAspConfig.setName(m3uaAspConfigDto.getName());
        m3uaAspConfig.setHeartbeat(m3uaAspConfigDto.isHeartbeat());
        m3uaAspConfig.setSctpAssocName(m3uaAspConfigDto.getSctpAssocName());
        m3uaAspConfig.setSigtranStack(sigtranStack);
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
}
