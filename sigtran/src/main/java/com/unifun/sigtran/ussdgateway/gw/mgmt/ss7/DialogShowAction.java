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
public class DialogShowAction extends Action {

    public DialogShowAction(String name, Gateway gateway) {
        super(name, gateway);
    }

    @Override
    public String exec(String cmd, StringTokenizer tokenizer) {
        String builder = "-----------------------------------------------------------------------------------------------------------------------------------\n" +
                String.format("%25s  %5s\n", "PARAMETER", "VALUE") +
                "-----------------------------------------------------------------------------------------------------------------------------------\n" +
                String.format("%25s  %5d\n", "dialog timeout", getGateway().getTcapService().getTcapStack(getGateway().getMobile().getNAME()).getDialogIdleTimeout()) +
                String.format("%25s  %5d\n", "invoke timeout", getGateway().getTcapService().getTcapStack(getGateway().getMobile().getNAME()).getInvokeTimeout()) +
                String.format("%25s  %5d\n", "max dialogs", getGateway().getTcapService().getTcapStack(getGateway().getMobile().getNAME()).getMaxDialogs());
        return builder;
    }

}
