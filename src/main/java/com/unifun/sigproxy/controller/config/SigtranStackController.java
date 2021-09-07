package com.unifun.sigproxy.controller.config;

import com.unifun.sigproxy.controller.dto.CreatorDataObject;
import com.unifun.sigproxy.controller.dto.SigtranStackDto;
import com.unifun.sigproxy.service.SigtranConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("conf/sigtranStack")
@RequiredArgsConstructor
public class SigtranStackController {

    private final CreatorDataObject creatorDataObject;
    private final SigtranConfigService sigtranConfigService;

    @GetMapping(value = "/getSigtranStack", produces = "application/json")
    @ResponseBody
    public SigtranStackDto getSigtranStack(@RequestParam Long sigtranStackId){
        return creatorDataObject.createSigtranStackDto(sigtranConfigService.getSigtranStackById(sigtranStackId));
    }

    @GetMapping(value = "/getSigtranStacks", produces = "application/json")
    @ResponseBody
    public List<SigtranStackDto> getSigtranStacks(){
        return sigtranConfigService.getSigtranStacks()
                .stream()
                .map(creatorDataObject::createSigtranStackDto)
                .collect(Collectors.toList());
    }
}
