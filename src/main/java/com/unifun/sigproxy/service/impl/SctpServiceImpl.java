package com.unifun.sigproxy.service.impl;

import com.unifun.sigproxy.dto.SctpLinkDto;
import com.unifun.sigproxy.dto.SctpServerDto;
import com.unifun.sigproxy.dto.SctpServerLinkDto;
import com.unifun.sigproxy.exception.InitializingException;
import com.unifun.sigproxy.exception.NoConfigurationException;
import com.unifun.sigproxy.models.config.sctp.ClientAssociation;
import com.unifun.sigproxy.models.config.sctp.SctpServer;
import com.unifun.sigproxy.repository.sctp.RemoteSctpLinkRepository;
import com.unifun.sigproxy.repository.sctp.SctpLinkRepository;
import com.unifun.sigproxy.repository.sctp.SctpServerRepository;
import com.unifun.sigproxy.service.SctpService;
import com.unifun.sigproxy.util.GateConstants;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mobicents.protocols.api.Association;
import org.mobicents.protocols.api.AssociationType;
import org.mobicents.protocols.api.IpChannelType;
import org.mobicents.protocols.api.Management;
import org.mobicents.protocols.sctp.netty.NettySctpManagementImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.testng.collections.Lists;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SctpServiceImpl implements SctpService {
    @Value("${jss.persist.dir}")
    private String jssPersistDir;

    private final SctpLinkRepository sctpLinkRepository;
    private final SctpServerRepository sctpServerRepository;
    private final RemoteSctpLinkRepository remoteSctpLinkRepository;
    @Getter
    private Management sctpManagement;

    @Override
    public void initialize() throws NoConfigurationException, InitializingException {
        try {
            log.info("Initializing SCTP management...");
            sctpManagement = new NettySctpManagementImpl(GateConstants.STACKNAME + "_sctp");
            sctpManagement.setPersistDir(this.jssPersistDir);
            sctpManagement.start();
            sctpManagement.removeAllResourses();
        } catch (Exception e) {
            throw new InitializingException("Can't initialize sctp management.", e);
        }

        List<ClientAssociation> clientAssociation = sctpLinkRepository.findAll();
        List<SctpServer> sctpServer = sctpServerRepository.findAll();
        if (clientAssociation.isEmpty() && sctpServer.isEmpty()) {
            throw new NoConfigurationException("No links or servers to configure for SCTP.");
        }
        sctpServer.forEach(this::addServer);
        clientAssociation.forEach(this::addLink);

        if (log.isTraceEnabled()) {
            log.trace("Get Configured Associations...");
            sctpManagement.getAssociations().forEach((key, value) -> log.trace("Key: {} Value: {}", key, value));
            log.trace("Get Configured Servers...");
            sctpManagement.getServers().forEach(server -> log.trace("ServerName: {}", server.getName()));
        }
    }

    @Override
    public void stop() {
        try {
            sctpManagement.stop();
        } catch (Exception e) {
            log.error("Can't stop SCTP management: ", e);
        }
    }

    @Override
    public Management getTransportManagement() {
        return this.sctpManagement;
    }

    @Override
    public void addLink(ClientAssociation link) {
        //TODO add check already exist assoc
        try {
            Association association = sctpManagement.addAssociation(
                    link.getLocalAddress(),
                    link.getLocalPort(),
                    link.getRemoteAddress(),
                    link.getRemotePort(),
                    link.getLinkName(),
                    IpChannelType.TCP,
                    link.getMultihomingAddresses()
            );
        } catch (Exception e) {
            log.error("Can't create link association " + link.getLinkName() + " . ", e);
        }
    }

    @Override
    public void addLinks(Set<ClientAssociation> newLinks) {
        newLinks.forEach(this::addLink);
    }

    @Override
    public void addServer(SctpServer serverConfig) {
        try {
            sctpManagement.addServer(
                    serverConfig.getServerName(),
                    serverConfig.getLocalAddress(),
                    serverConfig.getLocalPort(),
                    IpChannelType.TCP,
                    serverConfig.getMultihomingAddresses()
            );
            serverConfig.getServerAssociations().forEach(remoteLink -> {
                try {
                    sctpManagement.addServerAssociation(
                            remoteLink.getRemoteAddress(),
                            remoteLink.getRemotePort(),
                            serverConfig.getServerName(),
                            remoteLink.getLinkName(),
                            IpChannelType.TCP);
                } catch (Exception e) {
                    log.error("Can't create serverAssociation " + remoteLink.getLinkName() + " .", e);
                }
            });
            startServer(serverConfig.getServerName());

        } catch (Exception e) {
            log.error("Can't create server {}. {}", serverConfig.getServerName(), e.getMessage(), e);
        }
    }

    @Override
    public void addServers(Set<SctpServer> newServers) {
        newServers.forEach(this::addServer);
    }


    @Override
    public void removeAllLinks() {
        sctpManagement.getAssociations().values()
                .forEach(assoc -> {
                    try {
                        sctpManagement.stopAssociation(assoc.getName());
                    } catch (Exception e) {
                        log.warn("Can't stop association: {}. {}", assoc.getName(), e.getMessage(), e);
                    }
                    try {
                        sctpManagement.removeAssociation(assoc.getName());
                    } catch (Exception e) {
                        log.warn("Can't remove association: {}. {}", assoc.getName(), e.getMessage(), e);
                    }
                });
    }

    @Override
    public Set<SctpLinkDto> getLinkStatuses() {
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
                    sctpLinkDto.setUp(assoc.isUp());
                    sctpLinkDto.setStarted(assoc.isStarted());
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
                                            linkDto.setUp(association.isUp());
                                            linkDto.setStarted(association.isStarted());
                                            return linkDto;
                                        } catch (Exception e) {
                                            log.warn("Can't found link {} for server {}.",
                                                    assocName, server.getName(), e);
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
            log.warn(e.getMessage(), e);
        }
    }

    @Override
    public void stopLink(String linkName) {
        try {
            sctpManagement.stopAssociation(linkName);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }

    @Override
    public void startServer(String serverName) {
        try {
            sctpManagement.startServer(serverName);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }

    @Override
    public void stopServer(String serverName) {
        try {
            sctpManagement.stopServer(serverName);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }

    private void removeSctpLink(ClientAssociation link) {
        try {
            sctpManagement.stopAssociation(link.getLinkName());
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        try {
            sctpManagement.removeAssociation(link.getLinkName());
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }

    private void removeServer(SctpServer serverConfig) {
        try {
            sctpManagement.stopServer(serverConfig.getServerName());
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        try {
            sctpManagement.removeServer(serverConfig.getServerName());
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }

    @Override
    public void removeAllServers() {
        sctpManagement.getServers()
                .forEach(server -> {
                    try {
                        sctpManagement.stopServer(server.getName());
                    } catch (Exception e) {
                        log.warn("Can't stop association: {}. {}", server.getName(), e.getMessage(), e);
                    }
                    try {
                        sctpManagement.removeServer(server.getName());
                    } catch (Exception e) {
                        log.warn("Can't remove association: {}. {}", server.getName(), e.getMessage(), e);
                    }
                });
    }
}
