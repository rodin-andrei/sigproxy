package com.unifun.sigproxy.service;

import com.unifun.sigproxy.models.config.sctp.ClientLink;
import com.unifun.sigproxy.models.config.sctp.SctpServer;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface SctpConfigService {

    void addLink(ClientLink newLink);

    void addLinks(Set<ClientLink> newLinks);

    List<ClientLink> getLinkConfigs();

    Optional<ClientLink> getLinkConfig(String linkName);

    void addServerConfig(SctpServer newServer);

    void addServerConfigs(Set<SctpServer> newServers);

    List<SctpServer> getServerConfigs();

    Optional<SctpServer> getServerConfig(String serverName);
}
