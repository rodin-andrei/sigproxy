/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.gw.mgmt.ussd;

import com.unifun.sigtran.ussdgateway.gw.Gateway;
import com.unifun.sigtran.ussdgateway.gw.config.UssdConfig;
import com.unifun.sigtran.ussdgateway.gw.mgmt.Action;
import com.unifun.sigtran.ussdgateway.map.service.ussd.MapUssdChannel;
import com.unifun.sigtran.ussdgateway.map.service.ussd.UssdRouter;

import java.util.StringTokenizer;

/**
 * @author okulikov
 */
public class UssdClearAction extends Action {

    public UssdClearAction(String name, Gateway gateway) {
        super(name, gateway);
    }

    @Override
    public String exec(String cmd, StringTokenizer tokenizer) {
        try {
            final UssdConfig ussdConfig = getGateway().getConfig().getUssdConfig();
            final UssdRouter ussdRouter = ((MapUssdChannel) getGateway().channel("map://ussd")).ussdRouter();

            ussdRouter.clear();
            ussdConfig.clear();

            return "";
        } catch (Throwable e) {
            return e.getMessage();
        }
    }

}
