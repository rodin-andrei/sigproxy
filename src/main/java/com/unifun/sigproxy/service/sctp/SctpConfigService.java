package com.unifun.sigproxy.service.sctp;

import com.unifun.sigproxy.models.config.sctp.SctpClientAssociationConfig;
import com.unifun.sigproxy.models.config.sctp.SctpServerAssociationConfig;
import com.unifun.sigproxy.models.config.sctp.SctpServerConfig;
import javassist.NotFoundException;

import java.util.List;
import java.util.Set;

public interface SctpConfigService {

    SctpClientAssociationConfig getClientLinkById(Long clientLinkId);

    void removeClientLinkById(Long linkId); //todo return removed id or object

    Set<SctpServerAssociationConfig> getServerLinksBySctpServerId(Long serverId);

    SctpServerConfig getSctpServerById(Long serverId);

    SctpServerAssociationConfig getServerLinkById(Long serverLinkId);

    SctpServerAssociationConfig removeServerLinkById(Long serverLinkId);

    List<SctpServerConfig> getSctpServersByStackId(Long stackId);

    Set<SctpClientAssociationConfig> getClientLinksByStackId(Long stackId);

    void addSctpServer(SctpServerConfig sctpServer);

    void addServerLink(SctpServerAssociationConfig sctpServerAssociationConfig);

    void addClinetLink(SctpClientAssociationConfig sctpClientAssociationConfig);
}
