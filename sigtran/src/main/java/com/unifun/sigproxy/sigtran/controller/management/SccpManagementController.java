package com.unifun.sigproxy.sigtran.controller.management;

import com.unifun.sigproxy.sigtran.service.m3ua.M3uaService;
import com.unifun.sigproxy.sigtran.service.sccp.SccpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("management/sccp")
@RequiredArgsConstructor
@Slf4j
public class SccpManagementController {
    private final SccpService sccpService;
    private final M3uaService m3uaService;

    @PostMapping(value = "/sendMessage", produces = "application/json")
    @ResponseBody
    public String sendMessage(@RequestParam String stackName,
                              @RequestParam int addrA,
                              @RequestParam int addrB) {

        sccpService.test(stackName, addrA, addrB);
        String out = "";
        List<String> collect = m3uaService.getManagement(stackName).getAppServers().stream().map(as -> as.getName() + "---" + as.getState() + ",   ").collect(Collectors.toList());
        for (String collect1 : collect) {
            out += collect1;
        }
        return out;
    }
}
