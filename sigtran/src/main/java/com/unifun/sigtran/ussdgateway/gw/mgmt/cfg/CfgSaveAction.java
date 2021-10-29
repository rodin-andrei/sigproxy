/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.gw.mgmt.cfg;

import com.unifun.sigtran.ussdgateway.gw.Gateway;
import com.unifun.sigtran.ussdgateway.gw.mgmt.Action;

import java.util.StringTokenizer;

/**
 * @author okulikov
 */
public class CfgSaveAction extends Action {

    public CfgSaveAction(String name, Gateway gateway) {
        super(name, gateway);
    }

    @Override
    public String exec(String cmd, StringTokenizer tokenizer) {
        try {
            getGateway().getConfig().write();
            return "";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

}
