/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.gw.mgmt.stats;

import com.unifun.sigtran.ussdgateway.gw.Gateway;
import com.unifun.sigtran.ussdgateway.gw.mgmt.Action;
import com.unifun.sigtran.ussdgateway.gw.mgmt.CommandParser;
import com.unifun.sigtran.ussdgateway.gw.stats.DialogStats;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.util.StringTokenizer;

/**
 * @author okulikov
 */
public class StatsShowAction extends Action {

    public StatsShowAction(String name, Gateway gateway) {
        super(name, gateway);
    }

    @Override
    public String exec(String cmd, StringTokenizer tokenizer) throws Exception {
        Options options = new Options();
        options.addOption(Option.builder("o").required(true).longOpt("origination").hasArg(true).build());

        CommandParser parser = new CommandParser();
        CommandLine cmdLine = parser.parse(options, tokenizer);

        final String o = cmdLine.getOptionValue("o");
        DialogStats stats = o.equalsIgnoreCase("remote")
                ? getGateway().getUsageStats().getIncDialogs() : getGateway().getUsageStats().getOutDialogs();

        String builder = "-----------------------------------------------------------------------------------------------------------------\n" +
                String.format("%15s %15s %15s %15s %15s %20s %15s %15s %15s", "", "ACTIVE", "STARTED", "REJECTED", "USER ABORTED", "PROVIDER ABORTED", "RELEASED", "TIMEOUT", "CLOSED\n") +
                "-----------------------------------------------------------------------------------------------------------------\n" +
                String.format("%15d %15d %15d %15d %20d %15d %15d %15d\n",
                        stats.getActive(),
                        stats.getStarted(),
                        stats.getRejected(),
                        stats.getUserAborted(),
                        stats.getProviderAborted(),
                        stats.getReleased(),
                        stats.getClosed(),
                        stats.getClosed()) +
                "-----------------------------------------------------------------------------------------------------------------\n";
        return builder;
    }

}
