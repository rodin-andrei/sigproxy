package com.unifun.sigproxy.controller.configuration;

import com.unifun.sigproxy.model.config.M3uaConfig;
import com.unifun.sigproxy.model.config.SigtranConfig;
import com.unifun.sigproxy.repository.SigtranRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("m3ua")
public class M3uaController {
    private final SigtranRepository sigtranRepository;

    public M3uaController(SigtranRepository sigtranRepository) {
        this.sigtranRepository = sigtranRepository;
    }

    @GetMapping(value = "/get", produces = "application/json")
    @ResponseBody
    public ResponseEntity<M3uaConfig> getInfo() {
        SigtranConfig sigtranConfig = sigtranRepository.getSigtranConfig();
        if (sigtranConfig != null) {
            return ResponseEntity.ok(sigtranConfig.getM3uaConfig());
        }
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/put", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<M3uaConfig> updateM3ua() {

        return ResponseEntity.accepted().build();
    }

}
