package com.unifun.sigproxy.controller.monitoring;

import com.unifun.sigproxy.service.m3ua.impl.M3uaServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("monitoring/m3ua")
@RequiredArgsConstructor
public class M3uaController {
    private final M3uaServiceImpl m3uaService;

//    @GetMapping(value = "/asStatuses", produces = "application/json")
//    @ResponseBody
//    public ResponseEntity<Set<M3uaAsDTO>> getAsStatuses() {
//        return ResponseEntity.ok(m3uaService.getM3uaStatuses());
//    }

}
