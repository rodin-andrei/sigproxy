/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.gw.mgmt.stats;

import com.unifun.sigtran.ussdgateway.gw.Gateway;
import com.unifun.sigtran.ussdgateway.gw.mgmt.Action;

import java.util.StringTokenizer;

/**
 * @author okulikov
 */
public class StatsResetAction extends Action {

    public StatsResetAction(String name, Gateway gateway) {
        super(name, gateway);
    }

    @Override
    public String exec(String cmd, StringTokenizer tokenizer) throws Exception {
        getGateway().getUsageStats().getIncDialogs().reset();
        getGateway().getUsageStats().getOutDialogs().reset();
        return "";
    }

}
