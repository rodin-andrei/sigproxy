package com.unifun.sigproxy.controller.config;

import com.unifun.sigproxy.controller.dto.CreatorDataObjectService;
import com.unifun.sigproxy.controller.dto.SigtranStackDto;
import com.unifun.sigproxy.service.SigtranConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("conf/sigtranStack")
@RequiredArgsConstructor
public class SigtranStackController {

    private final CreatorDataObjectService creatorDataObjectService;
    private final SigtranConfigService sigtranConfigService;

    @GetMapping(value = "/getSigtranStack", produces = "application/json")
    @ResponseBody
    public SigtranStackDto getSigtranStack(@RequestParam Long sigtranStackId){
        return creatorDataObjectService.createSigtranStackDto(sigtranConfigService.getSigtranStackById(sigtranStackId));
    }

    @GetMapping(value = "/getSigtranStacks", produces = "application/json")
    @ResponseBody
    public List<SigtranStackDto> getSigtranStacks(){
        return sigtranConfigService.getSigtranStacks()
                .stream()
                .map(creatorDataObjectService::createSigtranStackDto)
                .collect(Collectors.toList());
    }
}
