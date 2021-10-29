/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.gw.mgmt.ss7;

import com.unifun.sigtran.ussdgateway.gw.Gateway;
import com.unifun.sigtran.ussdgateway.gw.mgmt.Action;
import org.restcomm.protocols.ss7.m3ua.As;
import org.restcomm.protocols.ss7.m3ua.M3UAManagement;
import org.restcomm.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.restcomm.protocols.ss7.m3ua.parameter.RoutingContext;

import java.util.List;
import java.util.StringTokenizer;

/**
 * @author okulikov
 */
public class SubsystemShowAction extends Action {

    public SubsystemShowAction(String name, Gateway gateway) {
        super(name, gateway);
    }

    @Override
    public String exec(String cmd, StringTokenizer tokenizer) throws Exception {
        String name = tokenizer.hasMoreTokens() ? tokenizer.nextToken() : "all";
        StringBuilder builder = new StringBuilder();
        switch (name) {
            case "all":
                builder.append("-----------------------------------------------------------------------------------------------------------------------------------");
                builder.append("\r\n");
                builder.append(String.format("%20s %5s %5s %5s %5s %5s %10s %21s %18s %10s", "AS NAME", "OPC", "DPC", "NI", "FUNC", "TYPE", "IPSP", "NETWORK APPEARANCE", "ROUTING CONTEXT", "STATE"));
                builder.append("\r\n");
                builder.append("-----------------------------------------------------------------------------------------------------------------------------------");
                builder.append("\r\n");

                M3UAManagement management = getGateway().getM3uaService().getManagement(getGateway().getMobile().getNAME());
                List<As> list = management.getAppServers();
                list.forEach((as) -> {
                    builder.append(String.format("%20s %5d %5d %5d %5s %5s %10s %21s %18s %10s",
                            as.getName(),
                            //TODO fix this params
                            -1,
                            -1,
                            -1,
                            as.getFunctionality().name(),
                            as.getExchangeType().name(),
                            as.getIpspType().name(),
                            na(as.getNetworkAppearance()),
                            rc(as.getRoutingContext()),
                            as.getState().getName()));
                    builder.append("\r\n");
                });
                break;
            default:
                break;
        }

        return builder.toString();

    }

    private String na(NetworkAppearance na) {
        if (na == null) {
            return "";
        }
        return Long.toString(na.getNetApp());
    }

    private String rc(RoutingContext rc) {
        if (rc == null) {
            return "";
        }

        long[] v = rc.getRoutingContexts();
        if (v == null || v.length == 0) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        builder.append(v[0]);

        for (int i = 1; i < v.length; i++) {
            builder.append(',');
            builder.append(v[i]);
        }

        return builder.toString();

    }

    private String address(String[] list) {
        return (list != null && list.length > 0) ? list[0] : "-";
    }

}
