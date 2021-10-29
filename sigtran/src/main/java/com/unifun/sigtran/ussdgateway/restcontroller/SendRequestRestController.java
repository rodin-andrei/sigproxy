package com.unifun.sigtran.ussdgateway.restcontroller;

import com.unifun.sigtran.ussdgateway.service.sendrequest.SendUssdRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

/**
 * @author asolopa
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/request")
public class SendRequestRestController {
    private final SendUssdRequestService sendUssdRequestService;

    @PostMapping(value = "/ussd")
    public CompletableFuture<String> sendUssd(@RequestBody String json) {
        return sendUssdRequestService.sendUssdRequest(json);
    }
}
