package com.unifun.sigproxy.controller.config;

import com.unifun.sigproxy.controller.dto.ClientAssociationDto;
import com.unifun.sigproxy.controller.dto.ServerAssociationDto;
import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.models.config.sctp.SctpClientAssociationConfig;
import com.unifun.sigproxy.models.config.sctp.SctpServerAssociationConfig;
import com.unifun.sigproxy.service.sctp.SctpConfigService;
import com.unifun.sigproxy.service.sctp.SctpService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.mobicents.protocols.api.Association;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("sctp/conf")
@RequiredArgsConstructor
public class SctpController {

    private final SctpConfigService sctpConfigService;
    private final SctpService sctpService;

    @PostMapping(value = "/clientLinks", produces = "application/json")
    @ResponseBody
    public List<ClientAssociationDto> getLinksInfo(@RequestParam Long stackId) {
        return sctpConfigService.getClientLinksByStackId(stackId).stream()
                .map(clientAssociation -> {
                    ClientAssociationDto clientAssociationDto = new ClientAssociationDto();
                    clientAssociationDto.setId(clientAssociation.getId());
                    clientAssociationDto.setLinkName(clientAssociation.getLinkName());
                    clientAssociationDto.setLocalAddress(clientAssociation.getLocalAddress());
                    clientAssociationDto.setLocalPort(clientAssociation.getLocalPort());
                    clientAssociationDto.setMultihomingAddresses(clientAssociation.getMultihomingAddresses());
                    clientAssociationDto.setRemoteAddress(clientAssociation.getRemoteAddress());
                    clientAssociationDto.setRemotePort(clientAssociation.getRemotePort());
                    try {
                        Association association = sctpService
                                .getTransportManagement(clientAssociation.getSigtranStack().getStackName())
                                .getAssociation(clientAssociation.getLinkName());
                        clientAssociationDto.setStatus(association.isConnected());
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    return clientAssociationDto;
                })
                .collect(Collectors.toList());

    }

    @PostMapping(value = "/newClientLink", produces = "application/json")
    @ResponseBody
    public String addClientLinkAsscociation(@RequestParam long stackId,
                                            @RequestParam String linkName,
                                            @RequestParam String remoteAddress,
                                            @RequestParam int remotePort,
                                            @RequestParam String localAddress,
                                            @RequestParam int localPort) {
        SctpClientAssociationConfig sctpClientAssociationConfig = new SctpClientAssociationConfig();
        sctpClientAssociationConfig.setLinkName(linkName);
        sctpClientAssociationConfig.setLocalAddress(localAddress);
        sctpClientAssociationConfig.setLocalPort(localPort);
        sctpClientAssociationConfig.setRemoteAddress(remoteAddress);
        sctpClientAssociationConfig.setRemotePort(remotePort);
        SigtranStack sigtranStack;
        try {
            sigtranStack = sctpConfigService.getSigtranStackById(stackId);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Server Not Found", e);
        }
        sctpClientAssociationConfig.setSigtranStack(sigtranStack);
        sctpConfigService.setClinetLink(sctpClientAssociationConfig);
        sctpService.addLink(sctpClientAssociationConfig, sigtranStack.getStackName());

        return "testpost";
    }


    @PostMapping(value = "/removeClientLink", produces = "application/json")
    public void removeClientLinkAsscociation(@RequestParam Long id) throws Exception {
        sctpConfigService.deleteSctpLinkById(id);

    }

    @PostMapping(value = "/startLink", produces = "application/json")
    public void startLink(@RequestParam Long clientLinkId) {
        SctpClientAssociationConfig sctpClientAssociationConfig;
        try {
            sctpClientAssociationConfig = sctpConfigService.getClientLinksById(clientLinkId);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Link Not Found", e);
        }
        sctpService.startLink(sctpClientAssociationConfig.getLinkName(), sctpClientAssociationConfig.getSigtranStack().getStackName());
    }


    @PostMapping(value = "/stopLink", produces = "application/json")
    public void stopLink(@RequestParam Long clientLinkId) {
        SctpClientAssociationConfig sctpClientAssociationConfig;
        try {
            sctpClientAssociationConfig = sctpConfigService.getClientLinksById(clientLinkId);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Link Not Found", e);
        }
        sctpService.stopLink(sctpClientAssociationConfig.getLinkName(), sctpClientAssociationConfig.getSigtranStack().getStackName());
    }

    @PostMapping(value = "/serverLinks", produces = "application/json")
    @ResponseBody
    public List<ServerAssociationDto> addNewServerLink(@RequestParam Long serverId) {

        Set<SctpServerAssociationConfig> sctpServerAssociationConfigs = sctpConfigService.getServerLinksBySctpServerId(serverId);
        if (sctpServerAssociationConfigs.isEmpty()) throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Server Not Found");
        return sctpServerAssociationConfigs.stream()
                .map(serverAssociation -> {
                    ServerAssociationDto serverAssociationDto = new ServerAssociationDto();
                    serverAssociationDto.setId(serverAssociation.getId());
                    serverAssociationDto.setLinkName(serverAssociation.getLinkName());
                    serverAssociationDto.setRemoteAddress(serverAssociation.getRemoteAddress());
                    serverAssociationDto.setRemotePort(serverAssociation.getRemotePort());
                    return serverAssociationDto;
                })
                .collect(Collectors.toList());
    }

    @PostMapping(value = "/newServerLink", produces = "application/json")
    @ResponseBody
    public String addServerLinkAsscociation(@RequestParam Long serverId,
                                            @RequestParam String linkName,
                                            @RequestParam String remoteAddress,
                                            @RequestParam int remotePort) {
        SctpServerAssociationConfig sctpServerAssociationConfig = new SctpServerAssociationConfig();
        sctpServerAssociationConfig.setLinkName(linkName);
        sctpServerAssociationConfig.setRemotePort(remotePort);
        sctpServerAssociationConfig.setRemoteAddress(remoteAddress);
        try {
            sctpServerAssociationConfig.setSctpServerConfig(sctpConfigService.getSctpServerById(serverId));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Server Not Found", e);
        }
        sctpConfigService.setServerLink(sctpServerAssociationConfig);
//        sctpService.addServerAssociation(serverAssociation,serverAssociation.getSctpServer().getSigtranStack().getStackName());
        return "OK";
    }

    @PostMapping(value = "/startServer", produces = "application/json")
    public void startServer(@RequestParam String serverName, @RequestParam String sigtranStack) {
        sctpService.startServer(serverName, sigtranStack);
    }

    @PostMapping(value = "/stopServer", produces = "application/json")
    public void stopServer(@RequestParam String serverName, @RequestParam String sigtranStack) {
        sctpService.stopServer(serverName, sigtranStack);
    }

    @PostMapping(value = "/stopSigtranStack", produces = "application/json")
    public void stopSigtranStack(@RequestParam String sigtranStack) {
        sctpService.stopStack(sigtranStack);
    }


}
