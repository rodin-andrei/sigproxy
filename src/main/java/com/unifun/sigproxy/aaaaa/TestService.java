package com.unifun.sigproxy.aaaaa;

import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.models.config.m3ua.AsConfig;
import com.unifun.sigproxy.models.config.m3ua.AspConfig;
import com.unifun.sigproxy.models.config.m3ua.RouteConfig;
import com.unifun.sigproxy.models.config.m3ua.TrafficModeType;
import com.unifun.sigproxy.models.config.sctp.ClientAssociation;
import com.unifun.sigproxy.models.config.sctp.SctpServer;
import com.unifun.sigproxy.models.config.sctp.ServerAssociation;
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

        ServerAssociation serverAssociation = new ServerAssociation();
        serverAssociation.setLinkName("unifun2");
        serverAssociation.setRemoteAddress("127.0.0.1");
        serverAssociation.setRemotePort(12000);
        serverAssociation.setSctpServer(sctpServer);
        remoteSctpLinkRepository.save(serverAssociation);

        HashSet<AsConfig> applicationServers = new HashSet<>();
        HashSet<AspConfig> applicationServerPoints = new HashSet<>();

        AspConfig aspConfig = new AspConfig();
        aspConfig.setName("unifun2_asp");
        aspConfig.setSctpAssocName(serverAssociation.getLinkName());
        aspConfig.setHeartbeat(true);
        aspConfig.setApplicationServers(applicationServers);

        AsConfig asConfig = new AsConfig();
        asConfig.setName("as2");
        asConfig.setFunctionality(Functionality.IPSP);
        asConfig.setExchangeType(ExchangeType.SE);
        asConfig.setIpspType(IPSPType.SERVER);
        asConfig.setTrafficModeType(TrafficModeType.Override);
        asConfig.setNetworkIndicator(12);
        asConfig.setNetworkAppearance(10);
        asConfig.setRoutingContexts(new long[]{6});
        asConfig.setApplicationServerPoints(applicationServerPoints);
        asConfig.setSigtranStack(sigtranStack);
        asConfig = asRepository.save(asConfig);

        aspConfig.getApplicationServers().add(asConfig);
        aspConfig.setSigtranStack(sigtranStack);
        aspConfig = aspRepository.save(aspConfig);

        RouteConfig routeConfig = new RouteConfig();
        routeConfig.setAs(asConfig);
        routeConfig.setDpc(100);
        routeConfig.setOpc(200);
        routeConfig.setTrafficModeType(TrafficModeType.Override);
        routeConfig.setSsn(6);
        routeRepository.save(routeConfig);
    }

    private void initClient() {
        SigtranStack sigtranStack = new SigtranStack();
        sigtranStack.setStackName("stack");
        sigtranStack = sigtranStackRepository.save(sigtranStack);

        ClientAssociation s = new ClientAssociation();
        s.setLinkName("unifun1");
        s.setLocalAddress("127.0.0.1");
        s.setLocalPort(12000);
        s.setRemoteAddress("127.0.0.1");
        s.setRemotePort(13000);
        s.setSigtranStack(sigtranStack);
        sctpLinkRepository.save(s);


        HashSet<AsConfig> applicationServers = new HashSet<>();
        HashSet<AspConfig> applicationServerPoints = new HashSet<>();

        AspConfig aspConfig = new AspConfig();
        aspConfig.setName("unifun1_asp");
        aspConfig.setSctpAssocName(s.getLinkName());
        aspConfig.setHeartbeat(true);
        aspConfig.setApplicationServers(applicationServers);


        AsConfig asConfig = new AsConfig();
        asConfig.setName("as1");
        asConfig.setFunctionality(Functionality.IPSP);
        asConfig.setExchangeType(ExchangeType.SE);
        asConfig.setIpspType(IPSPType.CLIENT);
        asConfig.setTrafficModeType(TrafficModeType.Override);
        asConfig.setNetworkIndicator(12);
        asConfig.setNetworkAppearance(10);
        asConfig.setRoutingContexts(new long[]{6});
        asConfig.setApplicationServerPoints(applicationServerPoints);
        asConfig.setSigtranStack(sigtranStack);
        asConfig = asRepository.save(asConfig);

        aspConfig.getApplicationServers().add(asConfig);
        aspConfig.setSigtranStack(sigtranStack);
        aspConfig = aspRepository.save(aspConfig);

        RouteConfig routeConfig = new RouteConfig();
        routeConfig.setAs(asConfig);
        routeConfig.setDpc(200);
        routeConfig.setOpc(100);
        routeConfig.setTrafficModeType(TrafficModeType.Override);
        routeConfig.setSsn(6);
        routeRepository.save(routeConfig);
    }
}
