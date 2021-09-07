package com.unifun.sigproxy.service.sctp;

import com.unifun.sigproxy.exception.InitializingException;
import com.unifun.sigproxy.exception.NoConfigurationException;
import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.models.config.sctp.SctpClientAssociationConfig;
import com.unifun.sigproxy.models.config.sctp.SctpServerAssociationConfig;
import com.unifun.sigproxy.models.config.sctp.SctpServerConfig;
import org.mobicents.protocols.api.Management;

import java.util.Set;

public interface SctpService {

    void initialize(SigtranStack sigtranStack) throws NoConfigurationException, InitializingException;

    void stopStack(String sigtranStack);

    Management getTransportManagement(String sigtranStack);

    void addLink(SctpClientAssociationConfig link, String sigtranStack);

    void addLinks(Set<SctpClientAssociationConfig> newLinks, String sigtranStack);

    void removeSctpLink(SctpClientAssociationConfig link, String sigtranStack);

    void addServerLink(SctpServerAssociationConfig serverLink, String sigtranStack);

    void addServer(SctpServerConfig serverConfig, String sigtranStack);

    void addServers(Set<SctpServerConfig> newServers, String sigtranStack);

    void removeAllLinks(String sigtranStack);

    void startLink(String linkName, String sigtranStack);

    void stopLink(String linkName, String sigtranStack);

    void startServer(String serverName, String sigtranStack);

    void stopServer(String serverName, String sigtranStack);

    void removeServer(SctpServerConfig serverConfig, String sigtranStack);

    void removeAllServers(String sigtranStack);
}
