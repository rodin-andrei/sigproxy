package com.unifun.sigproxy.service.sctp.impl;


import com.unifun.sigproxy.models.config.sctp.SctpClientAssociationConfig;
import com.unifun.sigproxy.models.config.sctp.SctpServerAssociationConfig;
import com.unifun.sigproxy.models.config.sctp.SctpServerConfig;
import com.unifun.sigproxy.repository.SigtranStackRepository;
import com.unifun.sigproxy.repository.sctp.RemoteSctpLinkRepository;
import com.unifun.sigproxy.repository.sctp.SctpLinkRepository;
import com.unifun.sigproxy.repository.sctp.SctpServerRepository;
import com.unifun.sigproxy.service.sctp.SctpConfigService;
import javassist.NotFoundException;
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

    public void setClinetLink(SctpClientAssociationConfig link) {
        sctpLinkRepository.save(link);
    }

    @Override
    public void removeClientLinkById(Long linkId) {
        sctpLinkRepository.deleteById(linkId);
    }

    public Set<SctpServerAssociationConfig> getServerLinksBySctpServerId(Long serverId) throws NotFoundException {
        Optional<SctpServerConfig> sctpServer = sctpServerRepository.findById(serverId);
        return sctpServer.map(SctpServerConfig::getSctpServerAssociationConfigs)
                .orElseThrow(() -> new NotFoundException("Not found Server by server id " + serverId));
    }

    public SctpServerConfig getSctpServerById(Long serverId) throws NotFoundException {
        return sctpServerRepository.findById(serverId)
                .orElseThrow(() -> new NotFoundException("Not found server by id " + serverId));
    }

    public void setServerLink(SctpServerAssociationConfig sctpServerAssociationConfig) {
        remoteSctpLinkRepository.save(sctpServerAssociationConfig);
    }

    public SctpClientAssociationConfig getClientLinkById(Long clientLinkId) throws NotFoundException {
        return sctpLinkRepository.findById(clientLinkId)
                .orElseThrow(() -> new NotFoundException("Not found Client Link by id " + clientLinkId));

    }

    public void removeServerLinkById(Long serverLinkId) {
        remoteSctpLinkRepository.deleteById(serverLinkId);
    }

    public void setSctpServer(SctpServerConfig sctpServer){
        sctpServerRepository.save(sctpServer);
    }

    @Override
    public List<SctpServerConfig> getSctpServers(long stackId) {
       return sctpServerRepository.findAll();
    }
}
