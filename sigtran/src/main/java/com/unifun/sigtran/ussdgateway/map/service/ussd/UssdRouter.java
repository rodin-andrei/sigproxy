/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.map.service.ussd;

import com.unifun.sigtran.ussdgateway.gw.config.UssdConfig.UssdRouteConfig;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * @author okulikov
 */
public class UssdRouter {

    private final ArrayList<UssdRoute> routes = new ArrayList();

    /**
     * Creates new instance of this router.
     */
    public UssdRouter() {
    }

    public Collection<UssdRoute> getRoutes() {
        return Collections.unmodifiableList(routes);
    }

    /**
     * Finds route matching given selector.
     *
     * @param selector
     * @return
     */
    public UssdRoute find(String selector) {
        for (UssdRoute route : routes) {
            if (selector.matches(route.pattern())) {
                return route;
            }
        }
        return null;
    }

    public void insert(int pos, UssdRouteConfig cfg) {
        String[] dst = new String[cfg.getDestinations().size()];
        cfg.getDestinations().toArray(dst);

        if (pos == -1) {
            routes.add(new UssdRoute(cfg.getSelector(), dst, cfg.getFailureDestination()));
        } else {
            routes.add(pos, new UssdRoute(cfg.getSelector(), dst, cfg.getFailureDestination()));
        }
    }

    public void remove(int i) {
        routes.remove(i);
    }

    public void clear() {
        routes.clear();
    }
}
