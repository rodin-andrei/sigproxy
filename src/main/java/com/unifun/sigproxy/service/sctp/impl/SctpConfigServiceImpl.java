package com.unifun.sigproxy.service.sctp.impl;


import com.unifun.sigproxy.exception.*;
import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.models.config.sctp.SctpClientAssociationConfig;
import com.unifun.sigproxy.models.config.sctp.SctpServerAssociationConfig;
import com.unifun.sigproxy.models.config.sctp.SctpServerConfig;
import com.unifun.sigproxy.repository.SigtranStackRepository;
import com.unifun.sigproxy.repository.sctp.RemoteSctpLinkRepository;
import com.unifun.sigproxy.repository.sctp.SctpLinkRepository;
import com.unifun.sigproxy.repository.sctp.SctpServerRepository;
import com.unifun.sigproxy.service.sctp.SctpConfigService;
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
    private final SigtranStackRepository sigtranStackRepository;
    private final RemoteSctpLinkRepository remoteSctpLinkRepository;
    private final SctpLinkRepository sctpLinkRepository;
    private final SctpServerRepository sctpServerRepository;

    @Override
    public Set<SctpClientAssociationConfig> getClientLinksByStackId(Long stackId) {

        SigtranStack sigtranStack = sigtranStackRepository.findById(stackId)
                .orElseThrow(() -> new SS7NotContentException("Not found stack with id " + stackId));
        return Optional.ofNullable(sigtranStack.getAssociations())
                .orElseThrow(() -> new SS7NotContentException("Sigtran stack with id " + stackId + " doesn't contain sctp client links"));


    }

    @Override
    public SctpClientAssociationConfig addClinetLink(SctpClientAssociationConfig link) {
        try {
            return sctpLinkRepository.save(link);
        } catch (Exception e) {
            throw new SS7AddException("Adding a link " + link.getLinkName() + " failed.");
        }
    }

    @Override
    public void removeClientLinkById(Long linkId) {
        try {
            sctpLinkRepository.deleteById(linkId);
        } catch (Exception e) {
            throw new SS7NotFoundException("Not found sctp link with id " + linkId + " for delete");
        }
    }

    @Override
    public Set<SctpServerAssociationConfig> getServerLinksBySctpServerId(Long serverId) {

        SctpServerConfig sctpServer = sctpServerRepository.findById(serverId)
                .orElseThrow(() -> new SS7NotFoundException("Not found Server with id " + serverId));
        return Optional.ofNullable(sctpServer.getSctpServerAssociationConfigs())
                .orElseThrow(() -> new SS7NotContentException("Sigtran Server with id " + serverId + " doesn't contain sctp server links"));
    }

    @Override
    public SctpServerConfig getSctpServerById(Long serverId) {

        return sctpServerRepository.findById(serverId)
                .orElseThrow(() -> new SS7NotFoundException("Not found Server with id " + serverId));
    }

    @Override
    public SctpServerAssociationConfig getServerLinkById(Long serverLinkId) {
        return remoteSctpLinkRepository.findById(serverLinkId)
                .orElseThrow(() -> new SS7NotFoundException("Not found Server Link with id " + serverLinkId));

    }

    @Override
    public SctpServerAssociationConfig addServerLink(SctpServerAssociationConfig sctpServerAssociationConfig) {
        try {
            return remoteSctpLinkRepository.save(sctpServerAssociationConfig);
        } catch (Exception e) {
            throw new SS7AddException("Error to add server with name " + sctpServerAssociationConfig.getLinkName());
        }
    }

    @Override
    public SctpClientAssociationConfig getClientLinkById(Long clientLinkId) {
        return sctpLinkRepository.findById(clientLinkId)
                .orElseThrow(() -> new SS7NotFoundException("Not found Client Link by id " + clientLinkId));
    }

    @Override
    public SctpServerAssociationConfig removeServerLinkById(Long serverLinkId) {

        SctpServerAssociationConfig sctpClientAssociationConfig = remoteSctpLinkRepository.findById(serverLinkId)
                .orElseThrow(() -> new SS7NotFoundException("Not found Server Link with id " + serverLinkId));
        try {
            remoteSctpLinkRepository.deleteById(serverLinkId);
        } catch (Exception e) {
            throw new SS7RemoveException("Failed remove Server Link with id " + serverLinkId);
        }
        return sctpClientAssociationConfig;
    }

    @Override
    public SctpServerConfig addSctpServer(SctpServerConfig sctpServer) {
        try {
            return sctpServerRepository.save(sctpServer);
        } catch (Exception e) {
            throw new SS7AddException("Add Server with name " + sctpServer.getName() + " failed");
        }
    }

    @Override
    public List<SctpServerConfig> getSctpServersByStackId(Long stackId) {
        return sctpServerRepository.findAll();
    }
}
