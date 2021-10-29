/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.gw.mgmt;

import com.unifun.sigtran.ussdgateway.gw.Gateway;

import java.util.StringTokenizer;

/**
 * @author okulikov
 */
public class Aggregator extends Action {

    public Aggregator(String name, Gateway gateway) {
        super(name, gateway);
    }

    @Override
    public String exec(String cmd, StringTokenizer tokenizer) throws Exception {
        if (tokenizer.hasMoreTokens()) {
            Action command = child.get(tokenizer.nextToken());
            return command != null ? command.exec(cmd, tokenizer) : "";
        }
        return "";
    }

}
