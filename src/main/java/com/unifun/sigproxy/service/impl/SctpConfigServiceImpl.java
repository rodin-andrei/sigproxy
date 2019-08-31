package com.unifun.sigproxy.service.impl;

import com.unifun.sigproxy.exception.NoConfigurationException;
import com.unifun.sigproxy.model.config.LinkConfig;
import com.unifun.sigproxy.model.config.SctpConfig;
import com.unifun.sigproxy.model.config.SigtranConfig;
import com.unifun.sigproxy.repository.SigtranRepository;
import com.unifun.sigproxy.service.SctpConfigService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SctpConfigServiceImpl implements SctpConfigService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SctpConfigServiceImpl.class);

    private final SigtranRepository sigtranRepository;

    @Override
    public SctpConfig getSctpConfiguration() throws NoConfigurationException {
        SigtranConfig sigtranConfig = getSigtranConfig();
        return sigtranConfig.getSctpConfig();
    }

    @Override
    public Optional<LinkConfig> getLinkConfig(String linkName) throws NoConfigurationException {
        SctpConfig sctpConfiguration = getSctpConfiguration();
        if (sctpConfiguration != null) {
            return sctpConfiguration.getLinkConfig().stream()
                    .filter(link -> link.getLinkName() != null && link.getLinkName().equals(linkName))
                    .findFirst();
        }
        throw new NoConfigurationException("No Sctp configuration.");
    }

    @Override
    public void setLinkConfig(LinkConfig newLink) throws NoConfigurationException {
        Set<LinkConfig> linksSet = getSctpConfiguration().getLinkConfig();

        linksSet.stream()
                .filter(link -> link.getLinkName().equals(newLink.getLinkName()))
                .findFirst()
                .ifPresent(linksSet::remove);

        linksSet.add(newLink);
        LOGGER.info("Added new link: {}", newLink.getLinkName());

        updateSigtranLink(newLink);
    }

    @Override
    public void updateLinkConfig(LinkConfig newLink) throws NoConfigurationException {
        Set<LinkConfig> linksSet = getSctpConfiguration().getLinkConfig();
        linksSet.stream()
                .filter(link -> link.getLinkName().equals(newLink.getLinkName()))
                .findFirst()
                .ifPresent(oldLink -> {
                    oldLink.setLinkType(newLink.getLinkType());
                    oldLink.setRemoteAddress(newLink.getRemoteAddress());
                    oldLink.setRemotePort(newLink.getRemotePort());
                    oldLink.setLocalAddress(newLink.getLocalAddress());
                    oldLink.setLocalPort(newLink.getLocalPort());
                });
        LOGGER.info("Updated link: {}", newLink.getLinkName());
        updateSigtranLink(newLink);
    }

    @Override
    public void setLinkConfig(Set<LinkConfig> newLinks) throws NoConfigurationException {
        //TODO: Reset all links
    }

    private SigtranConfig getSigtranConfig() throws NoConfigurationException {
        SigtranConfig sigtranConfig = sigtranRepository.getSigtranConfig();
        if (sigtranConfig == null) {
            LOGGER.error("Sigtran Configuration is null.");
            throw new NoConfigurationException("No Sigtran configuration");
        }
        return sigtranConfig;
    }

    private void updateSigtranLink(LinkConfig linkConfig) {
        //TODO: Make updating of link in SctpService
    }
}
