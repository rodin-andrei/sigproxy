/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template ocsDir, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.gw;


import com.unifun.sigtran.ussdgateway.map.dto.JsonMessage;
import lombok.extern.log4j.Log4j2;
import org.restcomm.protocols.ss7.m3ua.As;
import org.restcomm.protocols.ss7.m3ua.RouteAs;
import org.restcomm.protocols.ss7.m3ua.impl.M3UAManagementImpl;
import org.restcomm.protocols.ss7.sccp.Rule;
import org.restcomm.protocols.ss7.sccp.SccpStack;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This class manages collection of SendRoutingInfoSm queries.
 *
 * @author okulikov
 */
@Log4j2
public class QueryPool {

    private final String prefix;
    private final Gateway gateway;

    //pool of objets
    private final AtomicReference<Map<String, String>> pool = new AtomicReference();
    private final File poolDir;
    private Iterator<String> it;
    private List<File> files = new ArrayList();
    //date&time when messages were loaded
    private Date lastReload = new Date(0);


    /**
     * Creates new instance of this collection.
     *
     * @param prefix
     * @param gateway
     * @param path
     */
    public QueryPool(String prefix, Gateway gateway, String path) {
        this.prefix = prefix;
        this.gateway = gateway;
        if (Files.exists(Paths.get(path))) {
            poolDir = new File(path);
        } else {
            poolDir = new File("./");
        }
    }

    /**
     * Provides access to query with given name.
     *
     * @param name
     * @return
     */
    public String query(String name) {
        return pool.get().get(name);
    }

    /**
     * Indicates is this pool empty or not.
     *
     * @return
     */
    public boolean isEmpty() {
        return pool.get().isEmpty();
    }

    /**
     * Gets query based on round robin strategy.
     *
     * @return
     */
    public String nextQuery() {
        if (it == null) {
            it = pool.get().values().iterator();
        }

        if (!it.hasNext()) {
            it = pool.get().values().iterator();
        }

        return it.next();
    }


    //TODO check correct work
    public JsonMessage checkAndGetJsonMessage() {
        JsonMessage jsonMessage = null;
        SccpStack sccpStack = gateway.getSccpService().getSccpProvider(gateway.getMobile().getNAME()).getSccpStack();


        for (int i = 0; i < pool.get().size(); i++) {
            jsonMessage = new JsonMessage(this.nextQuery());
            final var gtDigits = jsonMessage.getSccp().getCalledPartyAddress().getGlobalTitle().getDigits();
            final var ni = jsonMessage.getSccp().getCalledPartyAddress().getGlobalTitle().getNatureOfAddressIndicator().toUpperCase();

            Collection<Rule> rules = sccpStack.getRouter().getRules().values();
            for (Rule rule : rules) {
                if (rule.getPattern().getGlobalTitle().getDigits().equals(gtDigits)) {
                    int primaryAddressId = rule.getPrimaryAddressId();
                    SccpAddress routingAddress = sccpStack.getRouter().getRoutingAddress(primaryAddressId);
                    int signalingPointCode = routingAddress.getSignalingPointCode();

                    M3UAManagementImpl mtp3UserPart = (M3UAManagementImpl) sccpStack.getMtp3UserPart(1);
                    Map<String, RouteAs> route = mtp3UserPart.getRoute();
                    boolean b = route.entrySet()
                            .stream()
                            .filter(stringRouteAsEntry -> stringRouteAsEntry.getKey().split(":")[0].equals(String.valueOf(signalingPointCode)))
                            .anyMatch(stringRouteAsEntry -> {
                                As[] asArray = stringRouteAsEntry.getValue().getAsArray();
                                return Arrays.stream(asArray)
                                        .anyMatch(As::isUp);
                            });

                    if (b) {
                        log.debug("Successfully Translated message: " + jsonMessage);
                        return jsonMessage;
                    }
                }
            }
        }
        log.warn("Unsuccessful translated message: " + jsonMessage);
        return jsonMessage;
    }

    public void reload() throws Exception {
        lastReload = new Date();

        files = Collections.unmodifiableList(listFiles());
        HashMap<String, String> items = new HashMap();

        for (File f : files) {
            log.info("Reloading  " + f.getName());
            try (FileInputStream fin = new FileInputStream(f)) {
                items.put(name(f), IOUtils.readString(fin));
            }
        }
        this.pool.set(Collections.unmodifiableMap(items));
    }

    /**
     * Gets file name without extension.
     *
     * @param f
     * @return
     */
    private String name(File f) {
        return f.getName().substring(0, f.getName().indexOf('.'));
    }

    public boolean isModified() {
        ArrayList<File> list = listFiles();
        if (list.size() != files.size()) {
            return true;
        }
        return list.stream().anyMatch((f) -> (new Date(f.lastModified()).after(lastReload)));
    }

    /**
     * List ocs related files located in working directory.
     *
     * @return
     */
    private ArrayList<File> listFiles() {
        ArrayList<File> items = new ArrayList();
        File[] newfiles = poolDir.listFiles();
        if (newfiles != null) {
            for (File newfile : newfiles) {
                if (newfile.getName().startsWith(prefix + "-")) {
                    items.add(newfile);
                }
            }
        }
        return items;
    }

}
