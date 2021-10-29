package com.unifun.sigproxy.sigtran.service.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.restcomm.protocols.ss7.map.MAPStackImpl;
import org.restcomm.protocols.ss7.map.api.MAPDialog;
import org.restcomm.protocols.ss7.map.api.MAPDialogListener;
import org.restcomm.protocols.ss7.map.api.dialog.*;
import org.restcomm.protocols.ss7.map.api.primitives.AddressString;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.tcap.asn.ApplicationContextName;

@Slf4j
@RequiredArgsConstructor
public class MAPDialogListenerImpl implements MAPDialogListener {
    private final MAPStackImpl mapStack;

    @Override
    public void onDialogDelimiter(MAPDialog mapDialog) {
        log.info("onDialogDelimiter   " + this.mapStack.getName());
    }

    @Override
    public void onDialogRequest(MAPDialog mapDialog, AddressString addressString, AddressString addressString1, MAPExtensionContainer mapExtensionContainer) {
        log.info("onDialogRequest   " + this.mapStack.getName());

    }

    @Override
    public void onDialogRequestEricsson(MAPDialog mapDialog, AddressString addressString, AddressString addressString1, AddressString addressString2, AddressString addressString3) {
        log.info("onDialogRequestEricsson   " + this.mapStack.getName());

    }

    @Override
    public void onDialogAccept(MAPDialog mapDialog, MAPExtensionContainer mapExtensionContainer) {
        log.info("onDialogAccept   " + this.mapStack.getName());

    }

    @Override
    public void onDialogReject(MAPDialog mapDialog, MAPRefuseReason mapRefuseReason, ApplicationContextName applicationContextName, MAPExtensionContainer mapExtensionContainer) {
        log.info("onDialogReject   " + this.mapStack.getName());

    }

    @Override
    public void onDialogUserAbort(MAPDialog mapDialog, MAPUserAbortChoice mapUserAbortChoice, MAPExtensionContainer mapExtensionContainer) {
        log.info("onDialogUserAbort   " + this.mapStack.getName());

    }

    @Override
    public void onDialogProviderAbort(MAPDialog mapDialog, MAPAbortProviderReason mapAbortProviderReason, MAPAbortSource mapAbortSource, MAPExtensionContainer mapExtensionContainer) {
        log.info("onDialogProviderAbort   " + this.mapStack.getName());

    }

    @Override
    public void onDialogClose(MAPDialog mapDialog) {
        log.info("onDialogClose   " + this.mapStack.getName());

    }

    @Override
    public void onDialogNotice(MAPDialog mapDialog, MAPNoticeProblemDiagnostic mapNoticeProblemDiagnostic) {
        log.info("onDialogNotice  " + this.mapStack.getName());

    }

    @Override
    public void onDialogRelease(MAPDialog mapDialog) {
        log.info("onDialogRelease   " + this.mapStack.getName());

    }

    @Override
    public void onDialogTimeout(MAPDialog mapDialog) {
        log.info("onDialogTimeout   " + this.mapStack.getName());

    }


}
