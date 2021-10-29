package com.unifun.sigtran.ussdgateway.gw.context;

import com.unifun.sigtran.ussdgateway.map.dto.JsonMessage;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.CompletableFuture;

/**
 * @author asolopa
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
@Log4j2
@Data
public class HttpCompletableFutureContext implements ExecutionContext {
    private final CompletableFuture<String> completable = new CompletableFuture<>();


    @Override
    @Async
    public void completed(JsonMessage jsonMessage) {
        completable.complete(jsonMessage.toString());
    }

    @Override
    @Async
    public void failed(Exception e) {
        log.error(e.getMessage(), e);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        completable.complete(sw.toString());
    }

    @Override
    public void cancelled() {
        log.info("Canceled");
    }

    @Override
    public void ack(JsonMessage msg) {
        log.info("Ack");
    }
}
