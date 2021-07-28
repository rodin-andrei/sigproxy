package com.unifun.sigproxy.service.sctp;

import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.models.config.sctp.ClientAssociation;
import com.unifun.sigproxy.models.config.sctp.SctpServer;
import com.unifun.sigproxy.models.config.sctp.ServerAssociation;
import javassist.NotFoundException;

import java.util.Set;

public interface SctpConfigService {
    Set<ClientAssociation> getClientLinksByStackId(Long stackId);

    String setClinetLink(ClientAssociation clientAssociation);

    SigtranStack getSigtranStack(String sigtranStack);

    void deleteSctpLinkById(Long linkId);

    Set<ServerAssociation> getServerLinksBySctpServerId(Long serverId);

    SctpServer getSctpServerById(Long serverId) throws NotFoundException;

    String setServerLink(ServerAssociation serverAssociation);

    SigtranStack getSigtranStackById(long stackId) throws NotFoundException;

    ClientAssociation getClientLinksById(Long clientLinkId) throws NotFoundException;
}
