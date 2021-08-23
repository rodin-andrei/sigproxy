package com.unifun.sigproxy.controller.config;

import com.unifun.sigproxy.controller.dto.SctpClientAssociationConfigDto;
import com.unifun.sigproxy.controller.dto.SctpServerAssociationConfigDto;
import com.unifun.sigproxy.controller.dto.SctpServerConfigDto;
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

    private final SigtranConfigService sigtranConfigService;
    private final SctpConfigService sctpConfigService;
    private final SctpService sctpService;

    @GetMapping(value = "/getClientLinks", produces = "application/json")
    @ResponseBody
    public List<SctpClientAssociationConfigDto> getLinksInfo(@RequestParam Long stackId) throws NotFoundException {

        return sigtranConfigService.getClientLinksByStackId(stackId).stream()
                .map(this::getSctpClientAssociationConfigDto)
                .collect(Collectors.toList());
    }

    @PostMapping(value = "/addClientLink", produces = "application/json")
    @ResponseBody
    public SctpClientAssociationConfigDto addClientLinkAsscociation(@RequestParam long stackId,
                                                                    @RequestParam String linkName,
                                                                    @RequestParam String remoteAddress,
                                                                    @RequestParam int remotePort,
                                                                    @RequestParam String localAddress,
                                                                    @RequestParam int localPort) throws NotFoundException {

        SctpClientAssociationConfig sctpClientAssociationConfig = new SctpClientAssociationConfig();
        sctpClientAssociationConfig.setLinkName(linkName);
        sctpClientAssociationConfig.setLocalAddress(localAddress);
        sctpClientAssociationConfig.setLocalPort(localPort);
        sctpClientAssociationConfig.setRemoteAddress(remoteAddress);
        sctpClientAssociationConfig.setRemotePort(remotePort);
        SigtranStack sigtranStack;
        sigtranStack = sigtranConfigService.getSigtranStackById(stackId);

        sctpClientAssociationConfig.setSigtranStack(sigtranStack);
        sctpService.addLink(sctpClientAssociationConfig, sigtranStack.getStackName());
        sctpConfigService.addClinetLink(sctpClientAssociationConfig);

        return getSctpClientAssociationConfigDto(sctpClientAssociationConfig);
    }

    @PostMapping(value = "/removeClientLink", produces = "application/json")
    public void removeClientLinkAsscociation(@RequestParam Long clientLinkId) throws NotFoundException {
        SctpClientAssociationConfig sctpClientAssociationConfig = sctpConfigService.getClientLinkById(clientLinkId);
        sctpService.removeSctpLink(sctpClientAssociationConfig, sctpClientAssociationConfig.getSigtranStack().getStackName());
        sctpConfigService.removeClientLinkById(clientLinkId);
    }

    @PostMapping(value = "/startClientLink", produces = "application/json")
    public void startLink(@RequestParam Long clientLinkId) throws NotFoundException {

        SctpClientAssociationConfig sctpClientAssociationConfig = sctpConfigService.getClientLinkById(clientLinkId);
        sctpService.startLink(sctpClientAssociationConfig.getLinkName(), sctpClientAssociationConfig.getSigtranStack().getStackName());
    }


    @PostMapping(value = "/stopClientLink", produces = "application/json")
    public void stopLink(@RequestParam Long clientLinkId) throws NotFoundException {

        SctpClientAssociationConfig sctpClientAssociationConfig = sctpConfigService.getClientLinkById(clientLinkId);

        sctpService.stopLink(sctpClientAssociationConfig.getLinkName(), sctpClientAssociationConfig.getSigtranStack().getStackName());
    }

    @GetMapping(value = "/getServerLinks", produces = "application/json")
    @ResponseBody
    public List<SctpServerAssociationConfigDto> srverLinks(@RequestParam Long serverId) {
        Set<SctpServerAssociationConfig> sctpServerAssociationConfigs = sctpConfigService.getServerLinksBySctpServerId(serverId);
        return getSctpServerAssociationConfigDto(sctpServerAssociationConfigs);
    }

    @PostMapping(value = "/addServerLink", produces = "application/json")
    @ResponseBody
    public void addServerLinkAsscociation(@RequestParam Long serverId,
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
    ) throws NotFoundException {

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
                .map(this::getSctpServerConfigDto)
                .collect(Collectors.toList());
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

    private List<SctpServerAssociationConfigDto> getSctpServerAssociationConfigDto(Set<SctpServerAssociationConfig> sctpServerAssociationConfigs) {
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

    private SctpServerConfigDto getSctpServerConfigDto(SctpServerConfig sctpServerConfig) {
        SctpServerConfigDto sctpServerConfigDto = new SctpServerConfigDto();
        sctpServerConfigDto.setLocalAddress(sctpServerConfig.getLocalAddress());
        sctpServerConfigDto.setLocalPort(sctpServerConfig.getLocalPort());
        sctpServerConfigDto.setMultihomingAddresses(sctpServerConfig.getMultihomingAddresses());
        sctpServerConfigDto.setName(sctpServerConfig.getName());
        Set<SctpServerAssociationConfig> sctpServerAssociationConfigs = sctpConfigService.getServerLinksBySctpServerId(sctpServerConfig.getId());
        sctpServerConfigDto.setSctpServerAssociationConfigs(getSctpServerAssociationConfigDto(sctpServerAssociationConfigs));
        sctpServerConfigDto.setId(sctpServerConfig.getId());

        return sctpServerConfigDto;
    }
}