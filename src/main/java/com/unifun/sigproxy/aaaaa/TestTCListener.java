package com.unifun.sigproxy.aaaaa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.restcomm.protocols.ss7.tcap.TCAPStackImpl;
import org.restcomm.protocols.ss7.tcap.api.TCListener;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.*;
import org.restcomm.protocols.ss7.tcap.asn.comp.Invoke;

@Slf4j
@RequiredArgsConstructor
public class TestTCListener implements TCListener {
    private final TCAPStackImpl tcapStack;

    @Override
    public void onTCUni(TCUniIndication tcUniIndication) {
        log.info("onTCUni" + "  " + tcapStack.getName());
    }

    @Override
    public void onTCBegin(TCBeginIndication tcBeginIndication) {
        log.info("onTCBegin " + "  " + tcapStack.getName());
    }

    @Override
    public void onTCContinue(TCContinueIndication tcContinueIndication) {
        log.info("onTCContinue " + tcapStack.getName());
    }

    @Override
    public void onTCEnd(TCEndIndication tcEndIndication) {
        log.info("onTCEnd" + "  " + tcapStack.getName());
    }

    @Override
    public void onTCUserAbort(TCUserAbortIndication tcUserAbortIndication) {
        log.info("onTCUserAbort" + "  " + tcapStack.getName());
    }

    @Override
    public void onTCPAbort(TCPAbortIndication tcpAbortIndication) {
        log.info("onTCPAbort" + "  " + tcapStack.getName());
    }

    @Override
    public void onTCNotice(TCNoticeIndication tcNoticeIndication) {
        log.info("onTCNotice" + "  " + tcapStack.getName());
    }

    @Override
    public void onDialogReleased(Dialog dialog) {
        log.info("oonDialogReleased " + "  " + tcapStack.getName());
    }

    @Override
    public void onInvokeTimeout(Invoke invoke) {
        log.info("onInvokeTimeout" + "  " + tcapStack.getName());
    }

    @Override
    public void onDialogTimeout(Dialog dialog) {
        log.info("onDialogTimeout" + "  " + tcapStack.getName());
    }
}
