/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.gw.mgmt;

import com.unifun.sigtran.ussdgateway.gw.Gateway;
import com.unifun.sigtran.ussdgateway.gw.mgmt.ss7.DialogShowAction;
import com.unifun.sigtran.ussdgateway.gw.mgmt.ss7.DialogStatAction;
import com.unifun.sigtran.ussdgateway.gw.mgmt.ss7.LinkShowAction;
import com.unifun.sigtran.ussdgateway.gw.mgmt.ss7.SubsystemShowAction;

import java.util.StringTokenizer;

/**
 * @author okulikov
 */
public class MobileShell {

    private final Gateway gateway;

    private final Aggregator ss7;
    private final Aggregator subsystem;
    private final Aggregator link;
    private final Aggregator relation;
    private final Aggregator gtt;
    private final Aggregator dlg;

    public MobileShell(Gateway gateway) {
        this.gateway = gateway;

        link = new Aggregator("link", gateway);
        link.add(new LinkShowAction("show", gateway));

        subsystem = new Aggregator("subsystem", gateway);
        subsystem.add(new SubsystemShowAction("show", gateway));

        gtt = new Aggregator("gtt", gateway);

        relation = new Aggregator("relation", gateway);

        dlg = new Aggregator("dialog", gateway);
        dlg.add(new DialogShowAction("show", gateway));
        dlg.add(new DialogStatAction("stat", gateway));

        ss7 = new Aggregator("ss7", gateway);
        ss7.add(subsystem);
        ss7.add(gtt);
        ss7.add(link);
        ss7.add(relation);
        ss7.add(dlg);
    }

    public String exec(String cmd, StringTokenizer tokenizer) throws Exception {
        return ss7.exec(cmd, tokenizer);
    }

}
