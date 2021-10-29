/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.map.service.sm;

/**
 * @author okulikov
 */
public class Resolver {
    public String resolve(String json, String name, String value) {
        String fqn = "\\$\\{" + name + "\\}";
        return json.replaceAll(fqn, value);
    }
}
