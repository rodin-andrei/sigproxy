package com.unifun.sigproxy.controller.monitoring;

import com.unifun.sigproxy.model.dto.SctpLinkDto;
import com.unifun.sigproxy.model.dto.SctpServerDto;
import com.unifun.sigproxy.service.SctpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;

@Controller
@RequestMapping("sctp")
@RequiredArgsConstructor
public class SctpController {

    private final SctpService sctpService;

    @GetMapping(value = "/link/status", produces = "application/json")
    public ResponseEntity<Set<SctpLinkDto>> getLinkStatus() {
        return ResponseEntity.ok(sctpService.getLinkStatus());
    }

    @GetMapping(value = "/server/status", produces = "application/json")
    public ResponseEntity<Set<SctpServerDto>> getServerLinksStatus() {
        return ResponseEntity.ok(sctpService.getServerLinkStatuses());
    }


}
