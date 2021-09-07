package com.unifun.sigproxy.controller.config;

import com.unifun.sigproxy.controller.dto.CreatorDataObject;
import com.unifun.sigproxy.controller.dto.sctp.SctpClientAssociationConfigDto;
import com.unifun.sigproxy.controller.dto.sctp.SctpServerAssociationConfigDto;
import com.unifun.sigproxy.controller.dto.sctp.SctpServerConfigDto;
import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.models.config.sctp.SctpClientAssociationConfig;
import com.unifun.sigproxy.models.config.sctp.SctpServerAssociationConfig;
import com.unifun.sigproxy.models.config.sctp.SctpServerConfig;
import com.unifun.sigproxy.service.SigtranConfigService;
import com.unifun.sigproxy.service.sctp.SctpConfigService;
import com.unifun.sigproxy.service.sctp.SctpService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("conf/sctp")
@RequiredArgsConstructor
public class SctpController {

    private final CreatorDataObject creatorDataObject;
    private final SigtranConfigService sigtranConfigService;
    private final SctpConfigService sctpConfigService;
    private final SctpService sctpService;

    @GetMapping(value = "/getClientLinks", produces = "application/json")
    @ResponseBody
    public List<SctpClientAssociationConfigDto> getLinksInfo(@RequestParam Long stackId) {

        return sigtranConfigService.getClientLinksByStackId(stackId).stream()
                .map(this.creatorDataObject::createSctpClientAssociationConfigDto)
                .collect(Collectors.toList());
    }

    @PostMapping(value = "/addClientLink", produces = "application/json")
    @ResponseBody
    public SctpClientAssociationConfigDto addClientLinkAsscociation(@RequestParam long stackId,
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

        SigtranStack sigtranStack = sigtranConfigService.getSigtranStackById(stackId);
        sctpClientAssociationConfig.setSigtranStack(sigtranStack);
        sctpService.addLink(sctpClientAssociationConfig, sigtranStack.getStackName());
        sctpConfigService.addClinetLink(sctpClientAssociationConfig);

        return creatorDataObject.createSctpClientAssociationConfigDto(sctpClientAssociationConfig);
    }

    @PostMapping(value = "/removeClientLink", produces = "application/json")
    @ResponseBody
    public SctpClientAssociationConfigDto removeClientLinkAsscociation(@RequestParam Long clientLinkId) throws NotFoundException {

        SctpClientAssociationConfig sctpClientAssociationConfig = sctpConfigService.getClientLinkById(clientLinkId);
        sctpService.removeSctpLink(sctpClientAssociationConfig, sctpClientAssociationConfig.getSigtranStack().getStackName());
        sctpConfigService.removeClientLinkById(clientLinkId);
        return creatorDataObject.createSctpClientAssociationConfigDto(sctpClientAssociationConfig);
    }

    @PostMapping(value = "/startClientLink", produces = "application/json")
    @ResponseBody
    public SctpClientAssociationConfigDto startLink(@RequestParam Long clientLinkId) throws NotFoundException {

        SctpClientAssociationConfig sctpClientAssociationConfig = sctpConfigService.getClientLinkById(clientLinkId);
        sctpService.startLink(sctpClientAssociationConfig.getLinkName(), sctpClientAssociationConfig.getSigtranStack().getStackName());
        return creatorDataObject.createSctpClientAssociationConfigDto(sctpClientAssociationConfig);
    }


    @PostMapping(value = "/stopClientLink", produces = "application/json")
    @ResponseBody
    public SctpClientAssociationConfigDto stopLink(@RequestParam Long clientLinkId) throws NotFoundException {

        SctpClientAssociationConfig sctpClientAssociationConfig = sctpConfigService.getClientLinkById(clientLinkId);
        sctpService.stopLink(sctpClientAssociationConfig.getLinkName(), sctpClientAssociationConfig.getSigtranStack().getStackName());
        return creatorDataObject.createSctpClientAssociationConfigDto(sctpClientAssociationConfig);
    }

    @GetMapping(value = "/getServerLinks", produces = "application/json")
    @ResponseBody
    public List<SctpServerAssociationConfigDto> srverLinks(@RequestParam Long serverId) {

        Set<SctpServerAssociationConfig> sctpServerAssociationConfigs = sctpConfigService.getServerLinksBySctpServerId(serverId);

        return sctpServerAssociationConfigs
                .stream()
                .map(this.creatorDataObject::createSctpServerAssociationConfigDto)
                .collect(Collectors.toList());
    }

    @PostMapping(value = "/addServerLink", produces = "application/json")
    @ResponseBody
    public SctpServerAssociationConfigDto addServerLinkAsscociation(@RequestParam Long serverId,
                                                                    @RequestParam String linkName,
                                                                    @RequestParam String remoteAddress,
                                                                    @RequestParam int remotePort) throws NotFoundException {

        SctpServerAssociationConfig sctpServerAssociationConfig = new SctpServerAssociationConfig();
        sctpServerAssociationConfig.setLinkName(linkName);
        sctpServerAssociationConfig.setRemotePort(remotePort);
        sctpServerAssociationConfig.setRemoteAddress(remoteAddress);

        sctpServerAssociationConfig.setSctpServerConfig(sctpConfigService.getSctpServerById(serverId));

        sctpConfigService.setServerLink(sctpServerAssociationConfig);
//        sctpService.addServerAssociation(serverAssociation,serverAssociation.getSctpServer().getSigtranStack().getStackName());
        return creatorDataObject.createSctpServerAssociationConfigDto(sctpServerAssociationConfig);
    }

    @PostMapping(value = "/removeServerLink", produces = "application/json")
    @ResponseBody
    public SctpServerAssociationConfig deleteServerLink(@RequestParam Long serverLinkId) {

        return sctpConfigService.removeServerLinkById(serverLinkId);
    }

    @PostMapping(value = "/startServer", produces = "application/json")
    public void startServer(@RequestParam Long serverId) throws NotFoundException {

        SctpServerConfig sctpServer;
        sctpServer = sctpConfigService.getSctpServerById(serverId);

        sctpService.startServer(sctpServer.getName(), sctpServer.getSigtranStack().getStackName());
    }

    @PostMapping(value = "/stopServer", produces = "application/json")
    public void stopServer(@RequestParam Long serverId) throws NotFoundException {

        SctpServerConfig sctpServer = sctpConfigService.getSctpServerById(serverId);
        sctpService.stopServer(sctpServer.getName(), sctpServer.getSigtranStack().getStackName());
    }

    @PostMapping(value = "/stopSigtranStack", produces = "application/json")
    public void stopSigtranStack(@RequestParam Long sigtranStackId) {

        SigtranStack sigtranStack = sigtranConfigService.getSigtranStackById(sigtranStackId);
        sctpService.stopStack(sigtranStack.getStackName());
    }

    @PostMapping(value = "/addSctpServer", produces = "application/json")
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
//      sctpServer.setMultihomingAddresses(multihomingAddresses);
        sctpServerConfig.setName(serverName);
        sctpServerConfig.setSigtranStack(sigtranConfigService.getSigtranStackById(stackId));
        sctpConfigService.setSctpServer(sctpServerConfig);


    }

    @GetMapping(value = "/getServers", produces = "application/json")
    @ResponseBody
    public List<SctpServerConfigDto> getServerList(@RequestParam Long stackId) throws NotFoundException {

        return sigtranConfigService.getSctpServersByStackId(stackId).stream()
                .map(this.creatorDataObject::createSctpServerConfigDto)
                .collect(Collectors.toList());
    }
}