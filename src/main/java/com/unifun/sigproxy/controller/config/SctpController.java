package com.unifun.sigproxy.controller.config;

import com.unifun.sigproxy.controller.dto.SctpClientAssociationConfigDto;
import com.unifun.sigproxy.controller.dto.SctpServerAssociationConfigDto;
import com.unifun.sigproxy.controller.dto.SctpServerConfigDto;
import com.unifun.sigproxy.exception.SS7NotContentException;
import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.models.config.sctp.SctpClientAssociationConfig;
import com.unifun.sigproxy.models.config.sctp.SctpServerAssociationConfig;
import com.unifun.sigproxy.service.SigtranConfigService;
import com.unifun.sigproxy.models.config.sctp.SctpServerConfig;
import com.unifun.sigproxy.service.sctp.SctpConfigService;
import com.unifun.sigproxy.service.sctp.SctpService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("conf/sctp")
@RequiredArgsConstructor
public class SctpController {

    private final SigtranConfigService sigtranConfigService;
    private final SctpConfigService sctpConfigService;
    private final SctpService sctpService;

    @GetMapping(value = "/clientLinks", produces = "application/json")
    @ResponseBody
    public List<SctpClientAssociationConfigDto> getLinksInfo(@RequestParam Long stackId) throws NotFoundException {

        List<SctpClientAssociationConfigDto> sctpClientAssociationConfig = sigtranConfigService.getClientLinksByStackId(stackId).stream()
                .map(this::getSctpClientAssociationConfigDto)
                .collect(Collectors.toList());

        if (sctpClientAssociationConfig.isEmpty())
            throw new SS7NotContentException("Not found Client Links with stack id " + stackId);
        return sctpClientAssociationConfig;

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
            sigtranStack = sigtranConfigService.getSigtranStackById(stackId);
        } catch (NotFoundException e) {
            throw new SS7NotContentException(e);
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
            throw new SS7NotContentException(e);
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
            throw new SS7NotContentException(e);
        }
        sctpService.startLink(sctpClientAssociationConfig.getLinkName(), sctpClientAssociationConfig.getSigtranStack().getStackName());
    }


    @PostMapping(value = "/stopLink", produces = "application/json")
    public void stopLink(@RequestParam Long clientLinkId) {

        SctpClientAssociationConfig sctpClientAssociationConfig;
        try {
            sctpClientAssociationConfig = sctpConfigService.getClientLinkById(clientLinkId);
        } catch (NotFoundException e) {
            throw new SS7NotContentException(e);
        }
        sctpService.stopLink(sctpClientAssociationConfig.getLinkName(), sctpClientAssociationConfig.getSigtranStack().getStackName());
    }

    @GetMapping(value = "/serverLinks", produces = "application/json")
    @ResponseBody
    public List<SctpServerAssociationConfigDto> srverLinks(@RequestParam Long serverId) {
        return getSctpServerAssociationConfigDto(serverId);
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
            throw new SS7NotContentException(e);
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

        SctpServerConfig sctpServer;
        try {

            sctpServer = sctpConfigService.getSctpServerById(serverId);
        } catch (NotFoundException e) {
            throw new SS7NotContentException(e);
        }
        sctpService.startServer(sctpServer.getName(), sctpServer.getSigtranStack().getStackName());
    }

    @PostMapping(value = "/stopServer", produces = "application/json")
    public void stopServer(@RequestParam Long serverId) {


        SctpServerConfig sctpServer;
        try {
            sctpServer = sctpConfigService.getSctpServerById(serverId);
        } catch (NotFoundException e) {
            throw new SS7NotContentException(e);
        }
        sctpService.stopServer(sctpServer.getName(), sctpServer.getSigtranStack().getStackName());
    }

    @PostMapping(value = "/stopSigtranStack", produces = "application/json")
    public void stopSigtranStack(@RequestParam Long sigtranStackId) {
        SigtranStack sigtranStack;
        try {
            sigtranStack = sigtranConfigService.getSigtranStackById(sigtranStackId);
        } catch (NotFoundException e) {
            throw new SS7NotContentException(e);
        }

        sctpService.stopStack(sigtranStack.getStackName());
    }

    @PostMapping(value = "/newSctpServer", produces = "application/json")
    public void addSctpServer(@RequestParam Long stackId,
                              @RequestParam String serverName,
                              @RequestParam String localAddres,
                              @RequestParam int localPort
//                              @RequestParam String[] multihomingAddresses,
//                              @RequestBody Set<SctpServerAssociationConfigDto> sctpServerAssociationConfigDto
    ) {

        SctpServerConfig sctpServerConfig = new SctpServerConfig();
        sctpServerConfig.setLocalAddress(localAddres);
        sctpServerConfig.setLocalPort(localPort);
//        sctpServer.setMultihomingAddresses(multihomingAddresses);
        sctpServerConfig.setName(serverName);
        try {
            sctpServerConfig.setSigtranStack(sigtranConfigService.getSigtranStackById(stackId));
        } catch (NotFoundException e) {
            throw new SS7NotContentException(e);
        }

        sctpConfigService.setSctpServer(sctpServerConfig);


    }

    @GetMapping(value = "servers", produces = "application/json")
    @ResponseBody
    public List<SctpServerConfigDto> getServerList(@RequestParam Long stackId) {

        try {
            return sigtranConfigService.getSctpServersByStackId(stackId).stream()
                    .map(this::getSctpServerConfigDto)
                    .collect(Collectors.toList());
        } catch (NotFoundException e) {
            throw new SS7NotContentException("Not found sigtran stack with id " + stackId);
        }
    }

    private SctpClientAssociationConfigDto getSctpClientAssociationConfigDto(SctpClientAssociationConfig clientAssociation) {
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
    }

    private SctpServerConfigDto getSctpServerConfigDto(SctpServerConfig sctpServerConfig) {
        SctpServerConfigDto sctpServerConfigDto = new SctpServerConfigDto();
        sctpServerConfigDto.setLocalAddress(sctpServerConfig.getLocalAddress());
        sctpServerConfigDto.setLocalPort(sctpServerConfig.getLocalPort());
        sctpServerConfigDto.setMultihomingAddresses(sctpServerConfig.getMultihomingAddresses());
        sctpServerConfigDto.setName(sctpServerConfig.getName());
        sctpServerConfigDto.setSctpServerAssociationConfigs(getSctpServerAssociationConfigDto(sctpServerConfig.getId()));
        sctpServerConfigDto.setId(sctpServerConfig.getId());

        return sctpServerConfigDto;
    }

    private List<SctpServerAssociationConfigDto> getSctpServerAssociationConfigDto(long serverId) {

        Set<SctpServerAssociationConfig> sctpServerAssociationConfigs;
        try {
            sctpServerAssociationConfigs = sctpConfigService.getServerLinksBySctpServerId(serverId);
        } catch (NotFoundException e) {
            throw new SS7NotContentException(e);
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
                    try {
                        sctpServerAssociationConfigDto.setStatus(sctpService.getTransportManagement(serverAssociation.getSctpServerConfig().getSigtranStack().getStackName())
                                .getAssociation(serverAssociation.getLinkName()).isConnected());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return sctpServerAssociationConfigDto;
                })
                .collect(Collectors.toList());
    }
}