package com.unifun.sigproxy.sigtran.controller.management;

import com.unifun.sigproxy.sigtran.service.map.MapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("management/map")
@RequiredArgsConstructor
@Slf4j
public class MapManagementController {
    private final MapService mapService;

    @PostMapping(value = "/sendMessage", produces = "application/json")
    @ResponseBody
    public String sendMessage(@RequestParam String stackName,
                              @RequestParam int addrA,
                              @RequestParam int addrB) {

        mapService.test(stackName, addrA, addrB);

        return "";
    }
}
