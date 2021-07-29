package com.unifun.sigproxy.aaaaa;

import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.models.config.m3ua.M3uaAsConfig;
import com.unifun.sigproxy.models.config.m3ua.M3uaAspConfig;
import com.unifun.sigproxy.models.config.m3ua.M3uaRouteConfig;
import com.unifun.sigproxy.models.config.m3ua.TrafficModeType;
import com.unifun.sigproxy.models.config.sccp.*;
import com.unifun.sigproxy.models.config.sctp.SctpClientAssociationConfig;
import com.unifun.sigproxy.models.config.sctp.SctpServer;
import com.unifun.sigproxy.models.config.sctp.SctpServerAssociationConfig;
import com.unifun.sigproxy.repository.SigtranStackRepository;
import com.unifun.sigproxy.repository.m3ua.AsRepository;
import com.unifun.sigproxy.repository.m3ua.AspRepository;
import com.unifun.sigproxy.repository.m3ua.RouteRepository;
import com.unifun.sigproxy.repository.sccp.*;
import com.unifun.sigproxy.repository.sctp.RemoteSctpLinkRepository;
import com.unifun.sigproxy.repository.sctp.SctpLinkRepository;
import com.unifun.sigproxy.repository.sctp.SctpServerRepository;
import com.unifun.sigproxy.service.SigtranService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.restcomm.protocols.ss7.indicator.NatureOfAddress;
import org.restcomm.protocols.ss7.indicator.NumberingPlan;
import org.restcomm.protocols.ss7.m3ua.ExchangeType;
import org.restcomm.protocols.ss7.m3ua.Functionality;
import org.restcomm.protocols.ss7.m3ua.IPSPType;
import org.restcomm.protocols.ss7.sccp.LoadSharingAlgorithm;
import org.restcomm.protocols.ss7.sccp.OriginationType;
import org.restcomm.protocols.ss7.sccp.RuleType;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.HashSet;

