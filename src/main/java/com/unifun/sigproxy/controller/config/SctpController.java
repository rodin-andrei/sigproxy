package com.unifun.sigproxy.controller.config;

import com.unifun.sigproxy.controller.dto.SctpClientAssociationConfigDto;
import com.unifun.sigproxy.controller.dto.SctpServerAssociationConfigDto;
import com.unifun.sigproxy.exception.MyResourceNotFoundException;
import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.models.config.sctp.SctpClientAssociationConfig;
import com.unifun.sigproxy.models.config.sctp.SctpServer;
import com.unifun.sigproxy.models.config.sctp.SctpServerAssociationConfig;
import com.unifun.sigproxy.service.sctp.SctpConfigService;
import com.unifun.sigproxy.service.sctp.SctpService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
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
    public List<SctpClientAssociationConfigDto> getLinksInfo(@RequestParam Long stackId) {
        try {
            return sctpConfigService.getClientLinksByStackId(stackId).stream().map(clientAssociation -> {
                SctpClientAssociationConfigDto sctpClientAssociationConfigDto = new SctpClientAssociationConfigDto();
                sctpClientAssociationConfigDto.setId(clientAssociation.getId());
                sctpClientAssociationConfigDto.setLinkName(clientAssociation.getLinkName());
                sctpClientAssociationConfigDto.setLocalAddress(clientAssociation.getLocalAddress());
                sctpClientAssociationConfigDto.setLocalPort(clientAssociation.getLocalPort());
                sctpClientAssociationConfigDto.setMultihomingAddresses(clientAssociation.getMultihomingAddresses());
                sctpClientAssociationConfigDto.setRemoteAddress(clientAssociation.getRemoteAddress());
                sctpClientAssociationConfigDto.setRemotePort(clientAssociation.getRemotePort());
                try {
                    sctpClientAssociationConfigDto.setStatus(sctpService.getTransportManagement(clientAssociation.getSigtranStack().getStackName())
                            .getAssociation(clientAssociation.getLinkName()).isConnected());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return sctpClientAssociationConfigDto;
            }).collect(Collectors.toList());
        } catch (NotFoundException e) {
            throw new MyResourceNotFoundException(e);
        }
    }

    @PostMapping(value = "/newClientLink", produces = "application/json")
    @ResponseBody
    public void addClientLinkAsscociation(@RequestParam long stackId,
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
            throw new MyResourceNotFoundException(e);
        }
        sctpClientAssociationConfig.setSigtranStack(sigtranStack);
        sctpConfigService.setClinetLink(sctpClientAssociationConfig);
        sctpService.addLink(sctpClientAssociationConfig, sigtranStack.getStackName());
    }


    @PostMapping(value = "/removeClientLink", produces = "application/json")
    public void removeClientLinkAsscociation(@RequestParam Long clientLinkId) {
        SctpClientAssociationConfig sctpClientAssociationConfig;
        try {
            sctpClientAssociationConfig = sctpConfigService.getClientLinkById(clientLinkId);
        } catch (NotFoundException e) {
            throw new MyResourceNotFoundException(e);
        }
        sctpService.removeSctpLink(sctpClientAssociationConfig, sctpClientAssociationConfig.getSigtranStack().getStackName());
        sctpConfigService.removeClientLinkById(clientLinkId);
    }

    @PostMapping(value = "/startLink", produces = "application/json")
    public void startLink(@RequestParam Long clientLinkId) {

        SctpClientAssociationConfig sctpClientAssociationConfig;
        try {
            sctpClientAssociationConfig = sctpConfigService.getClientLinkById(clientLinkId);
        } catch (NotFoundException e) {
            throw new MyResourceNotFoundException(e);
        }
        sctpService.startLink(sctpClientAssociationConfig.getLinkName(), sctpClientAssociationConfig.getSigtranStack().getStackName());
    }


    @PostMapping(value = "/stopLink", produces = "application/json")
    public void stopLink(@RequestParam Long clientLinkId) {

        SctpClientAssociationConfig sctpClientAssociationConfig;
        try {
            sctpClientAssociationConfig = sctpConfigService.getClientLinkById(clientLinkId);
        } catch (NotFoundException e) {
            throw new MyResourceNotFoundException(e);
        }
        sctpService.stopLink(sctpClientAssociationConfig.getLinkName(), sctpClientAssociationConfig.getSigtranStack().getStackName());
    }

    @PostMapping(value = "/serverLinks", produces = "application/json")
    @ResponseBody
    public List<SctpServerAssociationConfigDto> addNewServerLink(@RequestParam Long serverId) {

        Set<SctpServerAssociationConfig> sctpServerAssociationConfigs;
        try {
            sctpServerAssociationConfigs = sctpConfigService.getServerLinksBySctpServerId(serverId);
        } catch (NotFoundException e) {
            throw new MyResourceNotFoundException(e);
        }
        if (sctpServerAssociationConfigs.isEmpty()) throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Server Not Found");
        return sctpServerAssociationConfigs.stream()
                .map(serverAssociation -> {
                    SctpServerAssociationConfigDto sctpServerAssociationConfigDto = new SctpServerAssociationConfigDto();
                    sctpServerAssociationConfigDto.setId(serverAssociation.getId());
                    sctpServerAssociationConfigDto.setLinkName(serverAssociation.getLinkName());
                    sctpServerAssociationConfigDto.setRemoteAddress(serverAssociation.getRemoteAddress());
                    sctpServerAssociationConfigDto.setRemotePort(serverAssociation.getRemotePort());
                    return sctpServerAssociationConfigDto;
                })
                .collect(Collectors.toList());
    }

    @PostMapping(value = "/newServerLink", produces = "application/json")
    @ResponseBody
    public void addServerLinkAsscociation(@RequestParam Long serverId,
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
            throw new MyResourceNotFoundException(e);
        }
        sctpConfigService.setServerLink(sctpServerAssociationConfig);
//        sctpService.addServerAssociation(serverAssociation,serverAssociation.getSctpServer().getSigtranStack().getStackName());
    }

    @PostMapping(value = "/deleteServerLink", produces = "application/json")
    public void deleteServerLink(@RequestParam Long serverLinkId) {

        sctpConfigService.removeServerLinkById(serverLinkId);
    }

    @PostMapping(value = "/startServer", produces = "application/json")
    public void startServer(@RequestParam Long serverId) {

        SctpServer sctpServer;
        try {

            sctpServer = sctpConfigService.getSctpServerById(serverId);
        } catch (NotFoundException e) {
            throw new MyResourceNotFoundException(e);
        }
        sctpService.startServer(sctpServer.getName(), sctpServer.getSigtranStack().getStackName());
    }

    @PostMapping(value = "/stopServer", produces = "application/json")
    public void stopServer(@RequestParam Long serverId) {

        SctpServer sctpServer;
        try{
            sctpServer = sctpConfigService.getSctpServerById(serverId);
        } catch (NotFoundException e){
            throw new MyResourceNotFoundException(e);
        }
        sctpService.stopServer(sctpServer.getName(), sctpServer.getSigtranStack().getStackName());
    }

    @PostMapping(value = "/stopSigtranStack", produces = "application/json")
    public void stopSigtranStack(@RequestParam Long sigtranStackId) {
        SigtranStack sigtranStack;
        try{
          sigtranStack = sctpConfigService.getSigtranStackById(sigtranStackId);
        } catch (NotFoundException e){
            throw new MyResourceNotFoundException(e);
        }

        sctpService.stopStack(sigtranStack.getStackName());
    }
}