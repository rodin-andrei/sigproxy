package com.unifun.sigproxy.sigtran.controller.config;

import com.unifun.sigproxy.sigtran.controller.dto.m3ua.M3uaAsConfigDto;
import com.unifun.sigproxy.sigtran.controller.dto.m3ua.M3uaAspConfigDto;
import com.unifun.sigproxy.sigtran.controller.dto.m3ua.M3uaRouteConfigDto;
import com.unifun.sigproxy.sigtran.controller.dto.m3ua.M3uaStackSettingsConfigDto;
import com.unifun.sigproxy.sigtran.controller.dto.service.CreatorDataAccessObjectService;
import com.unifun.sigproxy.sigtran.controller.dto.service.CreatorDataObjectService;
import com.unifun.sigproxy.sigtran.exception.InitializingException;
import com.unifun.sigproxy.sigtran.models.config.SigtranStack;
import com.unifun.sigproxy.sigtran.models.config.m3ua.M3uaAsConfig;
import com.unifun.sigproxy.sigtran.models.config.m3ua.M3uaAspConfig;
import com.unifun.sigproxy.sigtran.models.config.m3ua.M3uaRouteConfig;
import com.unifun.sigproxy.sigtran.models.config.m3ua.M3uaStackSettingsConfig;
import com.unifun.sigproxy.sigtran.service.SigtranConfigService;
import com.unifun.sigproxy.sigtran.service.m3ua.M3uaConfigService;
import com.unifun.sigproxy.sigtran.service.m3ua.M3uaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("conf/m3ua")
@RequiredArgsConstructor
public class M3uaController {

    private final M3uaService m3uaService;
    private final SigtranConfigService sigtranConfigService;
    private final M3uaConfigService m3uaConfigService;
    private final CreatorDataObjectService creatorDto;
    private final CreatorDataAccessObjectService creatorDao;

    @GetMapping(value = "/getAsConfig", produces = "application/json")
    @ResponseBody
    public Set<M3uaAsConfigDto> getM3uaAsConfig(@RequestParam Long sigtranStackId) {
        return m3uaConfigService.getM3uaAsConfigByStackId(sigtranStackId).stream()
                .map(this.creatorDto::createM3uaAsConfigDto)
                .collect(Collectors.toSet());
    }

    @GetMapping(value = "/getAspConfig", produces = "application/json")
    @ResponseBody
    public Set<M3uaAspConfigDto> getM3uaAspConfig(@RequestParam Long sigtranStackId) {
        return m3uaConfigService.getM3uaAspConfigByAsId(sigtranStackId).stream()
                .map(this.creatorDto::createM3uaAspConfigDto)
                .collect(Collectors.toSet());
    }

    @GetMapping(value = "/getRouteConfig", produces = "application/json")
    @ResponseBody
    public Set<M3uaRouteConfigDto> getM3uaRouteConfig(@RequestParam Long m3uaAsId) {
        return m3uaConfigService.getM3uaRouteConfigByAsId(m3uaAsId).stream()
                .map(this.creatorDto::createM3uaRouteConfigDto)
                .collect(Collectors.toSet());
    }

    @GetMapping(value = "/getStackSettingsConfig", produces = "application/json")
    @ResponseBody
    public M3uaStackSettingsConfigDto getM3uaStackSettingsConfig(@RequestParam Long sigtranStackId) {
        return creatorDto.createM3uaStackSettingsConfigDto(m3uaConfigService.getM3uaStackSettingsConfigByStackId(sigtranStackId));
    }

    @PostMapping(value = "/addAsConfig", produces = "application/json")
    @ResponseBody
    public M3uaAsConfigDto addAsConfig(@RequestParam Long sigtranStackId,
                                       @RequestBody M3uaAsConfigDto m3uaAsConfigDto) {
        SigtranStack sigtranStack = sigtranConfigService.getSigtranStackById(sigtranStackId);
        M3uaAsConfig m3uaAsConfig = m3uaConfigService.addM3uaAsConfig(creatorDao.createM3uaAsConfigDao(m3uaAsConfigDto, sigtranStack));
        m3uaService.addAs(m3uaAsConfig);
        return creatorDto.createM3uaAsConfigDto(m3uaAsConfig);
    }

