package com.unifun.sigproxy.controller.config;

import com.unifun.sigproxy.controller.dto.service.CreatorDataAccessObjectService;
import com.unifun.sigproxy.controller.dto.service.CreatorDataObjectService;
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

    private final CreatorDataObjectService creatorDto;
    private final CreatorDataAccessObjectService creatorDao;
    private final SigtranConfigService sigtranConfigService;
    private final SctpConfigService sctpConfigService;
    private final SctpService sctpService;

    @GetMapping(value = "/getClientLinks", produces = "application/json")
    @ResponseBody
    public List<SctpClientAssociationConfigDto> getLinksInfo(@RequestParam Long stackId) {

        return sctpConfigService.getClientLinksByStackId(stackId).stream()
                .map(this.creatorDto::createSctpClientAssociationConfigDto)
                .collect(Collectors.toList());
    }

    @PostMapping(value = "/removeClientLink", produces = "application/json")
    @ResponseBody
    public SctpClientAssociationConfigDto removeClientLinkAsscociation(@RequestParam Long clientLinkId) {

        SctpClientAssociationConfig sctpClientAssociationConfig = sctpConfigService.getClientLinkById(clientLinkId);
        sctpService.removeSctpLink(sctpClientAssociationConfig, sctpClientAssociationConfig.getSigtranStack().getStackName());
        sctpConfigService.removeClientLinkById(clientLinkId);
        return creatorDto.createSctpClientAssociationConfigDto(sctpClientAssociationConfig);
    }

    @PostMapping(value = "/startClientLink", produces = "application/json")
    @ResponseBody
    public SctpClientAssociationConfigDto startClientLink(@RequestParam Long clientLinkId) {

        SctpClientAssociationConfig sctpClientAssociationConfig = sctpConfigService.getClientLinkById(clientLinkId);
        sctpService.startLink(sctpClientAssociationConfig.getLinkName(), sctpClientAssociationConfig.getSigtranStack().getStackName());
        return creatorDto.createSctpClientAssociationConfigDto(sctpClientAssociationConfig);
    }


    @PostMapping(value = "/stopClientLink", produces = "application/json")
    @ResponseBody
    public SctpClientAssociationConfigDto stopClientLink(@RequestParam Long clientLinkId) {

        SctpClientAssociationConfig sctpClientAssociationConfig = sctpConfigService.getClientLinkById(clientLinkId);
        sctpService.stopLink(sctpClientAssociationConfig.getLinkName(), sctpClientAssociationConfig.getSigtranStack().getStackName());
        return creatorDto.createSctpClientAssociationConfigDto(sctpClientAssociationConfig);
    }

    @GetMapping(value = "/getServerLinks", produces = "application/json")
    @ResponseBody
    public List<SctpServerAssociationConfigDto> srverLinks(@RequestParam Long serverId) {

        Set<SctpServerAssociationConfig> sctpServerAssociationConfigs = sctpConfigService.getServerLinksBySctpServerId(serverId);

        return sctpServerAssociationConfigs
                .stream()
                .map(this.creatorDto::createSctpServerAssociationConfigDto)
                .collect(Collectors.toList());
    }

    @PostMapping(value = "/removeServerLink", produces = "application/json")
    @ResponseBody
    public SctpServerAssociationConfig deleteServerLink(@RequestParam Long serverLinkId) {

        return sctpConfigService.removeServerLinkById(serverLinkId);
    }


    @PostMapping(value = "/startSrverLink", produces = "application/json")
    @ResponseBody
    public SctpServerAssociationConfigDto startSrverLink(@RequestParam Long serverLinkId) {

        SctpServerAssociationConfig sctpServerAssociationConfig = sctpConfigService.getServerLinkById(serverLinkId);
        sctpService.startLink(sctpServerAssociationConfig.getLinkName(), sctpServerAssociationConfig.getSctpServerConfig().getSigtranStack().getStackName());
        return creatorDto.createSctpServerAssociationConfigDto(sctpServerAssociationConfig);
    }


    @PostMapping(value = "/stopServerLink", produces = "application/json")
    @ResponseBody
    public SctpServerAssociationConfigDto stopServerLink(@RequestParam Long serverLinkId) {

        SctpServerAssociationConfig sctpServerAssociationConfig = sctpConfigService.getServerLinkById(serverLinkId);
        sctpService.stopLink(sctpServerAssociationConfig.getLinkName(), sctpServerAssociationConfig.getSctpServerConfig().getSigtranStack().getStackName());
        return creatorDto.createSctpServerAssociationConfigDto(sctpServerAssociationConfig);
    }


    @PostMapping(value = "/startServer", produces = "application/json")
    public void startServer(@RequestParam Long serverId) {

        SctpServerConfig sctpServer;
        sctpServer = sctpConfigService.getSctpServerById(serverId);
        sctpService.startServer(sctpServer.getName(), sctpServer.getSigtranStack().getStackName());
    }

    @PostMapping(value = "/stopServer", produces = "application/json")
    public void stopServer(@RequestParam Long serverId) {

        SctpServerConfig sctpServer = sctpConfigService.getSctpServerById(serverId);
        sctpService.stopServer(sctpServer.getName(), sctpServer.getSigtranStack().getStackName());
    }

    @PostMapping(value = "/stopSigtranStack", produces = "application/json")
    public void stopSigtranStack(@RequestParam Long sigtranStackId) {

        SigtranStack sigtranStack = sigtranConfigService.getSigtranStackById(sigtranStackId);
        sctpService.stopStack(sigtranStack.getStackName());
    }

    @GetMapping(value = "/getServers", produces = "application/json")
    @ResponseBody
    public List<SctpServerConfigDto> getServerList(@RequestParam Long stackId) {

        return sctpConfigService.getSctpServersByStackId(stackId).stream()
                .map(this.creatorDto::createSctpServerConfigDto)
                .collect(Collectors.toList());
    }

    @PostMapping(value = "/addSctpServer", produces = "application/json")
    @ResponseBody
    public SctpServerConfigDto addSctpServer(@RequestBody SctpServerConfigDto sctpServerConfigDto,
                                             @RequestParam Long sigtranStackId) {

        SigtranStack sigtranStack = sigtranConfigService.getSigtranStackById(sigtranStackId);
        SctpServerConfig sctpServerConfig = creatorDao.createSctpServerDao(sctpServerConfigDto, sigtranStack);
        sctpConfigService.addSctpServer(sctpServerConfig);
        return creatorDto.createSctpServerConfigDto(sctpServerConfig);
    }

    @PostMapping(value = "/addServerLink", produces = "application/json")
    @ResponseBody
    public SctpServerAssociationConfigDto addServerLinkAsscociation(@RequestParam Long serverId,
                                                                    @RequestBody SctpServerAssociationConfigDto sctpserverAssConfig) {

        SctpServerConfig sctpServerConfig = sctpConfigService.getSctpServerById(serverId);
        SctpServerAssociationConfig sctpServerAssociationConfig = creatorDao.createSctpServerAssociationConfigDao(sctpserverAssConfig, sctpServerConfig);
        sctpConfigService.addServerLink(sctpServerAssociationConfig);
        sctpService.addServerLink(sctpServerAssociationConfig, sctpServerAssociationConfig.getSctpServerConfig().getSigtranStack().getStackName());
        return creatorDto.createSctpServerAssociationConfigDto(sctpServerAssociationConfig);
    }

    @PostMapping(value = "/addClientLink", produces = "application/json")
    @ResponseBody
    public SctpClientAssociationConfigDto addClientLinkAsscociation(@RequestParam Long stackId,
                                                                    @RequestBody SctpClientAssociationConfigDto sctpClient) {

        SigtranStack sigtranStack = sigtranConfigService.getSigtranStackById(stackId);
        SctpClientAssociationConfig sctpClientAssociationConfig = creatorDao.createSctpClientAssociationConfigDao(sctpClient, sigtranStack);
        sctpConfigService.addClinetLink(sctpClientAssociationConfig);
        sctpService.addLink(sctpClientAssociationConfig, sigtranStack.getStackName());
        return creatorDto.createSctpClientAssociationConfigDto(sctpClientAssociationConfig);
    }
}