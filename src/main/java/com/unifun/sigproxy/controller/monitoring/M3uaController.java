package com.unifun.sigproxy.controller.monitoring;

import com.unifun.sigproxy.dto.M3uaAsDTO;
import com.unifun.sigproxy.service.impl.M3uaServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Set;

@Controller
@RequestMapping("monitoring/m3ua")
@RequiredArgsConstructor
public class M3uaController {
    private final M3uaServiceImpl m3uaService;

    @GetMapping(value = "/asStatuses", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Set<M3uaAsDTO>> getAsStatuses() {
        return ResponseEntity.ok(m3uaService.getM3uaStatuses());
    }

}