    @PostMapping(value = "/addAspConfig", produces = "application/json")
    @ResponseBody
    public M3uaAspConfigDto addAspConfig(@RequestParam Long m3uaAsId,
                                         @RequestBody M3uaAspConfigDto m3uaAspConfigDto) {
        M3uaAsConfig m3uaAsConfig = m3uaConfigService.getM3uaAsConfigById(m3uaAsId);
        M3uaAspConfig m3uaAspConfig = m3uaConfigService.addM3uaAspConfig(creatorDao.createM3uaAspConfigDao(m3uaAspConfigDto, m3uaAsConfig));
        m3uaService.addAsp(m3uaAspConfig, m3uaAsConfig.getSigtranStack().getStackName());
        return creatorDto.createM3uaAspConfigDto(m3uaAspConfig);
    }

    @PostMapping(value = "/addRouteConfig", produces = "application/json")
    @ResponseBody
    public M3uaRouteConfigDto addRouteConfig(@RequestParam Long m3uaAsId,
                                             @RequestBody M3uaRouteConfigDto m3uaRouteConfigDto) {
        M3uaAsConfig m3uaAsConfig = m3uaConfigService.getM3uaAsConfigById(m3uaAsId);
        M3uaRouteConfig m3uaRouteConfig = m3uaConfigService.addM3uaRouteConfig(creatorDao.createM3uaRouteConfigDao(m3uaRouteConfigDto, m3uaAsConfig));
        m3uaService.addRoute(m3uaRouteConfig);
        return creatorDto.createM3uaRouteConfigDto(m3uaRouteConfig);
    }

    @PostMapping(value = "/addStackSettingsConfig", produces = "application/json")
    @ResponseBody
    public M3uaStackSettingsConfigDto addStackSettingsConfig(@RequestParam Long sigtranStackId,
                                                             @RequestBody M3uaStackSettingsConfigDto m3uaStackSettingsConfigDto) throws InitializingException {
        SigtranStack sigtranStack = sigtranConfigService.getSigtranStackById(sigtranStackId);
        M3uaStackSettingsConfig m3uaStackSettingsConfig = m3uaConfigService.addM3uaStackSettingsConfig(creatorDao.createM3uaStackSettingsConfigDao(m3uaStackSettingsConfigDto, sigtranStack));
        m3uaService.initM3uaManagement(sigtranStack);
        return creatorDto.createM3uaStackSettingsConfigDto(m3uaStackSettingsConfig);
    }

    @DeleteMapping(value = "/removeAsConfig", produces = "application/json")
    @ResponseBody
    public M3uaAsConfigDto removeAsConfig(@RequestParam Long m3uaAsId) {
        M3uaAsConfig m3uaAsConfig = m3uaConfigService.getM3uaAsConfigById(m3uaAsId);
        m3uaConfigService.removeM3uaAsConfig(m3uaAsId);
        return creatorDto.createM3uaAsConfigDto(m3uaAsConfig);
    }

    @DeleteMapping(value = "/removeAspConfig", produces = "application/json")
    @ResponseBody
    public M3uaAspConfigDto removeAspConfig(@RequestParam Long m3uaAspId) {
        M3uaAspConfig m3uaAspConfig = m3uaConfigService.getM3uaAspConfigById(m3uaAspId);
        m3uaConfigService.removeM3uaAspConfig(m3uaAspId);
        return creatorDto.createM3uaAspConfigDto(m3uaAspConfig);
    }

    @DeleteMapping(value = "/removeRouteConfig", produces = "application/json")
    @ResponseBody
    public M3uaRouteConfigDto removeRouteConfig(@RequestParam Long m3uaRouteId) {
        M3uaRouteConfig m3uaRouteConfig = m3uaConfigService.getM3uaRouteConfigById(m3uaRouteId);
        m3uaConfigService.removeM3uaRouteConfig(m3uaRouteId);
        return creatorDto.createM3uaRouteConfigDto(m3uaRouteConfig);
    }

    @DeleteMapping(value = "/removeM3uaAsConfig", produces = "application/json")
    @ResponseBody
    public M3uaStackSettingsConfigDto removeStackSettingsConfig(@RequestParam Long m3uaStackSettingsId) {
        M3uaStackSettingsConfig m3uaStackSettingsConfig = m3uaConfigService.getM3uaStackSettingsConfigById(m3uaStackSettingsId);
        m3uaConfigService.removeM3uaStackSettingsConfig(m3uaStackSettingsId);
        return creatorDto.createM3uaStackSettingsConfigDto(m3uaStackSettingsConfig);
    }
}
