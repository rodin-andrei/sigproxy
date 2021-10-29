/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.gw.context;

import com.unifun.sigtran.ussdgateway.map.dto.JsonMessage;
import lombok.extern.log4j.Log4j2;
import org.restcomm.protocols.ss7.map.api.MAPDialog;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author okulikov
 */
@Log4j2
public class HttpLocalContext implements ExecutionContext {

    private final AsyncContext asyncContext;
    private MAPDialog mapDialog;

    public HttpLocalContext(AsyncContext asyncContext) {
        this.asyncContext = asyncContext;
    }


    @Override
    public void completed(JsonMessage msg) {
        EXECUTOR.execute(() -> {
            try {
                String content = msg.toString();
                byte[] data = content.getBytes();

                asyncContext.getResponse().setContentType("application/json");
                asyncContext.getResponse().setCharacterEncoding("UTF-8");

                ((HttpServletResponse) asyncContext.getResponse()).setStatus(HttpServletResponse.SC_OK);
                ((HttpServletResponse) asyncContext.getResponse()).addHeader("Connection", "keep-alive");
                asyncContext.getResponse().setContentLength(data.length);

                asyncContext.getResponse().getOutputStream().write(data);
                asyncContext.getResponse().flushBuffer();
            } catch (IOException e) {
                log.error("IO error: ", e);
            } finally {
                asyncContext.complete();
            }
        });
    }

    @Override
    public void failed(Exception e) {
        try {
            ((HttpServletResponse) asyncContext.getResponse()).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            asyncContext.getResponse().flushBuffer();
        } catch (IOException e1) {
            log.error("IO error: ", e1);
        } finally {
            asyncContext.complete();
        }
    }

    @Override
    public void cancelled() {
    }

    @Override
    public void ack(JsonMessage msg) {
    }

    public MAPDialog getMapDialog() {
        return mapDialog;
    }

    public void setMapDialog(MAPDialog mapDialog) {
        this.mapDialog = mapDialog;
    }
}
