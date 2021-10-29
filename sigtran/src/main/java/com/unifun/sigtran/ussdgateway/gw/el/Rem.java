/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.gw.el;

/**
 * @author okulikov
 */
public class Rem implements Operator {

    private final int pos;
    private final int amount;

    public Rem(int pos, int amount) {
        this.pos = pos;
        this.amount = amount;
    }

    public Rem(String s) {
        s = s.substring(3);
        s = s.replace('(', ' ');
        s = s.replace(')', ' ');
        s = s.trim();

        String[] tokens = s.split(",");
        pos = Integer.parseInt(tokens[0].trim());
        amount = Integer.parseInt(tokens[1].trim());
    }

    @Override
    public String exec(String arg) {
        return new StringBuilder(arg).replace(pos, pos + amount, "").toString();
    }

}
