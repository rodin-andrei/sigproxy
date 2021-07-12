package com.unifun.sigproxy.controller.monitoring;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("monitoring/sctp")
@RequiredArgsConstructor
public class MonitoringSctpController {

//    private final SctpService sctpService;
//
//    @GetMapping(value = "/links/status", produces = "application/json")
//    public ResponseEntity<Set<SctpLinkDto>> getLinkStatus() {
//        return ResponseEntity.ok(sctpService.getLinkStatuses());
//    }
//
//    @GetMapping(value = "/servers/status", produces = "application/json")
//    public ResponseEntity<Set<SctpServerDto>> getServerLinksStatus() {
//        return ResponseEntity.ok(sctpService.getServerLinkStatuses());
//    }
}
