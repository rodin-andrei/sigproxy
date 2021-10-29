/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.gw;

/**
 * @author okulikov
 */
public class UnknownProtocolException extends Exception {

    /**
     * Creates a new instance of <code>UnknownProtocol</code> without detail
     * message.
     */
    public UnknownProtocolException() {
    }

    /**
     * Constructs an instance of <code>UnknownProtocol</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public UnknownProtocolException(String msg) {
        super(msg);
    }
}
