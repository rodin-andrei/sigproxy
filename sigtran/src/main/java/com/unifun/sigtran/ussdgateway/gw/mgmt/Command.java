/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.gw.mgmt;

import java.text.ParseException;
import java.util.*;

/**
 * @author okulikov
 */
public class Command {

    private final HashMap<String, String> selector = new HashMap<>();
    private final HashMap<String, String> additions = new HashMap<>();
    private final ArrayList<String> removals = new ArrayList<>();

    public Command(StringTokenizer tokenizer) throws ParseException {
        while (tokenizer.hasMoreElements()) {
            String token = tokenizer.nextToken();
            switch (token) {
                case "set":
                    if (!tokenizer.hasMoreTokens()) {
                        throw new ParseException("Syntax error: Parameter name expected after set", 0);
                    }

                    String name = tokenizer.nextToken();

                    if (!tokenizer.hasMoreTokens()) {
                        throw new ParseException("Syntax error: Value expected for " + name, 0);
                    }

                    String value = tokenizer.nextToken();

                    additions.put(name, value);
                    break;
                case "rem":
                    if (!tokenizer.hasMoreTokens()) {
                        throw new ParseException("Syntax error: Parameter name expected after set", 0);
                    }


                    value = tokenizer.nextToken();

                    removals.add(value);
                    break;
                default:
                    if (!tokenizer.hasMoreTokens()) {
                        throw new ParseException("Syntax error: Value expected for " + token, 0);
                    }
                    selector.put(token, tokenizer.nextToken());
            }
        }
    }

    public Map<String, String> getSelector() {
        return selector;
    }

    public Map<String, String> getAdditions() {
        return additions;
    }

    public List<String> getRemovals() {
        return removals;
    }

    public boolean isAdded(String name) {
        return additions.containsKey(name);
    }

    public boolean isRemoved(String name) {
        return removals.contains(name);
    }

    public String valueOf(String name) {
        return additions.get(name);
    }

    public boolean isSelected(String name) {
        return selector.containsKey(name);
    }

    public String getSelection(String name) {
        return selector.get(name);
    }
}
