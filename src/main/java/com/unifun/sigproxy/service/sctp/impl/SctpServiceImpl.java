package com.unifun.sigproxy.service.sctp.impl;

import com.unifun.sigproxy.exception.InitializingException;
import com.unifun.sigproxy.exception.NoConfigurationException;
import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.models.config.sctp.ClientAssociation;
import com.unifun.sigproxy.models.config.sctp.SctpServer;
import com.unifun.sigproxy.service.sctp.SctpService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mobicents.protocols.api.Association;
import org.mobicents.protocols.api.IpChannelType;
import org.mobicents.protocols.api.Management;
import org.mobicents.protocols.sctp.netty.NettySctpManagementImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class SctpServiceImpl implements SctpService {
    @Value("${jss.persist.dir}")
    private String jssPersistDir;
    @Getter
    private final Map<String, Management> sctpManagements = new HashMap<>();

    @Override
    public void initialize(SigtranStack sigtranStack) throws NoConfigurationException, InitializingException {
        try {
            log.info("Initializing SCTP management...");
            if (sctpManagements.containsKey(sigtranStack.getStackName())) {
                throw new InitializingException("SctpManagement: " + sigtranStack.getStackName() + " already exist");
            }
            var sctpManagement = new NettySctpManagementImpl(sigtranStack.getStackName());
            sctpManagements.put(sigtranStack.getStackName(), sctpManagement);
            sctpManagement.setPersistDir(this.jssPersistDir);
            sctpManagement.start();
            sctpManagement.removeAllResourses();
        } catch (Exception e) {
            throw new InitializingException("Can't initialize sctp management: " + sigtranStack.getStackName(), e);
        }

        var clientAssociations = sigtranStack.getAssociations();
        var sctpServers = sigtranStack.getSctpServers();
        if (clientAssociations.isEmpty() && sctpServers.isEmpty()) {
            throw new NoConfigurationException("No links or servers to configure for SCTP managment: " + sigtranStack.getStackName());
        }

        addServers(sctpServers, sigtranStack.getStackName());
        addLinks(clientAssociations, sigtranStack.getStackName());

        if (log.isTraceEnabled()) {
            log.trace("Get Configured Associations...");
            sctpManagements.get(sigtranStack.getStackName())
                    .getAssociations()
                    .forEach((key, value) -> log.trace("Key: {} Value: {}", key, value));
            log.trace("Get Configured Servers...");
            sctpManagements.get(sigtranStack.getStackName())
                    .getServers()
                    .forEach(server -> log.trace("ServerName: {}", server.getName()));
        }
    }

    @Override
    public void stopStack(String sigtranStack) {
        try {
            sctpManagements.get(sigtranStack).stop();
        } catch (Exception e) {
            log.error("Can't stop SCTP management: {}", sigtranStack, e);
        }
    }

    @Override
    public Management getTransportManagement(String sigtranStack) {
        return this.sctpManagements.get(sigtranStack);
    }

    @Override
    public void addLink(ClientAssociation link, String sigtranStack) {
        //TODO add check already exist assoc
        try {
            Association association = sctpManagements.get(sigtranStack)
                    .addAssociation(
                            link.getLocalAddress(),
                            link.getLocalPort(),
                            link.getRemoteAddress(),
                            link.getRemotePort(),
                            link.getLinkName(),
                            IpChannelType.TCP,
                            link.getMultihomingAddresses()
                    );
            log.info("Added client association: {} to {} sigtran stack", link.getLinkName(), sigtranStack);
        } catch (Exception e) {
            log.error("Can't create link association " + link.getLinkName() + " . ", e);
        }
    }

    @Override
    public void addLinks(Set<ClientAssociation> newLinks, String sigtranStack) {
        newLinks.forEach(clientAssociation -> addLink(clientAssociation, sigtranStack));
    }

    @Override
    @Deprecated
    public void startLink(String linkName, String sigtranStack) {
        try {
            sctpManagements.get(sigtranStack).startAssociation(linkName);
            log.info("Started link: {}, sigtran stack: {}", linkName, sigtranStack);

        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }

    @Override
    public void stopLink(String linkName, String sigtranStack) {
        try {
            sctpManagements.get(sigtranStack).stopAssociation(linkName);
            log.info("Stopped link: {}, sigtran stack: {}", linkName, sigtranStack);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }

    @Override
    public void removeSctpLink(ClientAssociation link, String sigtranStack) {
        try {
            sctpManagements.get(sigtranStack).stopAssociation(link.getLinkName());
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        try {
            sctpManagements.get(sigtranStack).removeAssociation(link.getLinkName());
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }

    @Override
    public void removeAllLinks(String sigtranStack) {
        Management sctpManagement = sctpManagements.get(sigtranStack);
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
    public void addServer(SctpServer serverConfig, String sigtranStack) {
        try {
            sctpManagements.get(sigtranStack)
                    .addServer(
                            serverConfig.getName(),
                            serverConfig.getLocalAddress(),
                            serverConfig.getLocalPort(),
                            IpChannelType.TCP,
                            serverConfig.getMultihomingAddresses()
                    );
            log.info("Added server: {} to {} sigtran stack", serverConfig.getName(), sigtranStack);

            serverConfig.getServerAssociations().forEach(serverAssociation -> {
                try {
                    sctpManagements.get(sigtranStack)
                            .addServerAssociation(
                                    serverAssociation.getRemoteAddress(),
                                    serverAssociation.getRemotePort(),
                                    serverConfig.getName(),
                                    serverAssociation.getLinkName(),
                                    IpChannelType.TCP);
                    log.info("Added server association: {} to {} sigtran stack", serverAssociation.getLinkName(),
                            sigtranStack);
                } catch (Exception e) {
                    log.error("Can't create serverAssociation {}.", serverAssociation.getLinkName(), e);
                }
            });

            startServer(serverConfig.getName(), sigtranStack);
        } catch (Exception e) {
            log.error("Can't create server {}. {}", serverConfig.getName(), e.getMessage(), e);
        }
    }

    @Override
    public void addServers(Set<SctpServer> newServers, String sigtranStack) {
        newServers.forEach(sctpServer -> addServer(sctpServer, sigtranStack));
    }

    @Override
    public void startServer(String serverName, String sigtranStack) {
        try {
            sctpManagements.get(sigtranStack).startServer(serverName);
            log.info("Started server: {}, sigtran stack: {}", serverName, sigtranStack);

        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }

    @Override
    public void stopServer(String serverName, String sigtranStack) {
        try {
            sctpManagements.get(sigtranStack).stopServer(serverName);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }

    @Override
    public void removeServer(SctpServer serverConfig, String sigtranStack) {
        try {
            sctpManagements.get(sigtranStack).stopServer(serverConfig.getName());
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        try {
            sctpManagements.get(sigtranStack).removeServer(serverConfig.getName());
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }

    @Override
    public void removeAllServers(String sigtranStack) {
        Management sctpManagement = sctpManagements.get(sigtranStack);
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
