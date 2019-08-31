package com.unifun.sigproxy.controller.configuration;

import com.unifun.sigproxy.exception.NoConfigurationException;
import com.unifun.sigproxy.model.config.LinkConfig;
import com.unifun.sigproxy.model.config.SctpConfig;
import com.unifun.sigproxy.service.SctpConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("sctp/conf")
@RequiredArgsConstructor
public class SctpConfigController {

    private final SctpConfigService sctpConfigService;

    @GetMapping(value = {"/full", "/", ""}, produces = "application/json")
    @ResponseBody
    public ResponseEntity<SctpConfig> getInfo() {
        try {
            return ResponseEntity.ok(sctpConfigService.getSctpConfiguration());
        } catch (NoConfigurationException e) {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping(value = "/link", produces = "application/json")
    @ResponseBody
    public ResponseEntity<LinkConfig> getLinkInfo(@RequestParam("name") String linkName) {
        try {
            Optional<LinkConfig> linkConfig = sctpConfigService.getLinkConfig(linkName);
            if (linkConfig.isPresent()) {
                return ResponseEntity.ok(linkConfig.get());
            }
            return ResponseEntity.noContent().build();
        } catch (NoConfigurationException e) {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping(value = "/link", consumes = "application/json")
    public void postLinkInfo(@RequestBody LinkConfig linkConfig) throws NoConfigurationException {
        sctpConfigService.setLinkConfig(linkConfig);
    }

    @PutMapping(value = "/link", consumes = "application/json")
    public void putLinkInfo(@RequestBody LinkConfig linkConfig) throws NoConfigurationException {
        sctpConfigService.updateLinkConfig(linkConfig);
    }


}
