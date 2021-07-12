package com.unifun.sigproxy.controller.config;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("sctp/conf")
@RequiredArgsConstructor
public class SctpController {

//    private final SctpConfigService sctpConfigService;
//    private final SctpService sctpService;
//
//    @PostMapping(value = "/link", consumes = "application/json")
//    public void postLinkInfo(@RequestBody ClientAssociation clientAssociation, @RequestParam String sigtranStack) {
//        sctpConfigService.addLink(clientAssociation, sigtranStack);
//        sctpService.addLink(clientAssociation, sigtranStack);
//    }
//
//    @PostMapping(value = "/links", consumes = "application/json")
//    public void postLinkInfo(@RequestBody Set<ClientAssociation> clientAssociations, @RequestParam String sigtranStack) {
//        sctpConfigService.addLinks(clientAssociations, sigtranStack);
//        sctpService.addLinks(clientAssociations, sigtranStack);
//    }
//
//
//    @PostMapping(value = "/server", consumes = "application/json")
//    public void postServerInfo(@RequestBody SctpServer serverConfig, @RequestParam String sigtranStack) {
//        sctpConfigService.addServerConfig(serverConfig, sigtranStack);
//        sctpService.addServer(serverConfig, sigtranStack);
//    }
//
//    @PostMapping(value = "/servers", consumes = "application/json")
//    public void postServerInfo(@RequestBody Set<SctpServer> serverConfigs, @RequestParam String sigtranStack) {
//        sctpConfigService.addServerConfigs(serverConfigs, sigtranStack);
//        sctpService.addServers(serverConfigs, sigtranStack);
//    }
}
