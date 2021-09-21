package com.unifun.sigproxy.controller.config;

import com.unifun.sigproxy.controller.dto.m3ua.M3uaAsConfigDto;
import com.unifun.sigproxy.controller.dto.m3ua.M3uaAspConfigDto;
import com.unifun.sigproxy.controller.dto.m3ua.M3uaRouteConfigDto;
import com.unifun.sigproxy.controller.dto.m3ua.M3uaStackSettingsConfigDto;
import com.unifun.sigproxy.controller.dto.service.CreatorDataAccessObjectService;
import com.unifun.sigproxy.controller.dto.service.CreatorDataObjectService;
import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.models.config.m3ua.M3uaAsConfig;
import com.unifun.sigproxy.service.SigtranConfigService;
import com.unifun.sigproxy.service.m3ua.M3uaConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("conf/m3ua")
@RequiredArgsConstructor
public class M3uaController {

    private final SigtranConfigService sigtranConfigService;
    private final M3uaConfigService m3uaConfigService;
    private final CreatorDataObjectService creatorDto;
    private final CreatorDataAccessObjectService creatorDao;

    @GetMapping(value = "/getAsConfig", produces = "application/json")
    @ResponseBody
    public Set<M3uaAsConfigDto> getM3uaAsConfig(@RequestParam Long sigtranStackId) {
        return m3uaConfigService.getM3uaAsConfig(sigtranStackId).stream()
                .map(this.creatorDto::createM3uaAsConfigDto)
                .collect(Collectors.toSet());
    }

    @GetMapping(value = "/getAspConfig", produces = "application/json")
    @ResponseBody
    public Set<M3uaAspConfigDto> getM3uaAspConfig(@RequestParam Long sigtranStackId) {
        return m3uaConfigService.getM3uaAspConfig(sigtranStackId).stream()
                .map(this.creatorDto::createM3uaAspConfigDto)
                .collect(Collectors.toSet());
    }

    @GetMapping(value = "/getRouteConfig", produces = "application/json")
    @ResponseBody
    public Set<M3uaRouteConfigDto> getM3uaRouteConfig(@RequestParam Long m3uaAsId) {
        return m3uaConfigService.getM3uaRouteConfig(m3uaAsId).stream()
                .map(this.creatorDto::createM3uaRouteConfigDto)
                .collect(Collectors.toSet());
    }

    @GetMapping(value = "/getStackSettingsConfig", produces = "application/json")
    @ResponseBody
    public M3uaStackSettingsConfigDto getM3uaStackSettingsConfig(@RequestParam Long sigtranStackId) {
        return creatorDto.createM3uaStackSettingsConfigDto(m3uaConfigService.getM3uaStackSettingsConfig(sigtranStackId));
    }

    @PostMapping(value = "/addAsConfig", produces = "application/json")
    @ResponseBody
    public M3uaAsConfigDto addAsConfig(@RequestParam Long sigtranStackId,
                                       @RequestBody M3uaAsConfigDto m3uaAsConfigDto) {
        SigtranStack sigtranStack = sigtranConfigService.getSigtranStackById(sigtranStackId);
        M3uaAsConfig m3uaAsConfig = m3uaConfigService.addM3uaAsConfig(creatorDao.createM3uaAsConfigDao(m3uaAsConfigDto, sigtranStack));
        return creatorDto.createM3uaAsConfigDto(m3uaAsConfig);
    }
}
