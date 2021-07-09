package com.unifun.sigproxy.service;

import com.unifun.sigproxy.dto.SctpLinkDto;
import com.unifun.sigproxy.dto.SctpServerDto;
import com.unifun.sigproxy.exception.InitializingException;
import com.unifun.sigproxy.exception.NoConfigurationException;
import com.unifun.sigproxy.models.config.sctp.ClientLink;
import com.unifun.sigproxy.models.config.sctp.SctpServer;
import org.mobicents.protocols.api.Management;

import java.util.Set;

public interface SctpService {
    void initialize() throws NoConfigurationException, InitializingException;

    void stop();

    Management getTransportManagement();

    Set<SctpLinkDto> getLinkStatuses();

    Set<SctpServerDto> getServerLinkStatuses();

    void removeAllLinks();

    void removeAllServers();

    void addLink(ClientLink clientLink);

    void addLinks(Set<ClientLink> newLinks);

    void addServer(SctpServer serverConfig);

    void addServers(Set<SctpServer> newServers);

    void stopLink(String linkName);

    void startLink(String linkName);

    void stopServer(String serverName);

    void startServer(String serverName);
}
