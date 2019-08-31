package com.unifun.sigproxy.service.impl;

import com.unifun.sigproxy.exception.InitializingException;
import com.unifun.sigproxy.exception.NoConfigurationException;
import com.unifun.sigproxy.model.*;
import com.unifun.sigproxy.repository.SigtranRepository;
import com.unifun.sigproxy.service.SctpConfigService;
import com.unifun.sigproxy.service.SctpService;
import com.unifun.sigproxy.util.GateConstants;
import lombok.Getter;
import org.mobicents.protocols.api.Association;
import org.mobicents.protocols.api.AssociationType;
import org.mobicents.protocols.api.IpChannelType;
import org.mobicents.protocols.sctp.netty.NettySctpManagementImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.testng.collections.Lists;

import javax.annotation.PostConstruct;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SctpServiceImpl implements SctpService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SctpServiceImpl.class);

    @Getter
    private final NettySctpManagementImpl sctpManagement;

    private final SigtranRepository sigtranRepository;
    private final SctpConfigService sctpConfigService;

    @Autowired
    public SctpServiceImpl(SigtranRepository sigtranRepository, SctpConfigService sctpConfigService) throws InitializingException {
        this.sigtranRepository = sigtranRepository;
        this.sctpConfigService = sctpConfigService;
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
        final SctpConfig sctpConfig = sctpConfigService.getSctpConfiguration();
        Set<LinkConfig> linkConfig = sctpConfig.getLinkConfig();
        Set<SctpServerConfig> sctpServerConfig = sctpConfig.getSctpServerConfig();
        if (linkConfig.isEmpty() && sctpServerConfig.isEmpty()) {
            throw new NoConfigurationException("No links or servers to configure for SCTP.");
        }
        sctpServerConfig.forEach(this::updateSctpServer);
        linkConfig.forEach(this::updateSctpLink);

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Get Configured Associations...");
            sctpManagement.getAssociations().forEach((key, value) -> LOGGER.trace("Key: {} Value: {}", key, value));
            LOGGER.trace("Get Configured Servers...");
            sctpManagement.getServers().forEach(server -> LOGGER.trace("ServerName: {}", server.getName()));
        }

    }

    @Override
    public void updateSctpLink(LinkConfig link) {
        try {
            removeAssociation(link);
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
    }

    @Override
    public void updateSctpServer(SctpServerConfig serverConfig) {
        final String serverName = serverConfig.getServerName();
        try {
            removeServer(serverConfig);
            sctpManagement.addServer(
                    serverName,
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
                            serverName,
                            remoteLink.getLinkName(),
                            IpChannelType.getInstance(serverConfig.getLinkType())
                    );
                } catch (Exception e) {
                    LOGGER.error("Can't create serverAssociation " + remoteLink.getLinkName() + " .", e);
                }
            });
            startServer(serverName);
        } catch (Exception e) {
            LOGGER.error("Can't create server " + serverName + " .", e);
        }
    }

    @Override
    public Set<SctpLinkDto> getLinkStatus() {
        return sctpManagement.getAssociations()
                .values()
                .stream()
                .filter(assoc -> assoc.getAssociationType().equals(AssociationType.CLIENT))
                .map(assoc -> {
                    SctpLinkDto sctpLinkDto = new SctpLinkDto();
                    sctpLinkDto.setLinkName(assoc.getName());
                    sctpLinkDto.setLocalAddress(assoc.getHostAddress());
                    sctpLinkDto.setLocalPort(assoc.getHostPort());
                    sctpLinkDto.setRemoteAddress(assoc.getPeerAddress());
                    sctpLinkDto.setRemotePort(assoc.getPeerPort());
                    sctpLinkDto.setExtraAddresses(assoc.getExtraHostAddresses());
                    sctpLinkDto.setStatus(assoc.isUp() ? "UP" : "DOWN");
                    return sctpLinkDto;
                })
                .collect(Collectors.toSet());
    }

    @Override
    public Set<SctpServerDto> getServerLinkStatuses() {
        //TODO: 30-08-2019 Check if we have alternative of working with this type of lists
        return Lists.newArrayList(sctpManagement.getServers())
                .stream()
                .map(server -> {
                    SctpServerDto sctpServerDto = new SctpServerDto();
                    sctpServerDto.setServerName(server.getName());
                    sctpServerDto.setLocalAddress(server.getHostAddress());
                    sctpServerDto.setLocalPort(server.getHostport());
                    sctpServerDto.setExtraAddresses(server.getExtraHostAddresses());

                    sctpServerDto.setServerLinks(
                            Lists.newArrayList(server.getAssociations())
                                    .stream()
                                    .map(assocName -> {
                                        try {
                                            final Association association = sctpManagement.getAssociation(assocName);
                                            SctpServerLinkDto linkDto = new SctpServerLinkDto();
                                            linkDto.setLinkName(association.getName());
                                            linkDto.setRemoteAddress(association.getPeerAddress());
                                            linkDto.setRemotePort(association.getPeerPort());
                                            linkDto.setStatus(association.isUp() ? "UP" : "DOWN");
                                            return linkDto;
                                        } catch (Exception e) {
                                            LOGGER.warn("Can't found link {} for server {}.", assocName, server.getName());
                                            return null;
                                        }
                                    })
                                    .collect(Collectors.toSet())
                    );
                    return sctpServerDto;
                })
                .collect(Collectors.toSet());
    }

    @Override
    public void startLink(String linkName) {
        try {
            sctpManagement.startAssociation(linkName);
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
        }
    }

    @Override
    public void stopLink(String linkName) {
        try {
            sctpManagement.stopAssociation(linkName);
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
        }
    }

    @Override
    public void startServer(String serverName) {
        try {
            sctpManagement.startServer(serverName);
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
        }
    }

    @Override
    public void stopServer(String serverName) {
        try {
            sctpManagement.stopServer(serverName);
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
        }
    }

    private void removeAssociation(LinkConfig link) {
        try {
            sctpManagement.stopAssociation(link.getLinkName());
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
        try {
            sctpManagement.removeAssociation(link.getLinkName());
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
    }

    private void removeServer(SctpServerConfig serverConfig) {
        try {
            sctpManagement.stopServer(serverConfig.getServerName());
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
        try {
            sctpManagement.removeServer(serverConfig.getServerName());
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
    }
}
