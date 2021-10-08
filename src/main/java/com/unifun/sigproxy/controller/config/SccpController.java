package com.unifun.sigproxy.controller.config;

import com.unifun.sigproxy.controller.dto.sccp.*;
import com.unifun.sigproxy.controller.dto.service.CreatorDataAccessObjectService;
import com.unifun.sigproxy.controller.dto.service.CreatorDataObjectService;
import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.models.config.sccp.*;
import com.unifun.sigproxy.service.SigtranConfigService;
import com.unifun.sigproxy.service.sccp.SccpConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("conf/sccp")
@RequiredArgsConstructor
public class SccpController {

    private final SccpConfigService sccpConfigService;
    private final SigtranConfigService sigtranConfigService;
    private final CreatorDataObjectService creatorDto;
    private final CreatorDataAccessObjectService creatorDao;

    ///////////////////////////////////////
    //Get Controllers
    ///////////////////////////////////////
    @GetMapping(value = "/getAddressConfig", produces = "application/json")
    @ResponseBody
    public Set<SccpAddressConfigDto> getAddressConfig(@RequestParam Long sigtranStackId) {

        return sccpConfigService.getAddressConfigByStackId(sigtranStackId).stream()
                .map(creatorDto::createSccpAddressConfigDto)
                .collect(Collectors.toSet());
    }

    @GetMapping(value = "/getAddressRuleConfig", produces = "application/json")
    @ResponseBody
    public SccpAddressRuleConfigDto getAddressRuleConfig(@RequestParam Integer sccpRuleConfigId) {
        return creatorDto.createSccpAddressRuleConfigDto(sccpConfigService.getAddressRuleConfigByRuleConfigId(sccpRuleConfigId));
    }

    @GetMapping(value = "/getConcernedSignalingPointCodeConfig", produces = "application/json")
    @ResponseBody
    public Set<SccpConcernedSignalingPointCodeConfigDto> getConcernedSignalingPointCodeConfig(@RequestParam Long sigtranStackId) {
        return sccpConfigService.getConcernedSignalingPointCodeConfigByStackId(sigtranStackId).stream()
                .map(creatorDto::createSccpConcernedSignalingPointCodeConfigDto)
                .collect(Collectors.toSet());
    }

    @GetMapping(value = "/getLongMessageRuleConfig", produces = "application/json")
    @ResponseBody
    public Set<SccpLongMessageRuleConfigDto> getLongMessageRuleConfig(@RequestParam Long sigtranStackId) {
        return sccpConfigService.getLongMessageRuleConfigByStackId(sigtranStackId)
                .stream()
                .map(creatorDto::createSccpLongMessageRuleConfigDto)
                .collect(Collectors.toSet());
    }

    @GetMapping(value = "/getMtp3DestinationConfig", produces = "application/json")
    @ResponseBody
    public Set<SccpMtp3DestinationConfigDto> getMtp3DestinationConfig(@RequestParam Integer sccpServiceAccessPointConfigId) {
        return sccpConfigService.getMtp3DestinationConfigBySccpServiceAccessPointConfigId(sccpServiceAccessPointConfigId)
                .stream()
                .map(creatorDto::createSccpMtp3DestinationConfigDto)
                .collect(Collectors.toSet());
    }

    @GetMapping(value = "/getRemoteSignalingPointConfig", produces = "application/json")
    @ResponseBody
    public Set<SccpRemoteSignalingPointConfigDto> getRemoteSignalingPointConfig(@RequestParam Long sigtranStackId) {
        return sccpConfigService.getRemoteSignalingPointConfigByStackId(sigtranStackId)
                .stream()
                .map(creatorDto::createSccpRemoteSignalingPointConfigDto)
                .collect(Collectors.toSet());
    }

    @GetMapping(value = "/getRemoteSubsystemConfig", produces = "application/json")
    @ResponseBody
    public Set<SccpRemoteSubsystemConfigDto> getRemoteSubsystemConfig(@RequestParam Long sigtranStackId) {
        return sccpConfigService.getRemoteSubsystemConfigByStackId(sigtranStackId)
                .stream()
                .map(creatorDto::createSccpRemoteSubsystemConfigDto)
                .collect(Collectors.toSet());
    }

