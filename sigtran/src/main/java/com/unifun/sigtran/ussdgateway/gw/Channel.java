/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.gw;

import com.unifun.sigtran.ussdgateway.gw.context.ExecutionContext;
import com.unifun.sigtran.ussdgateway.map.dto.JsonMessage;

import java.util.concurrent.Future;

/**
 * @author okulikov
 */
public interface Channel {
    /**
     * Starts channel.
     *
     * @throws Exception
     */
    void start() throws Exception;

    /**
     * Send asynchronously given message.
     *
     * @param url
     * @param msg
     * @param context
     * @return
     */
    Future send(String url, JsonMessage msg, ExecutionContext context);

    /**
     * Stops this channel.
     */
    void stop();
}
