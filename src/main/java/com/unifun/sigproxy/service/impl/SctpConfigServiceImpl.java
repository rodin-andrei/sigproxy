package com.unifun.sigproxy.service.impl;

import com.unifun.sigproxy.models.config.sctp.ClientLink;
import com.unifun.sigproxy.models.config.sctp.SctpServer;
import com.unifun.sigproxy.repository.sctp.RemoteSctpLinkRepository;
import com.unifun.sigproxy.repository.sctp.SctpLinkRepository;
import com.unifun.sigproxy.repository.sctp.SctpServerRepository;
import com.unifun.sigproxy.service.SctpConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class SctpConfigServiceImpl implements SctpConfigService {
    private final RemoteSctpLinkRepository remoteSctpLinkRepository;
    private final SctpLinkRepository sctpLinkRepository;
    private final SctpServerRepository sctpServerRepository;


    @Override
    public void addLink(ClientLink newLink) {
        List<ClientLink> existLinks = sctpLinkRepository.findAll();

        boolean present = existLinks.stream()
                .anyMatch(link -> link.getLinkName().equals(newLink.getLinkName()));
        if (present) {
            throw new IllegalArgumentException("Link with name \"" + newLink.getLinkName() + "\" already exist");
        }
        sctpLinkRepository.save(newLink);
        log.info("Added new link: {}", newLink.getLinkName());
    }

    @Override
    public void addLinks(Set<ClientLink> newLinks) {
        newLinks.forEach(this::addLink);
    }

    @Override
    public List<ClientLink> getLinkConfigs() {
        return sctpLinkRepository.findAll();
    }

    @Override
    public Optional<ClientLink> getLinkConfig(String linkName) {
        //TODO replace to findByName
        return sctpLinkRepository.findAll().stream()
                .filter(link -> link.getLinkName() != null && link.getLinkName().equals(linkName))
                .findFirst();
    }

    @Override
    public void addServerConfig(SctpServer newServer) {

        List<SctpServer> servers = sctpServerRepository.findAll();
        boolean present = servers.stream()
                .anyMatch(link -> link.getServerName().equals(newServer.getServerName()));
        if (present) {
            throw new IllegalArgumentException("Server with name \"" + newServer.getServerName() + "\n already exist");
        }

        sctpServerRepository.save(newServer);
        log.info("Added new server: {}", newServer.getServerName());
    }

    @Override
    public void addServerConfigs(Set<SctpServer> newServers) {
        newServers.forEach(this::addServerConfig);
    }

    @Override
    public List<SctpServer> getServerConfigs() {
        return sctpServerRepository.findAll();
    }

    @Override
    public Optional<SctpServer> getServerConfig(String serverName) {
        return sctpServerRepository.findAll().stream()
                .filter(link -> link.getServerName() != null && link.getServerName().equals(serverName))
                .findFirst();
    }
}
