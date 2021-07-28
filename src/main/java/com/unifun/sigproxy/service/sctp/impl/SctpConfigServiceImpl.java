package com.unifun.sigproxy.service.sctp.impl;

import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.models.config.sctp.SctpClientAssociationConfig;
import com.unifun.sigproxy.models.config.sctp.SctpServer;
import com.unifun.sigproxy.models.config.sctp.SctpServerAssociationConfig;
import com.unifun.sigproxy.repository.SigtranStackRepository;
import com.unifun.sigproxy.repository.sctp.RemoteSctpLinkRepository;
import com.unifun.sigproxy.repository.sctp.SctpLinkRepository;
import com.unifun.sigproxy.repository.sctp.SctpServerRepository;
import com.unifun.sigproxy.service.sctp.SctpConfigService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class SctpConfigServiceImpl implements SctpConfigService {
    private final SigtranStackRepository sigtranStackRepository;
    private final RemoteSctpLinkRepository remoteSctpLinkRepository;
    private final SctpLinkRepository sctpLinkRepository;
    private final SctpServerRepository sctpServerRepository;
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

    public Set<SctpClientAssociationConfig> getClientLinksByStackId(Long stackId) {
        Optional<SigtranStack> sigtranStack = sigtranStackRepository.findById(stackId);
        return sigtranStack.map(SigtranStack::getAssociations).orElse(null);
    }

    public String setClinetLink(SctpClientAssociationConfig link) {
        try {
            sctpLinkRepository.save(link);
            return "ok";
        } catch (Exception e) {
            log.error("Error insert new link in BD: ", e);
            return "error";
        }
    }

    public SigtranStack getSigtranStack(String sigtranStack) {
        return sigtranStackRepository.findByStackName(sigtranStack);
    }

    @Override
    public void deleteSctpLinkById(Long linkId) {
        sctpLinkRepository.deleteById(linkId);
    }

    public Set<SctpServerAssociationConfig> getServerLinksBySctpServerId(Long serverId) {
        Optional<SctpServer> sctpServer = sctpServerRepository.findById(serverId);
        return sctpServer.map(SctpServer::getSctpServerAssociationConfigs).orElse(new HashSet<>());
    }

    public SctpServer getSctpServerById(Long serverId) throws NotFoundException {
        Optional<SctpServer> sctpServer = sctpServerRepository.findById(serverId);
        if (sctpServer.isPresent()) return sctpServer.get();
        else throw new NotFoundException("Not found server");
    }

    public String setServerLink(SctpServerAssociationConfig sctpServerAssociationConfig) {
        remoteSctpLinkRepository.save(sctpServerAssociationConfig);
        return "OK";
    }

    public SigtranStack getSigtranStackById(long stackId) throws NotFoundException {
        Optional<SigtranStack> sigtranStack = sigtranStackRepository.findById(stackId);
        if (sigtranStack.isPresent()) return sigtranStack.get();
        else throw new NotFoundException("Not found sigtran stack");
    }

    public SctpClientAssociationConfig getClientLinksById(Long clientLinkId) throws NotFoundException {
        Optional<SctpClientAssociationConfig> clientAssociation = sctpLinkRepository.findById(clientLinkId);
        if (clientAssociation.isPresent()) return clientAssociation.get();
        else throw new NotFoundException("Not found sigtran stack");
    }
}