    @GetMapping(value = "/getRuleConfig", produces = "application/json")
    @ResponseBody
    public Set<SccpRuleConfigDto> getRuleConfig(@RequestParam Long sigtranStackId) {
        return sccpConfigService.getRuleConfigByStackId(sigtranStackId)
                .stream()
                .map(creatorDto::createSccpRuleConfigDto)
                .collect(Collectors.toSet());
    }

    @GetMapping(value = "/getServiceAccessPointConfig", produces = "application/json")
    @ResponseBody
    public Set<SccpServiceAccessPointConfigDto> getServiceAccessPointConfig(@RequestParam Long sigtranStackId) {
        return sccpConfigService.getServiceAccessPointConfigByStackId(sigtranStackId)
                .stream()
                .map(creatorDto::createSccpServiceAccessPointConfigDto)
                .collect(Collectors.toSet());
    }

    @GetMapping(value = "/getSettingsConfig", produces = "application/json")
    @ResponseBody
    public SccpSettingsConfigDto getSettingsConfig(@RequestParam Long sigtranStackId) {
        return creatorDto.createSccpSettingsConfigDto(sccpConfigService.getSettingsConfigByStackId(sigtranStackId));
    }

    ///////////////////////////////////////
    //Remove Controllers
    ///////////////////////////////////////

    @DeleteMapping(value = "/removeAddressConfig", produces = "application/json")
    @ResponseBody
    public void removeAddressConfig(@RequestParam Integer sccpAddressConfigId) {
//        SccpAddressConfigDto sccpAddressConfigDto = creatorDto.createSccpAddressConfigDto(sccpConfigService.getAddressConfigById(sccpAddressConfigId));
        sccpConfigService.removeAddressConfig(sccpAddressConfigId);
//        return sccpAddressConfigDto;
    }

    @DeleteMapping(value = "/removeAddressRuleConfig", produces = "application/json")
    @ResponseBody
    public SccpAddressRuleConfigDto removeAddressRuleConfig(@RequestParam Long sccpAddressRuleConfigId) {
        SccpAddressRuleConfigDto sccpAddressRuleConfigDto = creatorDto.createSccpAddressRuleConfigDto(sccpConfigService.getAddressRuleConfigById(sccpAddressRuleConfigId));
        sccpConfigService.removeAddressRuleConfig(sccpAddressRuleConfigId);
        return sccpAddressRuleConfigDto;
    }

    @DeleteMapping(value = "/removeConcernedSignalingPointCodeConfig", produces = "application/json")
    @ResponseBody
    public SccpConcernedSignalingPointCodeConfigDto removeConcernedSignalingPointCodeConfig(@RequestParam Integer sccpConcernedSignalingPointCodeConfigId) {
        SccpConcernedSignalingPointCodeConfigDto sccpConcernedSignalingPointCodeConfigDto =
                creatorDto.createSccpConcernedSignalingPointCodeConfigDto(sccpConfigService.getConcernedSignalingPointCodeConfigById(sccpConcernedSignalingPointCodeConfigId));
        sccpConfigService.removeConcernedSignalingPointCodeConfig(sccpConcernedSignalingPointCodeConfigId);
        return sccpConcernedSignalingPointCodeConfigDto;
    }

    @DeleteMapping(value = "/removeLongMessageRuleConfig", produces = "application/json")
    @ResponseBody
    public SccpLongMessageRuleConfigDto removeLongMessageRuleConfig(@RequestParam Integer sccpLongMessageRuleConfigId) {
        SccpLongMessageRuleConfigDto sccpLongMessageRuleConfigDto =
                creatorDto.createSccpLongMessageRuleConfigDto(sccpConfigService.getLongMessageRuleConfigById(sccpLongMessageRuleConfigId));
        sccpConfigService.removeLongMessageRuleConfig(sccpLongMessageRuleConfigId);
        return sccpLongMessageRuleConfigDto;
    }

