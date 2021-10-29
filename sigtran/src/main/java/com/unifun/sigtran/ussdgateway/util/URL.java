/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author okulikov
 */
public class URL {
    private final String protocol;
    private final String host;
    private final String path;
    private final HashMap<String, String> parameters = new HashMap<>();

    public URL(String url) {
        int p1 = 0;
        int p2 = url.indexOf(':');

        protocol = url.substring(p1, p2);

        p1 = p2 + 3;
        p2 = url.indexOf("/", p1);

        String hp; //host and port
        String pq = null; //path and query

        if (p2 > 0) {
            hp = url.substring(p1, p2);
            pq = url.substring(p2 + 1);
        } else {
            p2 = url.indexOf("?", p1);
            if (p2 > 0) {
                hp = url.substring(p1, p2);
                pq = url.substring(p2 + 1);
            } else {
                hp = url.substring(p1);
            }
        }

        //split host & port
        p2 = hp.indexOf(":");
        if (p2 > 0) {
            host = hp.substring(0, p2);
        } else {
            host = hp;
        }

        //split path and query
        if (pq != null) {
            p2 = pq.indexOf("?");
            if (p2 > 0) {
                path = pq.substring(0, p2);
                String q = pq.substring(p2 + 1);

                String[] pms = q.split("&");
                for (String param : pms) {
                    String[] s = param.split("=");
                    parameters.put(s[0], s[1]);
                }
            } else {
                if (pq.indexOf("=") > 0) {
                    String[] pms = pq.split("&");
                    for (String param : pms) {
                        String[] s = param.split("=");
                        parameters.put(s[0], s[1]);
                    }
                    path = null;
                } else {
                    path = pq;
                }
            }
        } else {
            path = null;
        }
    }

    public String protocol() {
        return protocol;
    }

    public String host() {
        return host;
    }

    public String path() {
        return path;
    }

    public Map<String, String> params() {
        return parameters;
    }
}
