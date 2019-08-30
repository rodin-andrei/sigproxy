package com.unifun.sigproxy.service.impl;

import com.unifun.sigproxy.exception.InitializingException;
import com.unifun.sigproxy.exception.NoConfigurationException;
import com.unifun.sigproxy.model.LinkConfig;
import com.unifun.sigproxy.model.SctpConfig;
import com.unifun.sigproxy.model.SctpServerConfig;
import com.unifun.sigproxy.repository.SigtranRepository;
import com.unifun.sigproxy.service.SctpService;
import com.unifun.sigproxy.util.GateConstants;
import lombok.Getter;
import org.mobicents.protocols.api.IpChannelType;
import org.mobicents.protocols.sctp.netty.NettySctpManagementImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Set;

@Service
public class SctpServiceImpl implements SctpService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SctpServiceImpl.class);

    @Getter
    private final NettySctpManagementImpl sctpManagement;

    private final SigtranRepository sigtranRepository;

    @Autowired
    public SctpServiceImpl(SigtranRepository sigtranRepository) throws InitializingException {
        this.sigtranRepository = sigtranRepository;
        try {
            LOGGER.info("Initializing SCTP management...");
            sctpManagement = new NettySctpManagementImpl(GateConstants.STACKNAME);
            sctpManagement.start();
            sctpManagement.removeAllResourses();
        } catch (Exception e) {
            LOGGER.error("Can't initialize sctp management. Stopping app. Stacktrace: ", e);
            throw new InitializingException("Can't initialize sctp management.");
        }
    }

    @Override
    @PostConstruct
    public void initialize() throws NoConfigurationException, InitializingException {
        final SctpConfig sctpConfig = sigtranRepository.getSigtranConfig().getSctpConfig();
        Set<LinkConfig> linkConfig = sctpConfig.getLinkConfig();
        Set<SctpServerConfig> sctpServerConfig = sctpConfig.getSctpServerConfig();
        if (linkConfig.isEmpty() && sctpServerConfig.isEmpty()) {
            throw new NoConfigurationException("No links or servers to configure for SCTP.");
        }
        sctpServerConfig.forEach(serverConfig -> {
            try {
                sctpManagement.addServer(
                        serverConfig.getServerName(),
                        serverConfig.getLocalAddress(),
                        serverConfig.getLocalPort(),
                        IpChannelType.getInstance(serverConfig.getLinkType()),
                        serverConfig.getExtraAddresses()
                );
                serverConfig.getRemoteLinkConfig().forEach(remoteLink -> {
                    try {
                        sctpManagement.addServerAssociation(
                                remoteLink.getRemoteAddress(),
                                remoteLink.getRemotePort(),
                                serverConfig.getServerName(),
                                remoteLink.getLinkName(),
                                IpChannelType.getInstance(serverConfig.getLinkType())
                        );
                    } catch (Exception e) {
                        LOGGER.error("Can't create serverAssociation " + remoteLink.getLinkName() + " .", e);
                    }
                });
                sctpManagement.startServer(serverConfig.getServerName());
            } catch (Exception e) {
                LOGGER.error("Can't create server " + serverConfig.getServerName() + " .", e);
            }
        });
        linkConfig.forEach(link -> {
            try {
                sctpManagement.addAssociation(
                        link.getLocalAddress(),
                        link.getLocalPort(),
                        link.getRemoteAddress(),
                        link.getRemotePort(),
                        link.getLinkName(),
                        IpChannelType.getInstance(link.getLinkType()),
                        link.getExtraAddresses()
                );
            } catch (Exception e) {
                LOGGER.error("Can't create link association " + link.getLinkName() + " .", e);
            }
        });
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Get Configured Associations...");
            sctpManagement.getAssociations().forEach((key, value) -> LOGGER.trace("Key: {} Value: {}", key, value));
            LOGGER.trace("Get Configured Servers...");
            sctpManagement.getServers().forEach(server -> LOGGER.trace("ServerName: {}", server.getName()));
        }

    }
}
