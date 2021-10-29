package com.unifun.sigtran.ussdgateway.service.sendrequest;

import java.util.concurrent.CompletableFuture;

/**
 * @author asolopa
 */
public interface SendUssdRequestService {
    CompletableFuture<String> sendUssdRequest(String requestBody);
}
