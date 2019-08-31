package com.unifun.sigproxy.service.impl;

import com.unifun.sigproxy.exception.NoConfigurationException;
import com.unifun.sigproxy.model.config.LinkConfig;
import com.unifun.sigproxy.model.config.SctpConfig;
import com.unifun.sigproxy.model.config.SctpServerConfig;
import com.unifun.sigproxy.model.config.SigtranConfig;
import com.unifun.sigproxy.repository.SigtranRepository;
import com.unifun.sigproxy.service.SctpConfigService;
import com.unifun.sigproxy.service.SctpService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SctpConfigServiceImpl implements SctpConfigService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SctpConfigServiceImpl.class);

    private final SigtranRepository sigtranRepository;
    private final SctpService sctpService;

    @Override
    public SctpConfig getSctpConfiguration() throws NoConfigurationException {
        SctpConfig sctpConfig = getSigtranConfig().getSctpConfig();
        if (sctpConfig == null) {
            throw new NoConfigurationException("No SCTP Configuration.");
        }
        return sctpConfig;
    }

    @Override
    public Set<LinkConfig> getLinkConfig() throws NoConfigurationException {
        return getSctpConfiguration().getLinkConfig();
    }

    @Override
    public void setLinkConfig(LinkConfig newLink) throws NoConfigurationException {
        Set<LinkConfig> linksSet = getSctpConfiguration().getLinkConfig();

        linksSet.stream()
                .filter(link -> link.getLinkName().equals(newLink.getLinkName()))
                .findFirst()
                .ifPresent(linksSet::remove);

        linksSet.add(newLink);
        LOGGER.info("Added new link: {}", newLink.getLinkName());

        sctpService.updateSctpLink(newLink);
    }

    @Override
    public void setLinkConfig(Set<LinkConfig> newLinks) throws NoConfigurationException {
        getSctpConfiguration().setLinkConfig(newLinks);
        //TODO: Stop all links...
        newLinks.forEach(sctpService::updateSctpLink);
    }

    @Override
    public Optional<LinkConfig> getLinkConfig(String linkName) throws NoConfigurationException {
        return getSctpConfiguration().getLinkConfig().stream()
                .filter(link -> link.getLinkName() != null && link.getLinkName().equals(linkName))
                .findFirst();
    }

    @Override
    public Set<SctpServerConfig> getServerConfig() throws NoConfigurationException {
        return getSctpConfiguration().getSctpServerConfig();
    }

    @Override
    public void setServerConfig(SctpServerConfig newServer) throws NoConfigurationException {
        Set<SctpServerConfig> servers = getSctpConfiguration().getSctpServerConfig();

        servers.stream()
                .filter(link -> link.getServerName().equals(newServer.getServerName()))
                .findFirst()
                .ifPresent(servers::remove);

        servers.add(newServer);
        LOGGER.info("Added new server: {}", newServer.getServerName());

        sctpService.updateSctpServer(newServer);
    }

    @Override
    public void setServerConfig(Set<SctpServerConfig> newServers) throws NoConfigurationException {
        getSctpConfiguration().setSctpServerConfig(newServers);
        //TODO: Stop all servers
        newServers.forEach(sctpService::updateSctpServer);
    }

    @Override
    public Optional<SctpServerConfig> getServerConfig(String serverName) throws NoConfigurationException {
        return getSctpConfiguration().getSctpServerConfig().stream()
                .filter(link -> link.getServerName() != null && link.getServerName().equals(serverName))
                .findFirst();
    }

    @Override
    public void updateLinkConfig(LinkConfig newLink) throws NoConfigurationException {
        Set<LinkConfig> linksSet = getSctpConfiguration().getLinkConfig();
        linksSet.stream()
                .filter(link -> link.getLinkName().equals(newLink.getLinkName()))
                .findFirst()
                .ifPresent(oldLink -> {
                    oldLink.setLinkType(newLink.getLinkType());
                    oldLink.setRemoteAddress(newLink.getRemoteAddress());
                    oldLink.setRemotePort(newLink.getRemotePort());
                    oldLink.setLocalAddress(newLink.getLocalAddress());
                    oldLink.setLocalPort(newLink.getLocalPort());
                    sctpService.updateSctpLink(oldLink);
                });
        LOGGER.info("Updated link: {}", newLink.getLinkName());
    }

    @Override
    public void updateServerConfig(SctpServerConfig newServer) throws NoConfigurationException {
        Set<SctpServerConfig> servers = getSctpConfiguration().getSctpServerConfig();
        servers.stream()
                .filter(server -> server.getServerName().equals(newServer.getServerName()))
                .findFirst()
                .ifPresent(oldServer -> {
                    oldServer.setLinkType(newServer.getLinkType());
                    oldServer.setLocalAddress(newServer.getLocalAddress());
                    oldServer.setLocalPort(newServer.getLocalPort());
                    oldServer.setExtraAddresses(newServer.getExtraAddresses());
                    oldServer.setRemoteLinkConfig(newServer.getRemoteLinkConfig());
                    sctpService.updateSctpServer(oldServer);
                });
        LOGGER.info("Updated server: {}", newServer.getServerName());
    }

    private SigtranConfig getSigtranConfig() throws NoConfigurationException {
        SigtranConfig sigtranConfig = sigtranRepository.getSigtranConfig();
        if (sigtranConfig == null) {
            LOGGER.error("Sigtran Configuration is null.");
            throw new NoConfigurationException("No Sigtran configuration");
        }
        return sigtranConfig;
    }
}
