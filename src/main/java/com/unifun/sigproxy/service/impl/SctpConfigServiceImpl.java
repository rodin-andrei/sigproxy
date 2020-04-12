package com.unifun.sigproxy.service.impl;

import com.unifun.sigproxy.model.config.sctp.LinkConfig;
import com.unifun.sigproxy.model.config.sctp.SctpConfig;
import com.unifun.sigproxy.model.config.sctp.SctpServerConfig;
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
    public SctpConfig getSctpConfiguration() {
        return this.sigtranRepository.getSctpConfig();
    }

    @Override
    public Set<LinkConfig> getLinkConfig() {
        return sigtranRepository.getSctpConfig().getLinkConfig();
    }

    @Override
    public void setLinkConfig(LinkConfig newLink) {
        Set<LinkConfig> linksSet = sigtranRepository.getSctpConfig().getLinkConfig();

        linksSet.stream()
                .filter(link -> link.getLinkName().equals(newLink.getLinkName()))
                .findFirst()
                .ifPresent(linksSet::remove);

        linksSet.add(newLink);
        LOGGER.info("Added new link: {}", newLink.getLinkName());

        sctpService.updateSctpLink(newLink);
    }

    @Override
    public void setLinkConfig(Set<LinkConfig> newLinks) {
        sigtranRepository.getSctpConfig().setLinkConfig(newLinks);
        sctpService.removeAllLinks();
        sctpService.addNewSctpLinks(newLinks);
    }

    @Override
    public Optional<LinkConfig> getLinkConfig(String linkName) {
        return sigtranRepository.getSctpConfig().getLinkConfig().stream()
                .filter(link -> link.getLinkName() != null && link.getLinkName().equals(linkName))
                .findFirst();
    }

    @Override
    public Set<SctpServerConfig> getServerConfig() {
        return sigtranRepository.getSctpConfig().getSctpServerConfig();
    }

    @Override
    public void setServerConfig(SctpServerConfig newServer) {
        Set<SctpServerConfig> servers = sigtranRepository.getSctpConfig().getSctpServerConfig();

        servers.stream()
                .filter(link -> link.getServerName().equals(newServer.getServerName()))
                .findFirst()
                .ifPresent(servers::remove);

        servers.add(newServer);
        LOGGER.info("Added new server: {}", newServer.getServerName());

        sctpService.updateSctpServer(newServer);
    }

    @Override
    public void setServerConfig(Set<SctpServerConfig> newServers) {
        sigtranRepository.getSctpConfig().setSctpServerConfig(newServers);
        sctpService.removeAllServers();
        sctpService.addNewSctpServers(newServers);
    }

    @Override
    public Optional<SctpServerConfig> getServerConfig(String serverName) {
        return sigtranRepository.getSctpConfig().getSctpServerConfig().stream()
                .filter(link -> link.getServerName() != null && link.getServerName().equals(serverName))
                .findFirst();
    }

    @Override
    public void updateLinkConfig(LinkConfig newLink) {
        Set<LinkConfig> linksSet = sigtranRepository.getSctpConfig().getLinkConfig();
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
    public void updateServerConfig(SctpServerConfig newServer) {
        Set<SctpServerConfig> servers = sigtranRepository.getSctpConfig().getSctpServerConfig();
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
}