    @DeleteMapping(value = "/removeMtp3DestinationConfig", produces = "application/json")
    @ResponseBody
    public SccpMtp3DestinationConfigDto removeMtp3DestinationConfig(@RequestParam Integer sccpMtp3DestinationConfigId) {
        SccpMtp3DestinationConfigDto sccpMtp3DestinationConfigDto =
                creatorDto.createSccpMtp3DestinationConfigDto(sccpConfigService.getMtp3DestinationConfigById(sccpMtp3DestinationConfigId));
        sccpConfigService.removeMtp3DestinationConfig(sccpMtp3DestinationConfigId);
        return sccpMtp3DestinationConfigDto;
    }

    @DeleteMapping(value = "/removeRemoteSignalingPointConfig", produces = "application/json")
    @ResponseBody
    public SccpRemoteSignalingPointConfigDto removeRemoteSignalingPointConfig(@RequestParam Integer sccpRemoteSignalingPointConfigId) {
        SccpRemoteSignalingPointConfigDto sccpRemoteSignalingPointConfigDto =
                creatorDto.createSccpRemoteSignalingPointConfigDto(sccpConfigService.getRemoteSignalingPointConfigById(sccpRemoteSignalingPointConfigId));
        sccpConfigService.removeRemoteSignalingPointConfig(sccpRemoteSignalingPointConfigId);
        return sccpRemoteSignalingPointConfigDto;

    }

    @DeleteMapping(value = "/removeRemoteSubsystemConfig", produces = "application/json")
    @ResponseBody
    public SccpRemoteSubsystemConfigDto removeRemoteSubsystemConfig(@RequestParam Integer sccpRemoteSubsystemConfigId) {
        SccpRemoteSubsystemConfigDto sccpRemoteSubsystemConfigDto = creatorDto.createSccpRemoteSubsystemConfigDto(sccpConfigService.getRemoteSubsystemConfigById(sccpRemoteSubsystemConfigId));
        sccpConfigService.removeRemoteSubsystemConfig(sccpRemoteSubsystemConfigId);
        return sccpRemoteSubsystemConfigDto;
    }

    @DeleteMapping(value = "/removeRuleConfig", produces = "application/json")
    @ResponseBody
    public SccpRuleConfigDto removeRuleConfig(@RequestParam Integer sccpRuleConfigId) {
        SccpRuleConfigDto sccpRuleConfigDto = creatorDto.createSccpRuleConfigDto(sccpConfigService.getRuleConfigById(sccpRuleConfigId));
        sccpConfigService.removeRuleConfig(sccpRuleConfigId);
        return sccpRuleConfigDto;
    }

    @PostMapping(value = "/removeServiceAccessPointConfig", produces = "application/json")
    @ResponseBody
    public SccpServiceAccessPointConfigDto removeServiceAccessPointConfig(@RequestParam Integer sccpServiceAccessPointConfigId) {
        SccpServiceAccessPointConfigDto sccpServiceAccessPointConfigDto =
                creatorDto.createSccpServiceAccessPointConfigDto(sccpConfigService.getServiceAccessPointConfigById(sccpServiceAccessPointConfigId));
        sccpConfigService.removeServiceAccessPointConfig(sccpServiceAccessPointConfigId);
        return sccpServiceAccessPointConfigDto;
    }

    @DeleteMapping(value = "/removeSettingsConfig", produces = "application/json")
    @ResponseBody
    public SccpSettingsConfigDto removeSettingsConfig(@RequestParam Long sccpSettingsConfigId) {
        SccpSettingsConfigDto sccpSettingsConfigDto = creatorDto.createSccpSettingsConfigDto(sccpConfigService.getSettingsConfigById(sccpSettingsConfigId));
        sccpConfigService.removeSettingsConfig(sccpSettingsConfigId);
        return sccpSettingsConfigDto;
    }

