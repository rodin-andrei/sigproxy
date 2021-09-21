package com.unifun.sigproxy.service.sctp.impl;


import com.unifun.sigproxy.exception.SS7AddClientLinkException;
import com.unifun.sigproxy.exception.SS7NotContentException;
import com.unifun.sigproxy.exception.SS7NotFoundException;
import com.unifun.sigproxy.exception.SS7RemoveSctpAssociationException;
import com.unifun.sigproxy.models.config.sctp.SctpClientAssociationConfig;
import com.unifun.sigproxy.models.config.sctp.SctpServerAssociationConfig;
import com.unifun.sigproxy.models.config.sctp.SctpServerConfig;
import com.unifun.sigproxy.repository.SigtranStackRepository;
import com.unifun.sigproxy.repository.sctp.RemoteSctpLinkRepository;
import com.unifun.sigproxy.repository.sctp.SctpLinkRepository;
import com.unifun.sigproxy.repository.sctp.SctpServerRepository;
import com.unifun.sigproxy.service.sctp.SctpConfigService;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
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
    public void addClinetLink(SctpClientAssociationConfig link) {
        try {
            sctpLinkRepository.save(link);
        } catch (Exception e) {
            throw new SS7AddClientLinkException("Adding a link " + link.getLinkName() + " failed.");
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

        Optional<SctpServerConfig> sctpServer = sctpServerRepository.findById(serverId);
        if (sctpServer.map(SctpServerConfig::getSctpServerAssociationConfigs).isPresent()) {
            if (sctpServer.map(SctpServerConfig::getSctpServerAssociationConfigs).get().isEmpty()) {
                throw new SS7NotContentException("Sigtran Server with id " + serverId + " doesn't contain sctp server links");
            } else return sctpServer.map(SctpServerConfig::getSctpServerAssociationConfigs).get();
        } else throw new SS7NotFoundException("Not found Server with id " + serverId);
    }

    @Override
    public SctpServerConfig getSctpServerById(Long serverId) {

        return sctpServerRepository.findById(serverId)
                .orElseThrow(() -> new SS7NotFoundException("Not found Server with id " + serverId));
    }

    @Override
    public SctpServerAssociationConfig getServerLinkById(Long serverLinkId) {
        return remoteSctpLinkRepository.findById(serverLinkId)
                .orElseThrow(()->new SS7NotFoundException("Not found Server Link with id " + serverLinkId));

    }

    @Override
    public void setServerLink(SctpServerAssociationConfig sctpServerAssociationConfig) {
        remoteSctpLinkRepository.save(sctpServerAssociationConfig);
    }

    @Override
    public SctpClientAssociationConfig getClientLinkById(Long clientLinkId) {
        return sctpLinkRepository.findById(clientLinkId)
                .orElseThrow(() -> new SS7NotFoundException("Not found Client Link by id " + clientLinkId));
    }

    @Override
    public SctpServerAssociationConfig removeServerLinkById(Long serverLinkId) {

        Optional<SctpServerAssociationConfig> sctpClientAssociationConfig = remoteSctpLinkRepository.findById(serverLinkId);
        if (sctpClientAssociationConfig.isEmpty())
            throw new SS7NotFoundException("Not found Server Link with id " + serverLinkId);
        try {
            remoteSctpLinkRepository.deleteById(serverLinkId);
        } catch (Exception e) {
            throw new SS7RemoveSctpAssociationException("Failed remove Server Link with id " + serverLinkId);
        }
        return sctpClientAssociationConfig.get();
    }

    @Override
    public void setSctpServer(SctpServerConfig sctpServer) {
        sctpServerRepository.save(sctpServer);
    }

    @Override
    public List<SctpServerConfig> getSctpServers(long stackId) {
        return sctpServerRepository.findAll();
    }
}
