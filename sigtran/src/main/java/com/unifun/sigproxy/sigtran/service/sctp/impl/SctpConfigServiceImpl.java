package com.unifun.sigproxy.sigtran.service.sctp.impl;


import com.unifun.sigproxy.sigtran.exception.SS7AddException;
import com.unifun.sigproxy.sigtran.exception.SS7NotContentException;
import com.unifun.sigproxy.sigtran.exception.SS7NotFoundException;
import com.unifun.sigproxy.sigtran.exception.SS7RemoveException;
import com.unifun.sigproxy.sigtran.models.config.SigtranStack;
import com.unifun.sigproxy.sigtran.models.config.sctp.SctpClientAssociationConfig;
import com.unifun.sigproxy.sigtran.models.config.sctp.SctpServerAssociationConfig;
import com.unifun.sigproxy.sigtran.models.config.sctp.SctpServerConfig;
import com.unifun.sigproxy.sigtran.models.config.sctp.SctpStackSettingsConfig;
import com.unifun.sigproxy.sigtran.repository.SigtranStackRepository;
import com.unifun.sigproxy.sigtran.repository.sctp.RemoteSctpLinkRepository;
import com.unifun.sigproxy.sigtran.repository.sctp.SctpLinkRepository;
import com.unifun.sigproxy.sigtran.repository.sctp.SctpServerRepository;
import com.unifun.sigproxy.sigtran.repository.sctp.SctpStackSettingsConfigRepository;
import com.unifun.sigproxy.sigtran.service.sctp.SctpConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    private final SctpStackSettingsConfigRepository sctpStackSettingsConfigRepository;

    @Override
    public SctpClientAssociationConfig getSctpClientAssociationConfigById(Long clientLinkId) {
        return sctpLinkRepository.findById(clientLinkId)
                .orElseThrow(() -> new SS7NotFoundException("Not found Sctp Client Association Config by id " + clientLinkId));
    }

    @Override
    public SctpServerAssociationConfig getSctpServerAssociationConfigById(Long serverLinkId) {
        return remoteSctpLinkRepository.findById(serverLinkId)
                .orElseThrow(() -> new SS7NotFoundException("Not found Sctp Server Association Config with id " + serverLinkId));
    }

    @Override
    public SctpServerConfig getSctpServerConfigById(Long serverId) {
        return sctpServerRepository.findById(serverId)
                .orElseThrow(() -> new SS7NotFoundException("Not found Server with id " + serverId));
    }

    @Override
    public SctpStackSettingsConfig getSctpStackSettingsConfigById(Long SctpStackSettingsConfigId) {
        return sctpStackSettingsConfigRepository.findById(SctpStackSettingsConfigId)
                .orElseThrow(() -> new SS7NotFoundException("Not found Sctp Stack Settings Config with id " + SctpStackSettingsConfigId));
    }

    @Override
    public Set<SctpClientAssociationConfig> getSctpClientAssociationConfigByStackId(Long stackId) {

        SigtranStack sigtranStack = sigtranStackRepository.findById(stackId)
                .orElseThrow(() -> new SS7NotContentException("Not found stack with id " + stackId));
        return Optional.ofNullable(sigtranStack.getAssociations())
                .orElseThrow(() -> new SS7NotContentException("Sigtran stack with id " + stackId + " doesn't contain sctp client links"));

    }

    @Override
    public Set<SctpServerAssociationConfig> getSctpServerAssociationConfigBySctpServerConfigId(Long SctpServerConfig) {

        SctpServerConfig sctpServer = sctpServerRepository.findById(SctpServerConfig)
                .orElseThrow(() -> new SS7NotFoundException("Not found Server with id " + SctpServerConfig));
        return Optional.ofNullable(sctpServer.getSctpServerAssociationConfigs())
                .orElseThrow(() -> new SS7NotContentException("Sigtran Server with id " + SctpServerConfig + " doesn't contain sctp server links"));
    }

    @Override
    public Set<SctpServerConfig> getSctpServerConfigByStackId(Long stackId) {
        SigtranStack sigtranStack = sigtranStackRepository.findById(stackId)
                .orElseThrow(() -> new SS7NotFoundException("Not found Sigtran Stack with id " + stackId));
        return Optional.ofNullable(sigtranStack.getSctpServerConfigs())
                .orElseThrow(() -> new SS7NotContentException("Sigtran Sigtran Stack with id " + stackId + " doesn't contain sctp server links"));
    }

    @Override
    public SctpClientAssociationConfig addSctpClientAssociationConfig(SctpClientAssociationConfig link) {
        try {
            return sctpLinkRepository.save(link);
        } catch (Exception e) {
            throw new SS7AddException("Adding a link " + link.getLinkName() + " failed.");
        }
    }

    @Override
    public SctpServerAssociationConfig addSctpServerAssociationConfig(SctpServerAssociationConfig sctpServerAssociationConfig) {
        try {
            return remoteSctpLinkRepository.save(sctpServerAssociationConfig);
        } catch (Exception e) {
            throw new SS7AddException("Error to add server with name " + sctpServerAssociationConfig.getLinkName());
        }
    }

    @Override
    public SctpServerConfig addSctpServerConfig(SctpServerConfig sctpServer) {
        try {
            return sctpServerRepository.save(sctpServer);
        } catch (Exception e) {
            throw new SS7AddException("Add Server with name " + sctpServer.getName() + " failed");
        }
    }

    @Override
    public void removeSctpClientAssociationConfigById(Long linkId) {
        try {
            sctpLinkRepository.deleteById(linkId);
        } catch (Exception e) {
            throw new SS7RemoveException("Failed remove Sctp Client Association Config with id " + linkId);
        }
    }

    @Override
    public void removeSctpServerAssociationConfigById(Long serverLinkId) {
        try {
            remoteSctpLinkRepository.deleteById(serverLinkId);
        } catch (Exception e) {
            throw new SS7RemoveException("Failed remove Server Link with id " + serverLinkId);
        }
    }

    @Override
    public void removeSctpServerConfigById(Long sctpServerConfigId) {
        try {
            sctpServerRepository.deleteById(sctpServerConfigId);
        } catch (Exception e) {
            throw new SS7RemoveException("Failed remove Sctp Server Config with id " + sctpServerConfigId);
        }
    }

    @Override
    public void removeSctpStackSettingsConfigById(Long sctpStackSettingsConfigId) {
        try {
            sctpStackSettingsConfigRepository.deleteById(sctpStackSettingsConfigId);
        } catch (Exception e) {
            throw new SS7RemoveException("Failed remove Sctp Stack Settings Config with id " + sctpStackSettingsConfigId);
        }
    }
}
