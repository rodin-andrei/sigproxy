/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.gw.mgmt.ussd;

import com.unifun.sigtran.ussdgateway.gw.Gateway;
import com.unifun.sigtran.ussdgateway.gw.config.UssdConfig;
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
public class UssdDeleteAction extends Action {

    public UssdDeleteAction(String name, Gateway gateway) {
        super(name, gateway);
    }

    @Override
    public String exec(String cmd, StringTokenizer tokenizer) throws Exception {
        Options options = new Options();
        options.addOption(Option.builder("i").required(true).longOpt("index").hasArg(true).build());

        CommandParser parser = new CommandParser();
        CommandLine cmdLine = parser.parse(options, tokenizer);

        final int i = Integer.parseInt(cmdLine.getOptionValue("i"));

        final UssdConfig ussdConfig = getGateway().getConfig().getUssdConfig();
        final UssdRouter ussdRouter = ((MapUssdChannel) getGateway().channel("map://ussd")).ussdRouter();

        ussdRouter.remove(i);
        ussdConfig.remove(i);

        return "";
    }

}
