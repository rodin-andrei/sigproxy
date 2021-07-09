package com.unifun.sigproxy.aaaaa;

import com.unifun.sigproxy.models.config.m3ua.AsConfig;
import com.unifun.sigproxy.models.config.m3ua.AspConfig;
import com.unifun.sigproxy.models.config.m3ua.TrafficModeType;
import com.unifun.sigproxy.models.config.sctp.ClientLink;
import com.unifun.sigproxy.models.config.sctp.SctpServer;
import com.unifun.sigproxy.models.config.sctp.ServerLink;
import com.unifun.sigproxy.repository.m3ua.AsRepository;
import com.unifun.sigproxy.repository.m3ua.AspRepository;
import com.unifun.sigproxy.repository.sctp.RemoteSctpLinkRepository;
import com.unifun.sigproxy.repository.sctp.SctpLinkRepository;
import com.unifun.sigproxy.repository.sctp.SctpServerRepository;
import com.unifun.sigproxy.service.SigtranService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.restcomm.protocols.ss7.m3ua.ExchangeType;
import org.restcomm.protocols.ss7.m3ua.Functionality;
import org.restcomm.protocols.ss7.m3ua.IPSPType;
import org.springframework.context.event.ContextRefreshedEvent;
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

    private final SigtranService service;

    @EventListener
    private void test(ContextRefreshedEvent ctxStartEvt) {
        initClient();
        initServer();


        service.init();


    }

    private void initServer() {
        SctpServer sctpServer = new SctpServer();
        sctpServer.setLocalAddress("127.0.0.1");
        sctpServer.setLocalPort(13000);
        sctpServer.setServerName("server2");
        sctpServerRepository.save(sctpServer);


        ServerLink serverLink = new ServerLink();
        serverLink.setLinkName("unifun2");
        serverLink.setRemoteAddress("127.0.0.1");
        serverLink.setRemotePort(12000);
        serverLink.setSctpServer(sctpServer);
        remoteSctpLinkRepository.save(serverLink);


        HashSet<AsConfig> applicationServers = new HashSet<>();
        HashSet<AspConfig> applicationServerPoints = new HashSet<>();

        AspConfig aspConfig = new AspConfig();
        aspConfig.setAspId(1);
        aspConfig.setName("unifun2_asp");
        aspConfig.setSctpAssocName(serverLink.getLinkName());
        aspConfig.setHeartbeat(true);
        aspConfig.setApplicationServers(applicationServers);


        AsConfig asConfig = new AsConfig();
        asConfig.setName("as2");
        asConfig.setFunctionality(Functionality.IPSP);
        asConfig.setExchangeType(ExchangeType.SE);
        asConfig.setIpspType(IPSPType.CLIENT);
        asConfig.setTrafficModeType(TrafficModeType.Loadshare);
        asConfig.setNetworkIndicator(12);
        asConfig.setNetworkAppearance(10);
        asConfig.setRoutingContexts(new long[]{5, 4});
        asConfig.setApplicationServerPoints(applicationServerPoints);
        asConfig = asRepository.save(asConfig);

        aspConfig.getApplicationServers().add(asConfig);
        aspConfig = aspRepository.save(aspConfig);
    }

    private void initClient() {
        ClientLink s = new ClientLink();
        s.setLinkName("unifun1");
        s.setLocalAddress("127.0.0.1");
        s.setLocalPort(12000);
        s.setRemoteAddress("127.0.0.1");
        s.setRemotePort(13000);
        sctpLinkRepository.save(s);


        HashSet<AsConfig> applicationServers = new HashSet<>();
        HashSet<AspConfig> applicationServerPoints = new HashSet<>();

        AspConfig aspConfig = new AspConfig();
        aspConfig.setAspId(1);
        aspConfig.setName("unifun1_asp");
        aspConfig.setSctpAssocName(s.getLinkName());
        aspConfig.setHeartbeat(true);
        aspConfig.setApplicationServers(applicationServers);


        AsConfig asConfig = new AsConfig();
        asConfig.setName("as1");
        asConfig.setFunctionality(Functionality.IPSP);
        asConfig.setExchangeType(ExchangeType.SE);
        asConfig.setIpspType(IPSPType.CLIENT);
        asConfig.setTrafficModeType(TrafficModeType.Loadshare);
        asConfig.setNetworkIndicator(12);
        asConfig.setNetworkAppearance(10);
        asConfig.setRoutingContexts(new long[]{5, 4});
        asConfig.setApplicationServerPoints(applicationServerPoints);
        asConfig = asRepository.save(asConfig);

        aspConfig.getApplicationServers().add(asConfig);
        aspConfig = aspRepository.save(aspConfig);
    }
}
