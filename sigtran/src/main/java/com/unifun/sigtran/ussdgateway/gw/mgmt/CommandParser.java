/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.gw.mgmt;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.StringTokenizer;

/**
 * @author okulikov
 */
public class CommandParser extends DefaultParser {

    public CommandLine parse(Options options, StringTokenizer tokenizer) throws ParseException {
        String[] args = new String[tokenizer.countTokens()];
        for (int i = 0; i < args.length; i++) {
            args[i] = tokenizer.nextToken();
        }
        return super.parse(options, args);
    }
}
