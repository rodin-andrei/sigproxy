package com.unifun.sigtran.ussdgateway.gw.mgmt.ss7;

import com.unifun.sigtran.ussdgateway.gw.Gateway;
import com.unifun.sigtran.ussdgateway.gw.mgmt.Action;
import org.restcomm.protocols.ss7.tcap.api.TCAPCounterProvider;
import org.restcomm.protocols.ss7.tcap.api.TCAPProvider;

import java.util.StringTokenizer;

public class DialogStatAction extends Action {

    public DialogStatAction(String name, Gateway gateway) {
        super(name, gateway);
    }

    @Override
    public String exec(String cmd, StringTokenizer tokenizer) throws Exception {
        final TCAPProvider tcapProvider = getGateway().getTcapService().getTcapStack(getGateway().getMobile().getNAME()).getProvider();
        final TCAPCounterProvider tcapCounterProvider = getGateway().getTcapService().getTcapStack(getGateway().getMobile().getNAME()).getCounterProvider();

        String builder = "-----------------------------------------------------------------------------------------------------------------------------------\n" +
                String.format("%30s  %8s%n", "PARAMETER", "VALUE") +
                "-----------------------------------------------------------------------------------------------------------------------------------\n" +
                String.format("%30s  %8d%n", "Current Dialog Count", tcapProvider.getCurrentDialogsCount()) +
                String.format("%30s  %8d%n", "All Rejected Sent Count", tcapCounterProvider.getRejectSentCount()) +
                String.format("%30s  %8d%n", "All Timeouts Count", tcapCounterProvider.getDialogTimeoutCount());
        return builder;
    }
}
