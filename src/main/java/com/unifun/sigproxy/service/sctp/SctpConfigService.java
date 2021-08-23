package com.unifun.sigproxy.service.sctp;

import com.unifun.sigproxy.controller.dto.SctpServerAssociationConfigDto;
import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.models.config.sctp.SctpClientAssociationConfig;
import com.unifun.sigproxy.models.config.sctp.SctpServerAssociationConfig;
import com.unifun.sigproxy.models.config.sctp.SctpServerConfig;
import javassist.NotFoundException;

import java.util.List;
import java.util.Set;

public interface SctpConfigService {

    SctpClientAssociationConfig getClientLinkById(Long clientLinkId) throws NotFoundException;

    void addClinetLink(SctpClientAssociationConfig sctpClientAssociationConfig);

    void removeClientLinkById(Long linkId);

    Set<SctpServerAssociationConfig> getServerLinksBySctpServerId(Long serverId);

    SctpServerConfig getSctpServerById(Long serverId) throws NotFoundException;

    void setServerLink(SctpServerAssociationConfig sctpServerAssociationConfig);

    SctpServerAssociationConfig removeServerLinkById(Long serverLinkId);

    void setSctpServer(SctpServerConfig sctpServer);

    List<SctpServerConfig> getSctpServers(long stackId);
}
