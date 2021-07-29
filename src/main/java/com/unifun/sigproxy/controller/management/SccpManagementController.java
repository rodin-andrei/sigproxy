package com.unifun.sigproxy.controller.management;

import com.unifun.sigproxy.service.sccp.SccpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("management/sccp")
@RequiredArgsConstructor
@Slf4j
public class SccpManagementController {
    private final SccpService sccpService;

    @PostMapping(value = "/sendMessage", produces = "application/json")
    @ResponseBody
    public String sendMessage(@RequestParam String stackName,
                              @RequestParam int addrA,
                              @RequestParam int addrB) {

        sccpService.test(stackName, addrA, addrB);
        return "message sending";
    }
}