    ///////////////////////////////////////
    //Add Controllers
    ///////////////////////////////////////
    @PostMapping(value = "/addAddressConfig", produces = "application/json")
    @ResponseBody
    public SccpAddressConfigDto addAddressConfig(@RequestParam Long sigtranStackId,
                                                 @RequestBody SccpAddressConfigDto sccpAddressConfigDto) {
        SigtranStack sigtranStack = sigtranConfigService.getSigtranStackById(sigtranStackId);
        SccpAddressConfig sccpAddressConfig = sccpConfigService.addAddressConfig(creatorDao.createSccpAddressConfigDao(sccpAddressConfigDto, sigtranStack));
        return creatorDto.createSccpAddressConfigDto(sccpAddressConfig);
    }

    @PostMapping(value = "/addAddressRuleConfig", produces = "application/json")
    @ResponseBody
    public SccpAddressRuleConfigDto addAddressRuleConfig(@RequestParam Long sccpRuleConfigId,
                                                         @RequestBody SccpAddressRuleConfigDto sccpAddressRuleConfigDto) {
        SccpAddressRuleConfig sccpAddressRuleConfig = sccpConfigService.addAddressRuleConfig(creatorDao.createSccpAddressRuleConfigDao(sccpAddressRuleConfigDto));
        return creatorDto.createSccpAddressRuleConfigDto(sccpAddressRuleConfig);
    }

    @PostMapping(value = "/addConcernedSignalingPointCodeConfig", produces = "application/json")
    @ResponseBody
    public SccpConcernedSignalingPointCodeConfigDto addConcernedSignalingPointCodeConfig(@RequestParam Long sigtranStackId,
                                                                                         @RequestBody SccpConcernedSignalingPointCodeConfigDto sccpConcernedSignalingPointCodeConfigDto) {
        SigtranStack sigtranStack = sigtranConfigService.getSigtranStackById(sigtranStackId);
        SccpConcernedSignalingPointCodeConfig sccpConcernedSignalingPointCodeConfig =
                sccpConfigService.addConcernedSignalingPointCodeConfig(creatorDao.createSccpConcernedSignalingPointCodeConfigDao(sccpConcernedSignalingPointCodeConfigDto, sigtranStack));
        return creatorDto.createSccpConcernedSignalingPointCodeConfigDto(sccpConcernedSignalingPointCodeConfig);
    }

    @PostMapping(value = "/addLongMessageRuleConfig", produces = "application/json")
    @ResponseBody
    public SccpLongMessageRuleConfigDto addLongMessageRuleConfig(@RequestParam Long sigtranStackId,
                                                                 @RequestBody SccpLongMessageRuleConfigDto sccpLongMessageRuleConfigDto) {
        SigtranStack sigtranStack = sigtranConfigService.getSigtranStackById(sigtranStackId);
        SccpLongMessageRuleConfig sccpLongMessageRuleConfig = sccpConfigService.addLongMessageRuleConfig(creatorDao.createSccpLongMessageRuleConfigDao(sccpLongMessageRuleConfigDto, sigtranStack));
        return creatorDto.createSccpLongMessageRuleConfigDto(sccpLongMessageRuleConfig);
    }

    @PostMapping(value = "/addMtp3DestinationConfig", produces = "application/json")
    @ResponseBody
    public SccpMtp3DestinationConfigDto addMtp3DestinationConfig(@RequestParam Integer sccpServiceAccessPointConfigId,
                                                                 @RequestBody SccpMtp3DestinationConfigDto sccpMtp3DestinationConfigDto) {
        SccpServiceAccessPointConfig sccpServiceAccessPointConfig = sccpConfigService.getServiceAccessPointConfigById(sccpServiceAccessPointConfigId);
        SccpMtp3DestinationConfig sccpMtp3DestinationConfig = sccpConfigService.addMtp3DestinationConfig(creatorDao.createSccpMtp3DestinationConfigDao(sccpMtp3DestinationConfigDto, sccpServiceAccessPointConfig));
        return creatorDto.createSccpMtp3DestinationConfigDto(sccpMtp3DestinationConfig);
    }

