package com.unifun.sigproxy.controller.monitoring;

import com.unifun.sigproxy.dto.SctpLinkDto;
import com.unifun.sigproxy.dto.SctpServerDto;
import com.unifun.sigproxy.service.SctpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;

@Controller
@RequestMapping("monitoring/sctp")
@RequiredArgsConstructor
public class MonitoringSctpController {

    private final SctpService sctpService;

    @GetMapping(value = "/links/status", produces = "application/json")
    public ResponseEntity<Set<SctpLinkDto>> getLinkStatus() {
        return ResponseEntity.ok(sctpService.getLinkStatuses());
    }

    @GetMapping(value = "/servers/status", produces = "application/json")
    public ResponseEntity<Set<SctpServerDto>> getServerLinksStatus() {
        return ResponseEntity.ok(sctpService.getServerLinkStatuses());
    }
}
