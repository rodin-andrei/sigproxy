package com.unifun.sigproxy.controller.config;

import com.unifun.sigproxy.controller.dto.SigtranStackDto;
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

    @GetMapping(value = "/getClientAssociationConfig", produces = "application/json")
    @ResponseBody
    public List<SctpClientAssociationConfigDto> getClientAssociationConfig(@RequestParam Long stackId) {

        return sctpConfigService.getSctpClientAssociationConfigByStackId(stackId).stream()
                .map(this.creatorDto::createSctpClientAssociationConfigDto)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/getServerAssociationConfig", produces = "application/json")
    @ResponseBody
    public List<SctpServerAssociationConfigDto> getServerAssociationConfig(@RequestParam Long serverId) {

        Set<SctpServerAssociationConfig> sctpServerAssociationConfigs = sctpConfigService.getSctpServerAssociationConfigBySctpServerConfigId(serverId);

        return sctpServerAssociationConfigs
                .stream()
                .map(this.creatorDto::createSctpServerAssociationConfigDto)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/getServerConfig", produces = "application/json")
    @ResponseBody
    public Set<SctpServerConfigDto> getServerConfig(@RequestParam Long stackId) {

        return sctpConfigService.getSctpServerConfigByStackId(stackId).stream()
                .map(this.creatorDto::createSctpServerConfigDto)
                .collect(Collectors.toSet());
    }

    @DeleteMapping(value = "/removeClientAssociationConfig", produces = "application/json")
    @ResponseBody
    public SctpClientAssociationConfigDto removeClientAssociationConfig(@RequestParam Long clientLinkId) {

        SctpClientAssociationConfig sctpClientAssociationConfig = sctpConfigService.getSctpClientAssociationConfigById(clientLinkId);
        sctpService.removeSctpLink(sctpClientAssociationConfig, sctpClientAssociationConfig.getSigtranStack().getStackName());
        sctpConfigService.removeSctpClientAssociationConfigById(clientLinkId);
        return creatorDto.createSctpClientAssociationConfigDto(sctpClientAssociationConfig);
    }

    @DeleteMapping(value = "/removeServerAssociationConfig", produces = "application/json")
    @ResponseBody
    public SctpServerAssociationConfigDto removeServerAssociationConfig(@RequestParam Long serverLinkId) {
        SctpServerAssociationConfig sctpServerAssociationConfig = sctpConfigService.getSctpServerAssociationConfigById(serverLinkId);
        sctpConfigService.removeSctpServerAssociationConfigById(serverLinkId);
        return creatorDto.createSctpServerAssociationConfigDto(sctpServerAssociationConfig);
    }

    @DeleteMapping(value = "/removeServerConfig", produces = "application/json")
    @ResponseBody
    public SctpServerConfigDto removeServerConfig(@RequestParam Long serverConfigId) {
        SctpServerConfig sctpServerConfig = sctpConfigService.getSctpServerConfigById(serverConfigId);
        sctpConfigService.removeSctpServerConfigById(serverConfigId);
        return creatorDto.createSctpServerConfigDto(sctpServerConfig);
    }

    @PostMapping(value = "/startClientAssociationConfig", produces = "application/json")
    @ResponseBody
    public SctpClientAssociationConfigDto startClientAssociationConfig(@RequestParam Long clientLinkId) {

        SctpClientAssociationConfig sctpClientAssociationConfig = sctpConfigService.getSctpClientAssociationConfigById(clientLinkId);
        sctpService.startLink(sctpClientAssociationConfig.getLinkName(), sctpClientAssociationConfig.getSigtranStack().getStackName());
        return creatorDto.createSctpClientAssociationConfigDto(sctpClientAssociationConfig);
    }


    @PostMapping(value = "/stopClientAssociationConfig", produces = "application/json")
    @ResponseBody
    public SctpClientAssociationConfigDto stopClientAssociationConfig(@RequestParam Long clientLinkId) {

        SctpClientAssociationConfig sctpClientAssociationConfig = sctpConfigService.getSctpClientAssociationConfigById(clientLinkId);
        sctpService.stopLink(sctpClientAssociationConfig.getLinkName(), sctpClientAssociationConfig.getSigtranStack().getStackName());
        return creatorDto.createSctpClientAssociationConfigDto(sctpClientAssociationConfig);
    }

    @PostMapping(value = "/startServerAssociationConfig", produces = "application/json")
    @ResponseBody
    public SctpServerAssociationConfigDto startServerAssociationConfig(@RequestParam Long serverLinkId) {

        SctpServerAssociationConfig sctpServerAssociationConfig = sctpConfigService.getSctpServerAssociationConfigById(serverLinkId);
        sctpService.startLink(sctpServerAssociationConfig.getLinkName(), sctpServerAssociationConfig.getSctpServerConfig().getSigtranStack().getStackName());
        return creatorDto.createSctpServerAssociationConfigDto(sctpServerAssociationConfig);
    }

    @PostMapping(value = "/stopServerAssociationConfig", produces = "application/json")
    @ResponseBody
    public SctpServerAssociationConfigDto stopServerAssociationConfig(@RequestParam Long serverLinkId) {

        SctpServerAssociationConfig sctpServerAssociationConfig = sctpConfigService.getSctpServerAssociationConfigById(serverLinkId);
        sctpService.stopLink(sctpServerAssociationConfig.getLinkName(), sctpServerAssociationConfig.getSctpServerConfig().getSigtranStack().getStackName());
        return creatorDto.createSctpServerAssociationConfigDto(sctpServerAssociationConfig);
    }

    @PostMapping(value = "/startServerConfig", produces = "application/json")
    public SctpServerConfigDto startServerConfig(@RequestParam Long serverId) {

        SctpServerConfig sctpServer = sctpConfigService.getSctpServerConfigById(serverId);
        sctpService.startServer(sctpServer.getName(), sctpServer.getSigtranStack().getStackName());
        return creatorDto.createSctpServerConfigDto(sctpServer);
    }

    @PostMapping(value = "/stopServerConfig", produces = "application/json")
    public SctpServerConfigDto stopServerConfi(@RequestParam Long serverId) {

        SctpServerConfig sctpServer = sctpConfigService.getSctpServerConfigById(serverId);
        sctpService.stopServer(sctpServer.getName(), sctpServer.getSigtranStack().getStackName());
        return creatorDto.createSctpServerConfigDto(sctpServer);
    }

    @PostMapping(value = "/stopSigtranStack", produces = "application/json")
    public SigtranStackDto stopSigtranStack(@RequestParam Long sigtranStackId) {

        SigtranStack sigtranStack = sigtranConfigService.getSigtranStackById(sigtranStackId);
        sctpService.stopStack(sigtranStack.getStackName());
        return creatorDto.createSigtranStackDto(sigtranStack);
    }

    @PostMapping(value = "/addClientAssociationConfig", produces = "application/json")
    @ResponseBody
    public SctpClientAssociationConfigDto addClientAssociationConfig(@RequestParam Long stackId,
                                                                    @RequestBody SctpClientAssociationConfigDto sctpClientDto) {

        SigtranStack sigtranStack = sigtranConfigService.getSigtranStackById(stackId);
        SctpClientAssociationConfig sctpClientAssociationConfig = creatorDao.createSctpClientAssociationConfigDao(sctpClientDto, sigtranStack);
        sctpService.addLink(sctpClientAssociationConfig, sigtranStack.getStackName());
        sctpConfigService.addSctpClientAssociationConfig(sctpClientAssociationConfig);
        return creatorDto.createSctpClientAssociationConfigDto(sctpClientAssociationConfig);
    }

    @PostMapping(value = "/addServerAssociationConfig", produces = "application/json")
    @ResponseBody
    public SctpServerAssociationConfigDto addServerAssociationConfig(@RequestParam Long serverId,
                                                                     @RequestBody SctpServerAssociationConfigDto sctpserverAssConfigDto) {

        SctpServerConfig sctpServerConfig = sctpConfigService.getSctpServerConfigById(serverId);
        SctpServerAssociationConfig sctpServerAssociationConfig = creatorDao.createSctpServerAssociationConfigDao(sctpserverAssConfigDto, sctpServerConfig);
        sctpServerAssociationConfig = sctpConfigService.addSctpServerAssociationConfig(sctpServerAssociationConfig);
        sctpService.addServerLink(sctpServerAssociationConfig, sctpServerAssociationConfig.getSctpServerConfig().getSigtranStack().getStackName());
        return creatorDto.createSctpServerAssociationConfigDto(sctpServerAssociationConfig);
    }

    @PostMapping(value = "/addServerConfig", produces = "application/json")
    @ResponseBody
    public SctpServerConfigDto addServerConfig(@RequestBody SctpServerConfigDto sctpServerConfigDto,
                                               @RequestParam Long sigtranStackId) {

        SigtranStack sigtranStack = sigtranConfigService.getSigtranStackById(sigtranStackId);
        SctpServerConfig sctpServerConfig = creatorDao.createSctpServerDao(sctpServerConfigDto, sigtranStack);
        sctpServerConfig = sctpConfigService.addSctpServerConfig(sctpServerConfig);
        sctpService.addServer(sctpServerConfig, sigtranStack.getStackName());
        return creatorDto.createSctpServerConfigDto(sctpServerConfig);
    }
}