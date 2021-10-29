/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.gw.context;

import com.unifun.sigtran.ussdgateway.gw.Channel;
import com.unifun.sigtran.ussdgateway.gw.Gateway;
import com.unifun.sigtran.ussdgateway.map.dto.JsonCallback;
import com.unifun.sigtran.ussdgateway.map.dto.JsonMessage;
import lombok.extern.log4j.Log4j2;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author okulikov
 */
@Log4j2
public class HttpLocalAsyncContext implements ExecutionContext {
    private final Gateway gateway;
    private final AsyncContext asyncContext;
    private final JsonCallback callback;
    private JsonMessage msg;

    public HttpLocalAsyncContext(Gateway gateway, AsyncContext asyncContext, JsonCallback callback) {
        this.gateway = gateway;
        this.asyncContext = asyncContext;
        this.callback = callback;
    }


    @Override
    public void completed(JsonMessage msg) {
        this.msg = msg;
        try {
            Channel httpChannel = gateway.channel(callback.getPrimaryDestination());
            httpChannel.send(callback.getPrimaryDestination(), msg, new SpareContext(this));
        } catch (Exception e) {
            failed(e);
        }
    }

    @Override
    public void failed(Exception e) {
        ExecutionContext context = new SpareContext(this);
        try {
            Channel channel = gateway.channel(callback.getSpareDestination());
            channel.send(callback.getSpareDestination(), msg, context);
        } catch (Exception ex) {
            context.failed(ex);
        }
    }

    @Override
    public void cancelled() {
        //close dialog
    }

    @Override
    public void ack(JsonMessage msg) {
        EXECUTOR.execute(() -> {
            try {
                String content = msg.toString() + "\r\n";
                asyncContext.getResponse().setContentType("application/json");

                ((HttpServletResponse) asyncContext.getResponse()).setStatus(HttpServletResponse.SC_OK);
                ((HttpServletResponse) asyncContext.getResponse()).addHeader("Connection", "keep-alive");
                asyncContext.getResponse().setContentLength(content.length());

                asyncContext.getResponse().getWriter().println(content);
                asyncContext.getResponse().flushBuffer();
            } catch (IOException e) {
                log.error("IO error: ", e);
            } finally {
                asyncContext.complete();
            }
        });
    }

    private class SpareContext implements ExecutionContext {
        private final ExecutionContext parent;

        public SpareContext(ExecutionContext parent) {
            this.parent = parent;
        }

        @Override
        public void ack(JsonMessage msg) {
        }

        @Override
        public void completed(JsonMessage msg) {
            try {
                //send over map channel using the original context
                Channel ussdChannel = gateway.channel("map://ussd");
                ussdChannel.send(null, msg, parent);
            } catch (Exception e) {
                failed(e);
            }
        }

        @Override
        public void failed(Exception ex) {
            //would be nice to close dialog
        }

        @Override
        public void cancelled() {
            //would be nice to close dialog
        }

    }
}
