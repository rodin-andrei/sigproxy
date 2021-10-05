package com.unifun.sigproxy.controller.config;

import com.unifun.sigproxy.controller.dto.service.CreatorDataAccessObjectService;
import com.unifun.sigproxy.controller.dto.service.CreatorDataObjectService;
import com.unifun.sigproxy.controller.dto.tcap.TcapConfigDto;
import com.unifun.sigproxy.exception.SS7AddException;
import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.models.config.tcap.TcapConfig;
import com.unifun.sigproxy.service.SigtranConfigService;
import com.unifun.sigproxy.service.tcap.TcapConfigService;
import com.unifun.sigproxy.service.tcap.TcapService;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("conf")
@RequiredArgsConstructor
public class TcapController {

    private final TcapConfigService tcapConfigService;
    private final TcapService tcapService;
    private final SigtranConfigService sigtranConfigService;
    private final CreatorDataObjectService creatorDto;
    private final CreatorDataAccessObjectService creatorDao;

    @GetMapping(value = "/getTcapConfig", produces = "application/json")
    @ResponseBody
    public TcapConfigDto getTcapConf(@RequestParam Long sigtranStackId) {

        return creatorDto.createTcapConfigDto(tcapConfigService.getTcapConfigByStackId(sigtranStackId));
    }

    @PostMapping(value = "/addTcapConfig", produces = "application/json")
    @ResponseBody
    public TcapConfigDto addTcapConfig(@RequestParam Long sigtranStackId,
                                       @RequestBody TcapConfigDto tcapConfigDto) {

        SigtranStack sigtranStack = sigtranConfigService.getSigtranStackById(sigtranStackId);
        TcapConfig tcapConfig = tcapConfigService.addTcapConfig(creatorDao.createTcapConfigDao(tcapConfigDto, sigtranStack));
        try {
            tcapService.initialize(sigtranStack);
        } catch (Exception e) {
            throw new SS7AddException("Can't initialize tcap management: " + sigtranStack.getStackName(), e);
        }
        return creatorDto.createTcapConfigDto(tcapConfig);
    }

    @GetMapping(value = "/removeTcapConfig")
    @ResponseBody
    public TcapConfigDto removeTcapConfig(@RequestParam Long tcapId){
        TcapConfigDto tcapConfigDto = creatorDto.createTcapConfigDto(tcapConfigService.getTcapConfigById(tcapId));
        tcapConfigService.removeTcap(tcapId);
        return tcapConfigDto;
    }
}
