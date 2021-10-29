/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.gw.el;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author okulikov
 */
public class Expression {
    private final String PATTERN;
    private final List<Operator> OPERATORS;

    public Expression(String expression) {
        String[] tokens = expression.split(">>");
        PATTERN = tokens[0];

        final ArrayList<Operator> list = new ArrayList<>();
        for (int i = 1; i < tokens.length; i++) {
            if (tokens[i].startsWith("ins")) {
                list.add(new Ins(tokens[i]));
            } else if (tokens[i].startsWith("rem")) {
                list.add(new Rem(tokens[i]));
            }
        }

        OPERATORS = Collections.unmodifiableList(list);
    }

    public boolean matches(String arg) {
        return arg.matches(PATTERN);
    }

    public String exec(String arg) {
        String res = arg;
        for (Operator op : OPERATORS) {
            res = op.exec(res);
        }
        return res;
    }
}
