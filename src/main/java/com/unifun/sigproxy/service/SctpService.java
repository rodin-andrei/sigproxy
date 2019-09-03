package com.unifun.sigproxy.service;

import com.unifun.sigproxy.exception.InitializingException;
import com.unifun.sigproxy.exception.NoConfigurationException;
import com.unifun.sigproxy.model.config.LinkConfig;
import com.unifun.sigproxy.model.config.SctpConfig;
import com.unifun.sigproxy.model.config.SctpServerConfig;
import com.unifun.sigproxy.model.dto.SctpLinkDto;
import com.unifun.sigproxy.model.dto.SctpServerDto;
import org.mobicents.protocols.api.Management;

import java.util.Set;

public interface SctpService {
    void initialize(SctpConfig sctpConfig) throws NoConfigurationException, InitializingException;

    void stop();

    Management getTransportManagement();

    void removeAllLinks();

    void removeAllServers();

    void updateSctpLink(LinkConfig linkConfig);

    void addNewSctpLinks(Set<LinkConfig> newLinks);

    void updateSctpServer(SctpServerConfig serverConfig);

    void addNewSctpServers(Set<SctpServerConfig> newServers);

    Set<SctpLinkDto> getLinkStatus();

    Set<SctpServerDto> getServerLinkStatuses();

    void stopLink(String linkName);

    void startLink(String linkName);

    void stopServer(String serverName);

    void startServer(String serverName);
}
