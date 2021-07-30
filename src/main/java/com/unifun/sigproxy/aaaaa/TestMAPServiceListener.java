package com.unifun.sigproxy.aaaaa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.restcomm.protocols.ss7.map.MAPStackImpl;
import org.restcomm.protocols.ss7.map.api.MAPDialog;
import org.restcomm.protocols.ss7.map.api.MAPMessage;
import org.restcomm.protocols.ss7.map.api.MAPServiceListener;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.restcomm.protocols.ss7.tcap.asn.comp.Problem;

@Slf4j
@RequiredArgsConstructor
public class TestMAPServiceListener implements MAPServiceListener {
    protected final MAPStackImpl mapStack;

    @Override
    public void onErrorComponent(MAPDialog mapDialog, Long aLong, MAPErrorMessage mapErrorMessage) {
        log.info("onErrorComponent  " + this.mapStack.getName());
    }

    @Override
    public void onRejectComponent(MAPDialog mapDialog, Long aLong, Problem problem, boolean b) {
        log.info("onRejectComponent  " + this.mapStack.getName());

    }

    @Override
    public void onInvokeTimeout(MAPDialog mapDialog, Long aLong) {
        log.info("onInvokeTimeout  " + this.mapStack.getName());

    }

    @Override
    public void onMAPMessage(MAPMessage mapMessage) {
        log.info("onMAPMessage  " + this.mapStack.getName());
    }
}
