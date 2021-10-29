/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.gw.mgmt.ss7;

import com.unifun.sigtran.ussdgateway.gw.Gateway;
import com.unifun.sigtran.ussdgateway.gw.mgmt.Action;

import java.util.StringTokenizer;

/**
 * @author okulikov
 */
public class LinkShowAction extends Action {

    public LinkShowAction(String name, Gateway gateway) {
        super(name, gateway);
    }

    @Override
    public String exec(String cmd, StringTokenizer tokenizer) {
        StringBuilder builder = new StringBuilder();
        builder.append("-----------------------------------------------------------------------------------------------------------------------------------");
        builder.append("\r\n");
        builder.append(String.format("%15s %20s %15s %20s %20s %15s %10s", "LINK NAME", "REMOTE ADDRESS", "REMOTE PORT", "PRIMARY ADDRESS", "SECONDARY ADDRESS", "LOCAL PORT", "STATE"));
        builder.append("\r\n");
        builder.append("-----------------------------------------------------------------------------------------------------------------------------------");
        builder.append("\r\n");

        getGateway().getM3uaService().getManagement(getGateway().getMobile().getNAME()).getAspfactories().forEach((asp) -> {
            builder.append(String.format("%15s %20s %15s %20s %20s %15s %10s",
                    asp.getName(),
                    asp.getAssociation().getPeerAddress(),
                    asp.getAssociation().getPeerPort(),
                    asp.getAssociation().getHostAddress(),
                    address(asp.getAssociation().getExtraHostAddresses()),
                    asp.getAssociation().getHostPort(),
                    asp.getAssociation().isUp() ? "UP" : "DOWN"
            ));
            builder.append("\r\n");
        });

        return builder.toString();
    }

    private String address(String[] list) {
        return (list != null && list.length > 0) ? list[0] : "-";
    }

}
