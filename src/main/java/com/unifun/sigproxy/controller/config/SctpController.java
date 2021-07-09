package com.unifun.sigproxy.controller.config;

import com.unifun.sigproxy.models.config.sctp.ClientLink;
import com.unifun.sigproxy.models.config.sctp.SctpServer;
import com.unifun.sigproxy.service.SctpConfigService;
import com.unifun.sigproxy.service.SctpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("sctp/conf")
@RequiredArgsConstructor
public class SctpController {

    private final SctpConfigService sctpConfigService;
    private final SctpService sctpService;

    @GetMapping(value = "/link", produces = "application/json")
    @ResponseBody
    public ResponseEntity<List<ClientLink>> getLinkInfo() {
        return ResponseEntity.ok(sctpConfigService.getLinkConfigs());
    }

    @GetMapping(value = "/link/{linkName}", produces = "application/json")
    @ResponseBody
    public ResponseEntity<ClientLink> getLinkInfo(@PathVariable String linkName) {
        Optional<ClientLink> linkConfig = sctpConfigService.getLinkConfig(linkName);
        return linkConfig.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping(value = "/link", consumes = "application/json")
    public void postLinkInfo(@RequestBody ClientLink clientLink) {
        sctpConfigService.addLink(clientLink);
        sctpService.addLink(clientLink);
    }

    @PostMapping(value = "/links", consumes = "application/json")
    public void postLinkInfo(@RequestBody Set<ClientLink> clientLinks) {
        sctpConfigService.addLinks(clientLinks);
        sctpService.addLinks(clientLinks);
    }


    @GetMapping(value = "/server", produces = "application/json")
    @ResponseBody
    public ResponseEntity<List<SctpServer>> getServerInfo() {
        return ResponseEntity.ok(sctpConfigService.getServerConfigs());
    }

    @GetMapping(value = "/server/{serverName}", produces = "application/json")
    @ResponseBody
    public ResponseEntity<SctpServer> getServerInfo(@PathVariable String serverName) {
        return sctpConfigService.getServerConfig(serverName)
                .map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping(value = "/servers", consumes = "application/json")
    public void postServerInfo(@RequestBody Set<SctpServer> serverConfigs) {
        sctpConfigService.addServerConfigs(serverConfigs);
        sctpService.addServers(serverConfigs);
    }

    @PostMapping(value = "/server", consumes = "application/json")
    public void postServerInfo(@RequestBody SctpServer serverConfig) {
        sctpConfigService.addServerConfig(serverConfig);
        sctpService.addServer(serverConfig);
    }
}
