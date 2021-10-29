/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.gw.config;

import javax.json.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * @author okulikov
 */
public class UssdConfig {

    private final ArrayList<UssdRouteConfig> routes = new ArrayList();

    public UssdConfig() {

    }

    public UssdConfig(JsonObject obj) {
        JsonArray v = obj.getJsonArray("routes");
        if (v != null) {
            for (int i = 0; i < v.size(); i++) {
                routes.add(new UssdRouteConfig(v.getJsonObject(i)));
            }
        }
    }

    public void insert(int pos, UssdRouteConfig cfg) {
        if (pos > 0) {
            routes.add(pos, cfg);
        } else {
            routes.add(cfg);
        }
    }

    public void remove(int pos) {
        routes.remove(pos);
    }

    public Collection<UssdRouteConfig> routes() {
        return Collections.unmodifiableList(routes);
    }

    public void clear() {
        routes.clear();
    }

    public JsonObjectBuilder toJson() {
        JsonArrayBuilder r = Json.createArrayBuilder();
        routes.stream().forEach((c) -> {
            r.add(c.toJson());
        });

        return Json.createObjectBuilder().add("routes", r);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\n");
        builder.append('"');
        builder.append("routes");
        builder.append('"');
        builder.append(':');
        builder.append('[');

        Iterator<UssdRouteConfig> it = routes.iterator();
        if (it.hasNext()) {
            builder.append(it.next());
        }

        while (it.hasNext()) {
            builder.append(',');
            builder.append(it.next());
        }

        builder.append(']');
        builder.append("}\n");
        return builder.toString();
    }

    public class UssdRouteConfig {

        private final ArrayList<String> primaryDestination = new ArrayList();
        private String selector;
        private String failureDestination;

        public UssdRouteConfig() {
        }

        public UssdRouteConfig(JsonObject obj) {
            selector = obj.getString("selector");
            failureDestination = obj.getString("failure-destination");
            JsonArray dst = obj.getJsonArray("primary-destination");
            for (int i = 0; i < dst.size(); i++) {
                primaryDestination.add(dst.getString(i));
            }
        }

        public String getSelector() {
            return selector;
        }

        public void setSelector(String selector) {
            this.selector = selector;
        }

        public String getFailureDestination() {
            return failureDestination;
        }

        public void setFailureDestination(String failureDestination) {
            this.failureDestination = failureDestination;
        }

        public void addDestination(String dest) {
            primaryDestination.add(dest);
        }

        public void removeDestination(String dest) {
            primaryDestination.remove(dest);
        }

        public Collection getDestinations() {
            return Collections.unmodifiableCollection(primaryDestination);
        }

        public JsonObjectBuilder toJson() {
            JsonArrayBuilder pu = Json.createArrayBuilder();
            primaryDestination.stream().forEach((d) -> {
                pu.add(d);
            });

            JsonObjectBuilder builder = Json.createObjectBuilder();
            builder
                    .add("selector", selector)
                    .add("primary-destination", pu)
                    .add("failure-destination", failureDestination);
            return builder;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("{\n");

            builder.append('"');
            builder.append("selector");
            builder.append('"');
            builder.append(":");
            builder.append('"');
            builder.append(selector);
            builder.append('"');

            builder.append(',');
            builder.append('\n');

            builder.append('"');
            builder.append("primary-destination");
            builder.append('"');
            builder.append(":");
            builder.append("[");

            Iterator<String> it = primaryDestination.iterator();
            if (it.hasNext()) {
                builder.append(it.next());
            }

            while (it.hasNext()) {
                builder.append(',');
                builder.append(it.next());
            }

            builder.append("]");

            builder.append(',');
            builder.append('\n');

            builder.append('"');
            builder.append("failure-destination");
            builder.append('"');
            builder.append(":");
            builder.append('"');
            builder.append(failureDestination);
            builder.append('"');

            builder.append("\n}");
            return builder.toString();
        }
    }
}
