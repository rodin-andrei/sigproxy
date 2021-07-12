package com.unifun.sigproxy.service;

import com.unifun.sigproxy.models.config.sctp.ClientAssociation;
import com.unifun.sigproxy.models.config.sctp.SctpServer;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface SctpConfigService {

    void addLink(ClientAssociation newLink);

    void addLinks(Set<ClientAssociation> newLinks);

    List<ClientAssociation> getLinkConfigs();

    Optional<ClientAssociation> getLinkConfig(String linkName);

    void addServerConfig(SctpServer newServer);

    void addServerConfigs(Set<SctpServer> newServers);

    List<SctpServer> getServerConfigs();

    Optional<SctpServer> getServerConfig(String serverName);
}
