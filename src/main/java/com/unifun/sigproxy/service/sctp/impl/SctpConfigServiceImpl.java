package com.unifun.sigproxy.service.sctp.impl;

import com.unifun.sigproxy.service.sctp.SctpConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SctpConfigServiceImpl implements SctpConfigService {
//    private final SigtranStackRepository sigtranStackRepository;
//    private final RemoteSctpLinkRepository remoteSctpLinkRepository;
//    private final SctpLinkRepository sctpLinkRepository;
//    private final SctpServerRepository sctpServerRepository;
//
//    @Override
//    public void addLink(ClientAssociation newLink, String sigtranStack) {
//        List<ClientAssociation> existLinks = sctpLinkRepository.findAll();
//
//        boolean present = existLinks.stream()
//                .anyMatch(link -> link.getLinkName().equals(newLink.getLinkName()));
//        if (present) {
//            throw new IllegalArgumentException("Link with name \"" + newLink.getLinkName() + "\" already exist");
//        }
//        sctpLinkRepository.save(newLink);
//        log.info("Added new link: {}", newLink.getLinkName());
//    }
//
//    @Override
//    public void addLinks(Set<ClientAssociation> newLinks) {
//        newLinks.forEach(this::addLink);
//    }
//
//    @Override
//    public List<ClientAssociation> getLinkConfigs() {
//        return sctpLinkRepository.findAll();
//    }
//
//    @Override
//    public Optional<ClientAssociation> getLinkConfig(String linkName) {
//        //TODO replace to findByName
//        return sctpLinkRepository.findAll().stream()
//                .filter(link -> link.getLinkName() != null && link.getLinkName().equals(linkName))
//                .findFirst();
//    }
//
//    @Override
//    public void addServerConfig(SctpServer newServer) {
//
//        List<SctpServer> servers = sctpServerRepository.findAll();
//        boolean present = servers.stream()
//                .anyMatch(link -> link.getName().equals(newServer.getName()));
//        if (present) {
//            throw new IllegalArgumentException("Server with name \"" + newServer.getName() + "\n already exist");
//        }
//
//        sctpServerRepository.save(newServer);
//        log.info("Added new server: {}", newServer.getName());
//    }
//
//    @Override
//    public void addServerConfigs(Set<SctpServer> newServers) {
//        newServers.forEach(this::addServerConfig);
//    }
//
//    @Override
//    public List<SctpServer> getServerConfigs() {
//        return sctpServerRepository.findAll();
//    }
//
//    @Override
//    public Optional<SctpServer> getServerConfig(String serverName) {
//        return sctpServerRepository.findAll().stream()
//                .filter(link -> link.getName() != null && link.getName().equals(serverName))
//                .findFirst();
//    }
}
