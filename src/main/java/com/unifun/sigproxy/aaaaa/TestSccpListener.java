package com.unifun.sigproxy.aaaaa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.restcomm.protocols.ss7.sccp.*;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImpl;
import org.restcomm.protocols.ss7.sccp.message.SccpDataMessage;
import org.restcomm.protocols.ss7.sccp.message.SccpNoticeMessage;
import org.restcomm.protocols.ss7.sccp.parameter.*;

@Slf4j
@RequiredArgsConstructor
public class TestSccpListener implements SccpListener {
    private final SccpStackImpl sccpStack;

    @Override
    public void onMessage(SccpDataMessage sccpDataMessage) {
        log.info("onMessage " + new String(sccpDataMessage.getData()) + "  " + sccpStack.getName());
    }

    @Override
    public void onNotice(SccpNoticeMessage sccpNoticeMessage) {
        log.info("onNotice " + sccpStack.getName());
    }

    @Override
    public void onCoordResponse(int i, int i1) {
        log.info("onCoordResponse " + sccpStack.getName());
    }

    @Override
    public void onState(int i, int i1, boolean b, int i2) {
        log.info("onStat e" + sccpStack.getName());
    }

    @Override
    public void onPcState(int i, SignallingPointStatus signallingPointStatus, Integer integer, RemoteSccpStatus remoteSccpStatus) {
        log.info("onPcState " + sccpStack.getName());
    }

    @Override
    public void onNetworkIdState(int i, NetworkIdState networkIdState) {
        log.info("onNetworkIdState " + sccpStack.getName());
    }

    @Override
    public void onConnectIndication(SccpConnection sccpConnection, SccpAddress sccpAddress, SccpAddress sccpAddress1, ProtocolClass protocolClass, Credit credit, byte[] bytes, Importance importance) throws Exception {
        log.info("onConnectIndication " + sccpStack.getName());
    }

    @Override
    public void onConnectConfirm(SccpConnection sccpConnection, byte[] bytes) {
        log.info("onConnectConfirm " + sccpStack.getName());
    }

    @Override
    public void onDisconnectIndication(SccpConnection sccpConnection, ReleaseCause releaseCause, byte[] bytes) {
        log.info("onDisconnectIndication " + sccpStack.getName());
    }

    @Override
    public void onDisconnectIndication(SccpConnection sccpConnection, RefusalCause refusalCause, byte[] bytes) {
        log.info("onDisconnectIndication " + sccpStack.getName());
    }

    @Override
    public void onDisconnectIndication(SccpConnection sccpConnection, ErrorCause errorCause) {
        log.info("onDisconnectIndication " + sccpStack.getName());
    }

    @Override
    public void onResetIndication(SccpConnection sccpConnection, ResetCause resetCause) {
        log.info("onResetIndication " + sccpStack.getName());
    }

    @Override
    public void onResetConfirm(SccpConnection sccpConnection) {
        log.info("onResetConfirm " + sccpStack.getName());
    }

    @Override
    public void onData(SccpConnection sccpConnection, byte[] bytes) {
        log.info("onData " + sccpStack.getName());
    }

    @Override
    public void onDisconnectConfirm(SccpConnection sccpConnection) {
        log.info("onDisconnectConfirm " + sccpStack.getName());
    }
}
