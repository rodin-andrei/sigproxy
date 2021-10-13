package com.unifun.sigproxy.controller.config;

import com.unifun.sigproxy.controller.dto.SigtranStackDto;
import com.unifun.sigproxy.controller.dto.service.CreatorDataAccessObjectService;
import com.unifun.sigproxy.controller.dto.service.CreatorDataObjectService;
import com.unifun.sigproxy.exception.SS7AddException;
import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.service.SigtranConfigService;
import com.unifun.sigproxy.service.sctp.SctpService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("conf")
@RequiredArgsConstructor
public class SigtranStackController {

    private final CreatorDataObjectService creatorDto;
    private final CreatorDataAccessObjectService creatorDao;
    private final SigtranConfigService sigtranConfigService;
    private final SctpService sctpService;

    @GetMapping(value = "/getSigtranStack", produces = "application/json")
    @ResponseBody
    public SigtranStackDto getSigtranStack(@RequestParam Long sigtranStackId){
        return creatorDto.createSigtranStackDto(sigtranConfigService.getSigtranStackById(sigtranStackId));
    }

    @GetMapping(value = "/getSigtranStacks", produces = "application/json")
    @ResponseBody
    public List<SigtranStackDto> getSigtranStacks(){
        return sigtranConfigService.getSigtranStacks()
                .stream()
                .map(creatorDto::createSigtranStackDto)
                .collect(Collectors.toList());
    }

    @PostMapping(value = "/addSigtranStack", produces = "application/json")
    @ResponseBody
    public SigtranStackDto addSigtranStack(@RequestBody SigtranStackDto sigtranStackDto){
        SigtranStack sigtranStack = sigtranConfigService.addSigtranStack(creatorDao.createSigtranStackDao(sigtranStackDto));
        try {
            sctpService.addSigtranStack(sigtranStack);
        } catch (Exception e){
            throw new SS7AddException("Can't initialize sctp management: " + sigtranStack.getStackName(), e);
        }

        return creatorDto.createSigtranStackDto(sigtranStack);
    }

    @DeleteMapping(value = "/removeSigtranStack", produces = "application/json")
    @ResponseBody
    public SigtranStackDto removeSigtranStack(@RequestParam Long sigtranStackId){
        SigtranStack sigtranStack = sigtranConfigService.getSigtranStackById(sigtranStackId);
        sigtranConfigService.removeSigtranStack(sigtranStackId);
        return creatorDto.createSigtranStackDto(sigtranStack);
    }
}