/**
 * @author arodin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TestService {
    private final SctpLinkRepository sctpLinkRepository;
    private final SctpServerRepository sctpServerRepository;
    private final RemoteSctpLinkRepository remoteSctpLinkRepository;
    private final AsRepository asRepository;
    private final AspRepository aspRepository;
    private final SigtranStackRepository sigtranStackRepository;
    private final RouteRepository routeRepository;
    private final SccpServiceAccessPointConfigRepository sccpServiceAccessPointConfigRepository;
    private final SccpMtp3DestinationConfigRepository sccpMtp3DestinationConfigRepository;
    private final SccpRemoteSignalingPointConfigRepository sccpRemoteSignalingPointConfigRepository;
    private final SccpRemoteSubsystemConfigRepository sccpRemoteSubsystemConfigRepository;
    private final SccpAddressConfigRepository sccpAddressConfigRepository;
    private final SccpRuleConfigRepository sccpRuleConfigRepository;
    private final SccpAddressRuleConfigRepository sccpAddressRuleConfigRepository;


    private final SigtranService service;

    @EventListener
    private void test(ContextStartedEvent ctxStartEvt) {
        initClient();
        initServer();

        service.init();
    }

    private void initServer() {
        SigtranStack sigtranStack = new SigtranStack();
        sigtranStack.setStackName("stack1");
        sigtranStack = sigtranStackRepository.save(sigtranStack);

        SctpServer sctpServer = new SctpServer();
        sctpServer.setLocalAddress("127.0.0.1");
        sctpServer.setLocalPort(13000);
        sctpServer.setName("server1");
        sctpServer.setSigtranStack(sigtranStack);
        sctpServerRepository.save(sctpServer);

        SctpServerAssociationConfig sctpServerAssociationConfig = new SctpServerAssociationConfig();
        sctpServerAssociationConfig.setLinkName("unifun2");
        sctpServerAssociationConfig.setRemoteAddress("127.0.0.1");
        sctpServerAssociationConfig.setRemotePort(12000);
        sctpServerAssociationConfig.setSctpServer(sctpServer);
        remoteSctpLinkRepository.save(sctpServerAssociationConfig);

        HashSet<M3uaAsConfig> applicationServers = new HashSet<>();
        HashSet<M3uaAspConfig> applicationServerPoints = new HashSet<>();

        M3uaAspConfig m3uaAspConfig = new M3uaAspConfig();
        m3uaAspConfig.setName("unifun2_asp");
        m3uaAspConfig.setSctpAssocName(sctpServerAssociationConfig.getLinkName());
        m3uaAspConfig.setHeartbeat(true);
        m3uaAspConfig.setApplicationServers(applicationServers);

        M3uaAsConfig m3uaAsConfig = new M3uaAsConfig();
        m3uaAsConfig.setName("as2");
        m3uaAsConfig.setFunctionality(Functionality.IPSP);
        m3uaAsConfig.setExchangeType(ExchangeType.SE);
        m3uaAsConfig.setIpspType(IPSPType.SERVER);
        m3uaAsConfig.setTrafficModeType(TrafficModeType.Override);
        m3uaAsConfig.setNetworkIndicator(12);
        m3uaAsConfig.setNetworkAppearance(10);
        m3uaAsConfig.setRoutingContexts(new long[]{6});
        m3uaAsConfig.setApplicationServerPoints(applicationServerPoints);
        m3uaAsConfig.setSigtranStack(sigtranStack);
        m3uaAsConfig = asRepository.save(m3uaAsConfig);

        m3uaAspConfig.getApplicationServers().add(m3uaAsConfig);
        m3uaAspConfig.setSigtranStack(sigtranStack);
        m3uaAspConfig = aspRepository.save(m3uaAspConfig);

        M3uaRouteConfig m3uaRouteConfig = new M3uaRouteConfig();
        m3uaRouteConfig.setAs(m3uaAsConfig);
        m3uaRouteConfig.setDpc(100);
        m3uaRouteConfig.setOpc(200);
        m3uaRouteConfig.setTrafficModeType(TrafficModeType.Override);
        m3uaRouteConfig.setSsn(6);
        routeRepository.save(m3uaRouteConfig);

        SccpServiceAccessPointConfig sccpServiceAccessPointConfig = new SccpServiceAccessPointConfig();
        sccpServiceAccessPointConfig.setOpc(200);
        sccpServiceAccessPointConfig.setSigtranStack(sigtranStack);
        sccpServiceAccessPointConfig.setNi(2);
        sccpServiceAccessPointConfigRepository.save(sccpServiceAccessPointConfig);

        SccpMtp3DestinationConfig sccpMtp3DestinationConfig = new SccpMtp3DestinationConfig();
        sccpMtp3DestinationConfig.setSccpServiceAccessPointConfig(sccpServiceAccessPointConfig);
        sccpMtp3DestinationConfig.setFirstSignalingPointCode(100);
        sccpMtp3DestinationConfig.setLastSignalingPointCode(100);
        sccpMtp3DestinationConfig.setFirstSls(0);
        sccpMtp3DestinationConfig.setLastSls(255);
        sccpMtp3DestinationConfig.setSlsMask(255);
        sccpMtp3DestinationConfigRepository.save(sccpMtp3DestinationConfig);

        var sccpRemoteSignalingPointConfig = new SccpRemoteSignalingPointConfig();
        sccpRemoteSignalingPointConfig.setRemoteSignalingPointCode(100);
        sccpRemoteSignalingPointConfig.setSigtranStack(sigtranStack);
        sccpRemoteSignalingPointConfigRepository.save(sccpRemoteSignalingPointConfig);

        SccpRemoteSubsystemConfig sccpRemoteSubsystemConfig = new SccpRemoteSubsystemConfig();
        sccpRemoteSubsystemConfig.setRemoteSignalingPointCode(100);
        sccpRemoteSubsystemConfig.setRemoteSubsystemNumber(147);
        sccpRemoteSubsystemConfig.setMarkProhibitedWhenSpcResuming(false);
        sccpRemoteSubsystemConfig.setSigtranStack(sigtranStack);
        sccpRemoteSubsystemConfigRepository.save(sccpRemoteSubsystemConfig);

        SccpAddressConfig sccpAddressConfig = new SccpAddressConfig();
        sccpAddressConfig.setAddressIndicator((byte) 16);
        sccpAddressConfig.setPointCode(200);
        sccpAddressConfig.setSsn(147);
        sccpAddressConfig.setTranslationType(0);
        sccpAddressConfig.setNumberingPlan(NumberingPlan.valueOf(1));
        sccpAddressConfig.setNatureOfAddress(NatureOfAddress.valueOf(4));
        sccpAddressConfig.setDigits("069744353");
        sccpAddressConfig.setSigtranStack(sigtranStack);
        sccpAddressConfigRepository.save(sccpAddressConfig);


        SccpAddressConfig sccpAddressConfig2 = new SccpAddressConfig();
        sccpAddressConfig2.setAddressIndicator((byte) 16);
        sccpAddressConfig2.setPointCode(100);
        sccpAddressConfig2.setSsn(8);
        sccpAddressConfig2.setTranslationType(0);
        sccpAddressConfig2.setNumberingPlan(NumberingPlan.valueOf(1));
        sccpAddressConfig2.setNatureOfAddress(NatureOfAddress.valueOf(4));
        sccpAddressConfig2.setDigits("069112858");
        sccpAddressConfig2.setSigtranStack(sigtranStack);
        sccpAddressConfigRepository.save(sccpAddressConfig2);


        SccpAddressRuleConfig sccpAddressRuleConfig = new SccpAddressRuleConfig();
        sccpAddressRuleConfig.setAddressIndicator((byte) 16);
        sccpAddressRuleConfig.setPointCode(0);
        sccpAddressRuleConfig.setSsn(0);
        sccpAddressRuleConfig.setTranslationType(0);
        sccpAddressRuleConfig.setNumberingPlan(NumberingPlan.valueOf(1));
        sccpAddressRuleConfig.setNatureOfAddress(NatureOfAddress.valueOf(4));
        sccpAddressRuleConfig.setDigits("069744353");
        sccpAddressRuleConfig = sccpAddressRuleConfigRepository.save(sccpAddressRuleConfig);


        SccpRuleConfig sccpRuleConfig = new SccpRuleConfig();
        sccpRuleConfig.setRuleType(RuleType.SOLITARY);
        sccpRuleConfig.setLoadSharingAlgorithm(LoadSharingAlgorithm.Undefined);
        sccpRuleConfig.setOriginationType(OriginationType.ALL);
        sccpRuleConfig.setMask("K");
        sccpRuleConfig.setSccpAddressRuleConfig(sccpAddressRuleConfig);
        sccpRuleConfig.setPrimaryAddressId(sccpAddressConfig.getId());
        sccpRuleConfig.setSigtranStack(sigtranStack);
        sccpRuleConfig = sccpRuleConfigRepository.save(sccpRuleConfig);


        SccpAddressRuleConfig sccpAddressRuleConfig2 = new SccpAddressRuleConfig();
        sccpAddressRuleConfig2.setAddressIndicator((byte) 16);
        sccpAddressRuleConfig2.setPointCode(0);
        sccpAddressRuleConfig2.setSsn(0);
        sccpAddressRuleConfig2.setTranslationType(0);
        sccpAddressRuleConfig2.setNumberingPlan(NumberingPlan.valueOf(1));
        sccpAddressRuleConfig2.setNatureOfAddress(NatureOfAddress.valueOf(4));
        sccpAddressRuleConfig2.setDigits("069112858");
        sccpAddressRuleConfig2 = sccpAddressRuleConfigRepository.save(sccpAddressRuleConfig2);


        SccpRuleConfig sccpRuleConfig2 = new SccpRuleConfig();
        sccpRuleConfig2.setRuleType(RuleType.SOLITARY);
        sccpRuleConfig2.setLoadSharingAlgorithm(LoadSharingAlgorithm.Undefined);
        sccpRuleConfig2.setOriginationType(OriginationType.ALL);
        sccpRuleConfig2.setMask("K");
        sccpRuleConfig2.setSccpAddressRuleConfig(sccpAddressRuleConfig2);
        sccpRuleConfig2.setPrimaryAddressId(sccpAddressConfig2.getId());
        sccpRuleConfig2.setSigtranStack(sigtranStack);
        sccpRuleConfig2 = sccpRuleConfigRepository.save(sccpRuleConfig2);


    }

    private void initClient() {
        SigtranStack sigtranStack = new SigtranStack();
        sigtranStack.setStackName("stack");
        sigtranStack = sigtranStackRepository.save(sigtranStack);

        SctpClientAssociationConfig s = new SctpClientAssociationConfig();
        s.setLinkName("unifun1");
        s.setLocalAddress("127.0.0.1");
        s.setLocalPort(12000);
        s.setRemoteAddress("127.0.0.1");
        s.setRemotePort(13000);
        s.setSigtranStack(sigtranStack);
        sctpLinkRepository.save(s);


        HashSet<M3uaAsConfig> applicationServers = new HashSet<>();
        HashSet<M3uaAspConfig> applicationServerPoints = new HashSet<>();

        M3uaAspConfig m3uaAspConfig = new M3uaAspConfig();
        m3uaAspConfig.setName("unifun1_asp");
        m3uaAspConfig.setSctpAssocName(s.getLinkName());
        m3uaAspConfig.setHeartbeat(true);
        m3uaAspConfig.setApplicationServers(applicationServers);


        var m3uaAsConfig = new M3uaAsConfig();
        m3uaAsConfig.setName("as1");
        m3uaAsConfig.setFunctionality(Functionality.IPSP);
        m3uaAsConfig.setExchangeType(ExchangeType.SE);
        m3uaAsConfig.setIpspType(IPSPType.CLIENT);
        m3uaAsConfig.setTrafficModeType(TrafficModeType.Override);
        m3uaAsConfig.setNetworkIndicator(12);
        m3uaAsConfig.setNetworkAppearance(10);
        m3uaAsConfig.setRoutingContexts(new long[]{6});
        m3uaAsConfig.setApplicationServerPoints(applicationServerPoints);
        m3uaAsConfig.setSigtranStack(sigtranStack);
        m3uaAsConfig = asRepository.save(m3uaAsConfig);

        m3uaAspConfig.getApplicationServers().add(m3uaAsConfig);
        m3uaAspConfig.setSigtranStack(sigtranStack);
        m3uaAspConfig = aspRepository.save(m3uaAspConfig);

        M3uaRouteConfig m3uaRouteConfig = new M3uaRouteConfig();
        m3uaRouteConfig.setAs(m3uaAsConfig);
        m3uaRouteConfig.setDpc(200);
        m3uaRouteConfig.setOpc(100);
        m3uaRouteConfig.setTrafficModeType(TrafficModeType.Override);
        m3uaRouteConfig.setSsn(6);
        routeRepository.save(m3uaRouteConfig);


        var sccpServiceAccessPointConfig = new SccpServiceAccessPointConfig();
        sccpServiceAccessPointConfig.setOpc(100);
        sccpServiceAccessPointConfig.setSigtranStack(sigtranStack);
        sccpServiceAccessPointConfig.setNi(2);
        sccpServiceAccessPointConfig = sccpServiceAccessPointConfigRepository.save(sccpServiceAccessPointConfig);


        var sccpMtp3DestinationConfig = new SccpMtp3DestinationConfig();
        sccpMtp3DestinationConfig.setSccpServiceAccessPointConfig(sccpServiceAccessPointConfig);
        sccpMtp3DestinationConfig.setFirstSignalingPointCode(200);
        sccpMtp3DestinationConfig.setLastSignalingPointCode(200);
        sccpMtp3DestinationConfig.setFirstSls(0);
        sccpMtp3DestinationConfig.setLastSls(255);
        sccpMtp3DestinationConfig.setSlsMask(255);
        sccpMtp3DestinationConfigRepository.save(sccpMtp3DestinationConfig);


        var sccpRemoteSignalingPointConfig = new SccpRemoteSignalingPointConfig();
        sccpRemoteSignalingPointConfig.setRemoteSignalingPointCode(200);
        sccpRemoteSignalingPointConfig.setSigtranStack(sigtranStack);
        sccpRemoteSignalingPointConfigRepository.save(sccpRemoteSignalingPointConfig);

        SccpRemoteSubsystemConfig sccpRemoteSubsystemConfig = new SccpRemoteSubsystemConfig();
        sccpRemoteSubsystemConfig.setRemoteSignalingPointCode(200);
        sccpRemoteSubsystemConfig.setRemoteSubsystemNumber(8);

        sccpRemoteSubsystemConfig.setMarkProhibitedWhenSpcResuming(false);
        sccpRemoteSubsystemConfig.setSigtranStack(sigtranStack);
        sccpRemoteSubsystemConfigRepository.save(sccpRemoteSubsystemConfig);


        SccpAddressConfig sccpAddressConfig = new SccpAddressConfig();
        sccpAddressConfig.setAddressIndicator((byte) 16);
        sccpAddressConfig.setPointCode(100);
        sccpAddressConfig.setSsn(8);
        sccpAddressConfig.setTranslationType(0);
        sccpAddressConfig.setNumberingPlan(NumberingPlan.valueOf(1));
        sccpAddressConfig.setNatureOfAddress(NatureOfAddress.valueOf(4));
        sccpAddressConfig.setDigits("069112858");
        sccpAddressConfig.setSigtranStack(sigtranStack);
        sccpAddressConfig = sccpAddressConfigRepository.save(sccpAddressConfig);


        SccpAddressConfig sccpAddressConfig2 = new SccpAddressConfig();
        sccpAddressConfig2.setAddressIndicator((byte) 16);
        sccpAddressConfig2.setPointCode(200);
        sccpAddressConfig2.setSsn(147);
        sccpAddressConfig2.setTranslationType(0);
        sccpAddressConfig2.setNumberingPlan(NumberingPlan.valueOf(1));
        sccpAddressConfig2.setNatureOfAddress(NatureOfAddress.valueOf(4));
        sccpAddressConfig2.setDigits("069744353");
        sccpAddressConfig2.setSigtranStack(sigtranStack);
        sccpAddressConfig2 = sccpAddressConfigRepository.save(sccpAddressConfig2);


        SccpAddressRuleConfig sccpAddressRuleConfig = new SccpAddressRuleConfig();
        sccpAddressRuleConfig.setAddressIndicator((byte) 16);
        sccpAddressRuleConfig.setPointCode(0);
        sccpAddressRuleConfig.setSsn(0);
        sccpAddressRuleConfig.setTranslationType(0);
        sccpAddressRuleConfig.setNumberingPlan(NumberingPlan.valueOf(1));
        sccpAddressRuleConfig.setNatureOfAddress(NatureOfAddress.valueOf(4));
        sccpAddressRuleConfig.setDigits("069112858");
        sccpAddressRuleConfig = sccpAddressRuleConfigRepository.save(sccpAddressRuleConfig);


        SccpRuleConfig sccpRuleConfig = new SccpRuleConfig();
        sccpRuleConfig.setRuleType(RuleType.SOLITARY);
        sccpRuleConfig.setLoadSharingAlgorithm(LoadSharingAlgorithm.Undefined);
        sccpRuleConfig.setOriginationType(OriginationType.ALL);
        sccpRuleConfig.setMask("K");
        sccpRuleConfig.setSccpAddressRuleConfig(sccpAddressRuleConfig);
        sccpRuleConfig.setPrimaryAddressId(sccpAddressConfig.getId());
        sccpRuleConfig.setSigtranStack(sigtranStack);
        sccpRuleConfig = sccpRuleConfigRepository.save(sccpRuleConfig);


        SccpAddressRuleConfig sccpAddressRuleConfig2 = new SccpAddressRuleConfig();
        sccpAddressRuleConfig2.setAddressIndicator((byte) 16);
        sccpAddressRuleConfig2.setPointCode(0);
        sccpAddressRuleConfig2.setSsn(0);
        sccpAddressRuleConfig2.setTranslationType(0);
        sccpAddressRuleConfig2.setNumberingPlan(NumberingPlan.valueOf(1));
        sccpAddressRuleConfig2.setNatureOfAddress(NatureOfAddress.valueOf(4));
        sccpAddressRuleConfig2.setDigits("069744353");
        sccpAddressRuleConfig2 = sccpAddressRuleConfigRepository.save(sccpAddressRuleConfig2);


        SccpRuleConfig sccpRuleConfig2 = new SccpRuleConfig();
        sccpRuleConfig2.setRuleType(RuleType.SOLITARY);
        sccpRuleConfig2.setLoadSharingAlgorithm(LoadSharingAlgorithm.Undefined);
        sccpRuleConfig2.setOriginationType(OriginationType.ALL);
        sccpRuleConfig2.setMask("K");
        sccpRuleConfig2.setSccpAddressRuleConfig(sccpAddressRuleConfig2);
        sccpRuleConfig2.setPrimaryAddressId(sccpAddressConfig2.getId());
        sccpRuleConfig2.setSigtranStack(sigtranStack);
        sccpRuleConfig2 = sccpRuleConfigRepository.save(sccpRuleConfig2);

    }
}
