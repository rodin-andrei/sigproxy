package com.unifun.sigproxy.aaaaa;

import lombok.extern.slf4j.Slf4j;
import org.restcomm.protocols.ss7.map.MAPStackImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.*;

@Slf4j
public class TestMAPServiceSupplementaryListener extends TestMAPServiceListener implements MAPServiceSupplementaryListener {
    public TestMAPServiceSupplementaryListener(MAPStackImpl mapStack) {
        super(mapStack);
    }

    @Override
    public void onRegisterSSRequest(RegisterSSRequest registerSSRequest) {
        log.info("onRegisterSSRequest  " + mapStack.getName());
    }

    @Override
    public void onRegisterSSResponse(RegisterSSResponse registerSSResponse) {
        log.info("onRegisterSSResponse  " + mapStack.getName());

    }

    @Override
    public void onEraseSSRequest(EraseSSRequest eraseSSRequest) {
        log.info("onEraseSSRequest  " + mapStack.getName());

    }

    @Override
    public void onEraseSSResponse(EraseSSResponse eraseSSResponse) {
        log.info("onEraseSSResponse  " + mapStack.getName());

    }

    @Override
    public void onActivateSSRequest(ActivateSSRequest activateSSRequest) {
        log.info("onActivateSSRequest  " + mapStack.getName());

    }

    @Override
    public void onActivateSSResponse(ActivateSSResponse activateSSResponse) {
        log.info("onActivateSSResponse  " + mapStack.getName());

    }

    @Override
    public void onDeactivateSSRequest(DeactivateSSRequest deactivateSSRequest) {
        log.info("onDeactivateSSRequest  " + mapStack.getName());

    }

    @Override
    public void onDeactivateSSResponse(DeactivateSSResponse deactivateSSResponse) {
        log.info("onDeactivateSSResponse  " + mapStack.getName());

    }

    @Override
    public void onInterrogateSSRequest(InterrogateSSRequest interrogateSSRequest) {
        log.info("onInterrogateSSRequest  " + mapStack.getName());

    }

    @Override
    public void onInterrogateSSResponse(InterrogateSSResponse interrogateSSResponse) {
        log.info("onInterrogateSSResponse  " + mapStack.getName());

    }

    @Override
    public void onGetPasswordRequest(GetPasswordRequest getPasswordRequest) {
        log.info("onGetPasswordRequest  " + mapStack.getName());

    }

    @Override
    public void onGetPasswordResponse(GetPasswordResponse getPasswordResponse) {
        log.info("onGetPasswordResponse  " + mapStack.getName());

    }

    @Override
    public void onRegisterPasswordRequest(RegisterPasswordRequest registerPasswordRequest) {
        log.info("onRegisterPasswordRequest  " + mapStack.getName());

    }

    @Override
    public void onRegisterPasswordResponse(RegisterPasswordResponse registerPasswordResponse) {
        log.info("onRegisterPasswordResponse  " + mapStack.getName());

    }

    @Override
    public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest processUnstructuredSSRequest) {
        log.info("onProcessUnstructuredSSRequest  " + mapStack.getName());

    }

    @Override
    public void onProcessUnstructuredSSResponse(ProcessUnstructuredSSResponse processUnstructuredSSResponse) {
        log.info("onProcessUnstructuredSSResponse  " + mapStack.getName());

    }

    @Override
    public void onUnstructuredSSRequest(UnstructuredSSRequest unstructuredSSRequest) {
        log.info("onUnstructuredSSRequest  " + mapStack.getName());

    }

    @Override
    public void onUnstructuredSSResponse(UnstructuredSSResponse unstructuredSSResponse) {
        log.info("onUnstructuredSSResponse  " + mapStack.getName());

    }

    @Override
    public void onUnstructuredSSNotifyRequest(UnstructuredSSNotifyRequest unstructuredSSNotifyRequest) {
        log.info("onUnstructuredSSNotifyRequest  " + mapStack.getName());

    }

    @Override
    public void onUnstructuredSSNotifyResponse(UnstructuredSSNotifyResponse unstructuredSSNotifyResponse) {
        log.info("onUnstructuredSSNotifyResponse  " + mapStack.getName());

    }
}
