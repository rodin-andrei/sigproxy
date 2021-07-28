package com.unifun.sigproxy.service.sctp;

import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.models.config.sctp.SctpClientAssociationConfig;
import com.unifun.sigproxy.models.config.sctp.SctpServer;
import com.unifun.sigproxy.models.config.sctp.SctpServerAssociationConfig;
import javassist.NotFoundException;

import java.util.Set;

public interface SctpConfigService {
    Set<SctpClientAssociationConfig> getClientLinksByStackId(Long stackId);

    String setClinetLink(SctpClientAssociationConfig sctpClientAssociationConfig);

    SigtranStack getSigtranStack(String sigtranStack);

    void deleteSctpLinkById(Long linkId);

    Set<SctpServerAssociationConfig> getServerLinksBySctpServerId(Long serverId);

    SctpServer getSctpServerById(Long serverId) throws NotFoundException;

    String setServerLink(SctpServerAssociationConfig sctpServerAssociationConfig);

    SigtranStack getSigtranStackById(long stackId) throws NotFoundException;

    SctpClientAssociationConfig getClientLinksById(Long clientLinkId) throws NotFoundException;
}
