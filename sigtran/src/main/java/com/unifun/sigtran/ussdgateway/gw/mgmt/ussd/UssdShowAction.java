/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.gw.mgmt.ussd;

import com.unifun.sigtran.ussdgateway.gw.Gateway;
import com.unifun.sigtran.ussdgateway.gw.mgmt.Action;
import com.unifun.sigtran.ussdgateway.map.service.ussd.MapUssdChannel;
import com.unifun.sigtran.ussdgateway.map.service.ussd.UssdRouter;

import java.util.StringTokenizer;

/**
 * @author okulikov
 */
public class UssdShowAction extends Action {

    public UssdShowAction(String name, Gateway gateway) {
        super(name, gateway);
    }

    @Override
    public String exec(String cmd, StringTokenizer tokenizer) {
        try {
            UssdRouter ussdRouter = ((MapUssdChannel) getGateway().channel("map://ussd")).ussdRouter();
            StringBuilder builder = new StringBuilder();
            builder.append("-----------------------------------------------------------------------------------------------------------------------------------\n");
            builder.append(String.format("%10s %50s %50s", "USSD CODE", "PRIMARY URL", "SECONDARY URL\n"));
            builder.append("-----------------------------------------------------------------------------------------------------------------------------------\n");

            ussdRouter.getRoutes().forEach((r) -> {
                builder.append(String.format("%10s %50s %50s\n", r.pattern(), r.primaryDestionation()[0], r.failureDestination()));
            });
            return builder.toString();
        } catch (Exception e) {
            return e.getMessage();
        }
    }

}
