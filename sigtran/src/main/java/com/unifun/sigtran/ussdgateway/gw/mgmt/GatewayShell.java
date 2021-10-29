/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.gw.mgmt;

import com.unifun.sigtran.ussdgateway.gw.Gateway;
import com.unifun.sigtran.ussdgateway.gw.mgmt.cfg.CfgSaveAction;
import com.unifun.sigtran.ussdgateway.gw.mgmt.state.StateShowAction;
import com.unifun.sigtran.ussdgateway.gw.mgmt.stats.StatsResetAction;
import com.unifun.sigtran.ussdgateway.gw.mgmt.stats.StatsShowAction;
import com.unifun.sigtran.ussdgateway.gw.mgmt.ussd.OCSReloadAction;
import com.unifun.sigtran.ussdgateway.gw.mgmt.ussd.UssdClearAction;
import com.unifun.sigtran.ussdgateway.gw.mgmt.ussd.UssdInsertAction;
import com.unifun.sigtran.ussdgateway.gw.mgmt.ussd.UssdShowAction;
import lombok.extern.log4j.Log4j2;

import java.util.StringTokenizer;

/**
 * @author okulikov
 */
@Log4j2
public class GatewayShell {

    private final static String TOMCAT = "http";
    private final static String MOBILE = "mobile";
    private final Gateway gateway;
    private final MobileShell ss7Shell;
    private final Aggregator ocsShell;
    private final Aggregator ussdShell;
    private final Aggregator confShell;
    private final Aggregator statsShell;
    private final Aggregator stateShell;

    public GatewayShell(Gateway gateway) {
        this.gateway = gateway;

        ss7Shell = new MobileShell(gateway);

        ocsShell = new Aggregator("ocs", gateway);
        ocsShell.add(new OCSReloadAction("reload", gateway));

        ussdShell = new Aggregator("ussd", gateway);
        ussdShell.add(new UssdShowAction("show", gateway));
        ussdShell.add(new UssdInsertAction("insert", gateway));
        ussdShell.add(new UssdClearAction("clear", gateway));

        confShell = new Aggregator("config", gateway);
        confShell.add(new CfgSaveAction("save", gateway));

        statsShell = new Aggregator("stats", gateway);
        statsShell.add(new StatsShowAction("show", gateway));
        statsShell.add(new StatsResetAction("reset", gateway));

        stateShell = new Aggregator("state", gateway);
        stateShell.add(new StateShowAction("show", gateway));

    }

    public String exec(String cmd) {
        StringTokenizer tokenizer = new StringTokenizer(cmd, " ");
        String[] args = cmd.split("\\s");

        try {
            switch (tokenizer.nextToken()) {
                case MOBILE:
                    return ss7Shell.exec(cmd, tokenizer);
                case "ussd":
                    return ussdShell.exec(cmd, tokenizer);
                case "config":
                    return confShell.exec(cmd, tokenizer);
                case "ocs":
                    return ocsShell.exec(cmd, tokenizer);
                case "stats":
                    return this.statsShell.exec(cmd, tokenizer);
                case "shutdown":
                    System.exit(0);
                    return "";
                case "state":
                    return this.stateShell.exec(cmd, tokenizer);
                default:
                    return "Unknown command: " + args[0];
            }
        } catch (Throwable t) {
            log.error("Could not execute command", t);
            String msg = t.getMessage();
            if (msg != null && msg.length() > 0) {
                return msg + "\n";
            }
            return t.getClass().getName();
        }
    }

}
