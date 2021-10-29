/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.gw.mgmt.ussd;

import com.unifun.sigtran.ussdgateway.gw.Gateway;
import com.unifun.sigtran.ussdgateway.gw.config.UssdConfig;
import com.unifun.sigtran.ussdgateway.gw.config.UssdConfig.UssdRouteConfig;
import com.unifun.sigtran.ussdgateway.gw.mgmt.Action;
import com.unifun.sigtran.ussdgateway.gw.mgmt.CommandParser;
import com.unifun.sigtran.ussdgateway.map.service.ussd.MapUssdChannel;
import com.unifun.sigtran.ussdgateway.map.service.ussd.UssdRouter;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.util.StringTokenizer;

/**
 * @author okulikov
 */
public class UssdInsertAction extends Action {

    public UssdInsertAction(String name, Gateway gateway) {
        super(name, gateway);
    }

    @Override
    public String exec(String cmd, StringTokenizer tokenizer) throws Exception {
        Options options = new Options();
        options.addOption(Option.builder("s").required(true).longOpt("ussd-string").hasArg(true).build());
        options.addOption(Option.builder("u").required(true).longOpt("url").hasArg(true).build());
        options.addOption(Option.builder("f").required(true).longOpt("failure-url").hasArg(true).build());
        options.addOption(Option.builder("i").required(false).longOpt("index").hasArg(true).build());

        CommandParser parser = new CommandParser();
        CommandLine cmdLine = parser.parse(options, tokenizer);

        final String ussdString = cmdLine.getOptionValue("s");
        final String url = cmdLine.getOptionValue("u");
        final String failureUrl = cmdLine.getOptionValue("f");

        final int i = cmdLine.hasOption("i") ? Integer.parseInt(cmdLine.getOptionValue("i")) : 0;

        UssdConfig ussdConfig = getGateway().getConfig().getUssdConfig();
        UssdRouteConfig cfg = ussdConfig.new UssdRouteConfig();

        cfg.setSelector(ussdString);
        cfg.addDestination(url);
        cfg.setFailureDestination(failureUrl);

        UssdRouter ussdRouter = ((MapUssdChannel) getGateway().channel("map://ussd")).ussdRouter();

        ussdRouter.insert(i, cfg);
        ussdConfig.insert(i, cfg);

        return "";
    }

}
