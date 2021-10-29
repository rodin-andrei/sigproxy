/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.gw.el;

/**
 * @author okulikov
 */
public class Ins implements Operator {
    private final int pos;
    private final String value;

    public Ins(int pos, String value) {
        this.pos = pos;
        this.value = value;
    }

    public Ins(String s) {
        s = s.substring(3);
        s = s.replace('(', ' ');
        s = s.replace(')', ' ');
        s = s.trim();

        String[] tokens = s.split(",");
        pos = Integer.parseInt(tokens[0].trim());
        value = tokens[1].trim();
    }

    @Override
    public String exec(String arg) {
        return new StringBuilder(arg).insert(pos, value).toString();
    }

}