    @PostMapping(value = "/addRemoteSignalingPointConfig", produces = "application/json")
    @ResponseBody
    public SccpRemoteSignalingPointConfigDto addRemoteSignalingPointConfig(@RequestParam Long sigtranStackId,
                                                                           @RequestBody SccpRemoteSignalingPointConfigDto sccpRemoteSignalingPointConfigDto) {
        SigtranStack sigtranStack = sigtranConfigService.getSigtranStackById(sigtranStackId);
        SccpRemoteSignalingPointConfig sccpRemoteSignalingPointConfig =
                sccpConfigService.addRemoteSignalingPointConfig(creatorDao.createSccpRemoteSignalingPointConfigDao(sccpRemoteSignalingPointConfigDto, sigtranStack));
        return creatorDto.createSccpRemoteSignalingPointConfigDto(sccpRemoteSignalingPointConfig);
    }

    @PostMapping(value = "/addRemoteSubsystemConfig", produces = "application/json")
    @ResponseBody
    public SccpRemoteSubsystemConfigDto addRemoteSubsystemConfig(@RequestParam Long sigtranStackId,
                                                                 @RequestBody SccpRemoteSubsystemConfigDto sccpRemoteSubsystemConfigDto) {
        SigtranStack sigtranStack = sigtranConfigService.getSigtranStackById(sigtranStackId);
        SccpRemoteSubsystemConfig sccpRemoteSubsystemConfig = sccpConfigService.addRemoteSubsystemConfig(creatorDao.createSccpRemoteSubsystemConfigDao(sccpRemoteSubsystemConfigDto, sigtranStack));
        return creatorDto.createSccpRemoteSubsystemConfigDto(sccpRemoteSubsystemConfig);
    }

    @PostMapping(value = "/addRuleConfig", produces = "application/json")
    @ResponseBody
    public SccpRuleConfigDto addRuleConfig(@RequestParam Long sigtranStackId,
                                           @RequestParam Long sccpAddressRuleConfigId,
                                           @RequestParam Long callingSccpAddressRuleConfigId,
                                           @RequestBody SccpRuleConfigDto sccpRuleConfigDto) {
        SigtranStack sigtranStack = sigtranConfigService.getSigtranStackById(sigtranStackId);
        SccpAddressRuleConfig sccpAddressRuleConfig = sccpConfigService.getAddressRuleConfigById(sccpAddressRuleConfigId);
        SccpAddressRuleConfig callingSccpAddressRuleConfig = sccpConfigService.getAddressRuleConfigById(callingSccpAddressRuleConfigId);
        SccpRuleConfig sccpRuleConfig = sccpConfigService.addRuleConfig(creatorDao.createSccpRuleConfigDao(sccpRuleConfigDto, sigtranStack, sccpAddressRuleConfig, callingSccpAddressRuleConfig));
        return creatorDto.createSccpRuleConfigDto(sccpRuleConfig);
    }

    @PostMapping(value = "/addServiceAccessPointConfig", produces = "application/json")
    @ResponseBody
    public SccpServiceAccessPointConfigDto addServiceAccessPointConfig(@RequestParam Long sigtranStackId,
                                                                       @RequestBody SccpServiceAccessPointConfigDto sccpServiceAccessPointConfigDto) {
        SigtranStack sigtranStack = sigtranConfigService.getSigtranStackById(sigtranStackId);
        SccpServiceAccessPointConfig sccpServiceAccessPointConfig = sccpConfigService.addServiceAccessPointConfig(creatorDao.createSccpServiceAccessPointConfigDao(sccpServiceAccessPointConfigDto, sigtranStack));
        return creatorDto.createSccpServiceAccessPointConfigDto(sccpServiceAccessPointConfig);
    }

    @PostMapping(value = "/addSettingsConfig", produces = "application/json")
    @ResponseBody
    public SccpSettingsConfigDto addSettingsConfig(@RequestParam Long sigtranStackId,
                                                   @RequestBody SccpSettingsConfigDto sccpSettingsConfigDto) {
        SigtranStack sigtranStack = sigtranConfigService.getSigtranStackById(sigtranStackId);
        SccpSettingsConfig sccpSettingsConfig = sccpConfigService.addSettingsConfig(creatorDao.createSccpSettingsConfigDao(sccpSettingsConfigDto, sigtranStack));
        return creatorDto.createSccpSettingsConfigDto(sccpSettingsConfig);
    }

}
