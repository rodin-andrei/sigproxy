package com.unifun.sigproxy.service;

import com.unifun.sigproxy.exception.InitializingException;
import com.unifun.sigproxy.exception.NoConfigurationException;
import com.unifun.sigproxy.json.model.config.sctp.LinkConfig;
import com.unifun.sigproxy.json.model.config.sctp.SctpConfig;
import com.unifun.sigproxy.json.model.config.sctp.SctpServerConfig;
import com.unifun.sigproxy.json.model.dto.SctpLinkDto;
import com.unifun.sigproxy.json.model.dto.SctpServerDto;
import org.mobicents.protocols.api.Management;

import java.util.Set;

public interface SctpService {
    void initialize(SctpConfig sctpConfig) throws NoConfigurationException, InitializingException;

    void stop();

    Management getTransportManagement();

    Set<SctpLinkDto> getLinkStatuses();

    Set<SctpServerDto> getServerLinkStatuses();

    void removeAllLinks();

    void removeAllServers();

    void updateSctpLink(LinkConfig linkConfig);

    void addNewSctpLinks(Set<LinkConfig> newLinks);

    void updateSctpServer(SctpServerConfig serverConfig);

    void addNewSctpServers(Set<SctpServerConfig> newServers);

    void stopLink(String linkName);

    void startLink(String linkName);

    void stopServer(String serverName);

    void startServer(String serverName);
}
