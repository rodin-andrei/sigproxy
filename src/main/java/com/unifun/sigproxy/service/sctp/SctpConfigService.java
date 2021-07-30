package com.unifun.sigproxy.service.sctp;

import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.models.config.sctp.SctpClientAssociationConfig;
import com.unifun.sigproxy.models.config.sctp.SctpServer;
import com.unifun.sigproxy.models.config.sctp.SctpServerAssociationConfig;
import javassist.NotFoundException;

import java.util.Set;

public interface SctpConfigService {
    Set<SctpClientAssociationConfig> getClientLinksByStackId(Long stackId) throws NotFoundException;

    SctpClientAssociationConfig getClientLinkById(Long clientLinkId) throws NotFoundException;

    void setClinetLink(SctpClientAssociationConfig sctpClientAssociationConfig);

    void removeClientLinkById(Long linkId);

    Set<SctpServerAssociationConfig> getServerLinksBySctpServerId(Long serverId) throws NotFoundException;

    SctpServer getSctpServerById(Long serverId) throws NotFoundException;

    void setServerLink(SctpServerAssociationConfig sctpServerAssociationConfig);

    SigtranStack getSigtranStackById(Long stackId) throws NotFoundException;

    void removeServerLinkById(Long serverLinkId);
}
