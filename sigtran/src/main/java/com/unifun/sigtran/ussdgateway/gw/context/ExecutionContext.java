/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.gw.context;

import com.unifun.sigtran.ussdgateway.map.dto.JsonMessage;
import org.apache.http.concurrent.FutureCallback;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author okulikov
 */
public interface ExecutionContext extends FutureCallback<JsonMessage> {
    ExecutorService EXECUTOR = Executors.newCachedThreadPool();

    void ack(JsonMessage msg);
}
