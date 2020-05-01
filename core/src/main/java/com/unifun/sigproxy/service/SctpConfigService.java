package com.unifun.sigproxy.service;

import com.unifun.sigproxy.exception.NoConfigurationException;
import com.unifun.sigproxy.json.model.config.sctp.LinkConfig;
import com.unifun.sigproxy.json.model.config.sctp.SctpConfig;
import com.unifun.sigproxy.json.model.config.sctp.SctpServerConfig;

import java.util.Optional;
import java.util.Set;

public interface SctpConfigService {
    SctpConfig getSctpConfiguration() throws NoConfigurationException;

    Set<LinkConfig> getLinkConfig() throws NoConfigurationException;

    void setLinkConfig(LinkConfig newLink) throws NoConfigurationException;

    void setLinkConfig(Set<LinkConfig> newLinks) throws NoConfigurationException;

    Optional<LinkConfig> getLinkConfig(String linkName) throws NoConfigurationException;

    Set<SctpServerConfig> getServerConfig() throws NoConfigurationException;

    void setServerConfig(SctpServerConfig newServer) throws NoConfigurationException;

    void setServerConfig(Set<SctpServerConfig> newServers) throws NoConfigurationException;

    void updateLinkConfig(LinkConfig newLink) throws NoConfigurationException;

    Optional<SctpServerConfig> getServerConfig(String serverName) throws NoConfigurationException;

    void updateServerConfig(SctpServerConfig newServer) throws NoConfigurationException;
}
