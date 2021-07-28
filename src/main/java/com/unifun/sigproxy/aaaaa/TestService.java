package com.unifun.sigproxy.aaaaa;

import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.models.config.m3ua.M3uaAsConfig;
import com.unifun.sigproxy.models.config.m3ua.M3uaAspConfig;
import com.unifun.sigproxy.models.config.m3ua.M3uaRouteConfig;
import com.unifun.sigproxy.models.config.m3ua.TrafficModeType;
import com.unifun.sigproxy.models.config.sctp.SctpClientAssociationConfig;
import com.unifun.sigproxy.models.config.sctp.SctpServer;
import com.unifun.sigproxy.models.config.sctp.SctpServerAssociationConfig;
import com.unifun.sigproxy.repository.SigtranStackRepository;
import com.unifun.sigproxy.repository.m3ua.AsRepository;
import com.unifun.sigproxy.repository.m3ua.AspRepository;
import com.unifun.sigproxy.repository.m3ua.RouteRepository;
import com.unifun.sigproxy.repository.sctp.RemoteSctpLinkRepository;
import com.unifun.sigproxy.repository.sctp.SctpLinkRepository;
import com.unifun.sigproxy.repository.sctp.SctpServerRepository;
import com.unifun.sigproxy.service.SigtranService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.restcomm.protocols.ss7.m3ua.ExchangeType;
import org.restcomm.protocols.ss7.m3ua.Functionality;
import org.restcomm.protocols.ss7.m3ua.IPSPType;
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


        M3uaAsConfig m3uaAsConfig = new M3uaAsConfig();
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
    }
}
