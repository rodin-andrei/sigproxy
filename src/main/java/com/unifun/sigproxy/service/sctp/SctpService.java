package com.unifun.sigproxy.service.sctp;

import com.unifun.sigproxy.exception.InitializingException;
import com.unifun.sigproxy.exception.NoConfigurationException;
import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.models.config.sctp.ClientAssociation;
import com.unifun.sigproxy.models.config.sctp.SctpServer;
import org.mobicents.protocols.api.Management;

import java.util.Set;

public interface SctpService {

    void initialize(SigtranStack sigtranStack) throws NoConfigurationException, InitializingException;

    void stopStack(String sigtranStack);

    Management getTransportManagement(String sigtranStack);

    void addLink(ClientAssociation link, String sigtranStack);

    void addLinks(Set<ClientAssociation> newLinks, String sigtranStack);

    void removeSctpLink(ClientAssociation link, String sigtranStack);

    void addServer(SctpServer serverConfig, String sigtranStack);

    void addServers(Set<SctpServer> newServers, String sigtranStack);

    void removeAllLinks(String sigtranStack);

    void startLink(String linkName, String sigtranStack);

    void stopLink(String linkName, String sigtranStack);

    void startServer(String serverName, String sigtranStack);

    void stopServer(String serverName, String sigtranStack);

    void removeServer(SctpServer serverConfig, String sigtranStack);

    void removeAllServers(String sigtranStack);
}
