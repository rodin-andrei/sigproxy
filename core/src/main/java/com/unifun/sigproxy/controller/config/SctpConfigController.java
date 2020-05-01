package com.unifun.sigproxy.controller.config;

import com.unifun.sigproxy.exception.NoConfigurationException;
import com.unifun.sigproxy.json.model.config.sctp.LinkConfig;
import com.unifun.sigproxy.json.model.config.sctp.SctpConfig;
import com.unifun.sigproxy.json.model.config.sctp.SctpServerConfig;
import com.unifun.sigproxy.service.SctpConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

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

    @GetMapping(value = "/link/{linkName}", produces = "application/json")
    @ResponseBody
    public ResponseEntity<LinkConfig> getLinkInfo(@PathVariable String linkName) {
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

    @PostMapping(value = "/link/{linkName}", consumes = "application/json")
    public void postLinkInfo(@RequestBody LinkConfig linkConfig, @PathVariable String linkName) throws NoConfigurationException {
        if (!linkConfig.getLinkName().equals(linkName)) {
            throw new IllegalArgumentException("Incorrect mapping between config and request. Link name are not the same.");
        }
        sctpConfigService.setLinkConfig(linkConfig);
    }

    @PutMapping(value = "/link/{linkName}", consumes = "application/json")
    public void putLinkInfo(@RequestBody LinkConfig linkConfig, @PathVariable String linkName) throws NoConfigurationException {
        if (!linkConfig.getLinkName().equals(linkName)) {
            throw new IllegalArgumentException("Incorrect mapping between config and request. Link name are not the same.");
        }
        sctpConfigService.updateLinkConfig(linkConfig);
    }

    @GetMapping(value = "/link", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Set<LinkConfig>> getLinkInfo() throws NoConfigurationException {
        return ResponseEntity.ok(sctpConfigService.getLinkConfig());
    }

    @PostMapping(value = "/link", consumes = "application/json")
    public void postLinkInfo(@RequestBody Set<LinkConfig> linkConfig) throws NoConfigurationException {
        sctpConfigService.setLinkConfig(linkConfig);
    }

    @GetMapping(value = "/server", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Set<SctpServerConfig>> getServerInfo() throws NoConfigurationException {
        return ResponseEntity.ok(sctpConfigService.getServerConfig());
    }

    @PostMapping(value = "/server", consumes = "application/json")
    public void postServerInfo(@RequestBody Set<SctpServerConfig> serverConfig) throws NoConfigurationException {
        sctpConfigService.setServerConfig(serverConfig);
    }

    @GetMapping(value = "/server/{serverName}", produces = "application/json")
    @ResponseBody
    public ResponseEntity<SctpServerConfig> getServerInfo(@PathVariable String serverName) {
        try {
            Optional<SctpServerConfig> serverConfig = sctpConfigService.getServerConfig(serverName);
            if (serverConfig.isPresent()) {
                return ResponseEntity.ok(serverConfig.get());
            }
            return ResponseEntity.noContent().build();
        } catch (NoConfigurationException e) {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping(value = "/server/{serverName}", consumes = "application/json")
    public void postServerInfo(@RequestBody SctpServerConfig serverConfig, @PathVariable String serverName) throws NoConfigurationException {
        if (!serverConfig.getServerName().equals(serverName)) {
            throw new IllegalArgumentException("Incorrect mapping between config and request. Link name are not the same.");
        }
        sctpConfigService.setServerConfig(serverConfig);
    }

    @PutMapping(value = "/server/{serverName}", consumes = "application/json")
    public void putServerInfo(@RequestBody SctpServerConfig serverConfig, @PathVariable String serverName) throws NoConfigurationException {
        if (!serverConfig.getServerName().equals(serverName)) {
            throw new IllegalArgumentException("Incorrect mapping between config and request. Link name are not the same.");
        }
        sctpConfigService.updateServerConfig(serverConfig);
    }


}
