/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.gw.mgmt;

import com.unifun.sigtran.ussdgateway.gw.Gateway;

import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * @author okulikov
 */
public abstract class Action {
    protected final HashMap<String, Action> child = new HashMap<>();
    private final String name;
    private final Gateway gateway;

    public Action(String name, Gateway gateway) {
        this.name = name;
        this.gateway = gateway;
    }

    public Action add(Action cmd) {
        child.put(cmd.name, cmd);
        return this;
    }

    public Gateway getGateway() {
        return gateway;
    }

    public abstract String exec(String cmd, StringTokenizer tokenizer) throws Exception;


    public void validate(Command cmd, String... params) {
        for (String s : params) {
            if (!cmd.isSelected(s)) {
                throw new IllegalArgumentException(s + " must be specified");
            }
        }
    }

}
