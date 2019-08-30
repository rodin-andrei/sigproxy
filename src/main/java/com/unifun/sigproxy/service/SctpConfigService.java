package com.unifun.sigproxy.service;

import com.unifun.sigproxy.exception.NoConfigurationException;
import com.unifun.sigproxy.model.LinkConfig;
import com.unifun.sigproxy.model.SctpConfig;

import java.util.Optional;
import java.util.Set;

public interface SctpConfigService {
    SctpConfig getSctpConfiguration() throws NoConfigurationException;

    Optional<LinkConfig> getLinkConfig(String linkName) throws NoConfigurationException;

    void setLinkConfig(LinkConfig newLink) throws NoConfigurationException;

    void updateLinkConfig(LinkConfig newLink) throws NoConfigurationException;

    void setLinkConfig(Set<LinkConfig> newLinks) throws NoConfigurationException;

}
