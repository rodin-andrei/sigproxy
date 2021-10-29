package com.unifun.sigproxy.sigtran.service.listeners;

import com.unifun.sigproxy.sigtran.service.rabbit.ProducerService;
import com.unifun.sigproxy.sigtran.service.rabbit.pojo.MapSupplementaryMessageRabbit;
import com.unifun.sigproxy.sigtran.service.rabbit.pojo.SccpRabbitAddress;
import com.unifun.sigproxy.sigtran.service.sccp.SccpParametersService;
import lombok.extern.slf4j.Slf4j;
import org.restcomm.protocols.ss7.map.MAPStackImpl;
import org.restcomm.protocols.ss7.map.api.MAPDialog;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.restcomm.protocols.ss7.map.api.service.supplementary.*;

import java.util.Optional;


@Slf4j
public class MAPServiceSupplementaryListenerImpl extends MAPServiceListenerImpl implements MAPServiceSupplementaryListener {
    private final ProducerService producerService;
    private final SccpParametersService sccpParametersService;
    public MAPServiceSupplementaryListenerImpl(MAPStackImpl mapStack, ProducerService producerService, SccpParametersService sccpParametersService) {
        super(mapStack);
        this.producerService = producerService;
        this.sccpParametersService = sccpParametersService;

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

    //TODO create autonomous process of responding if possible
    @Override
    public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest processUnstructuredSSRequest) { //TODO creates service for converting pussr to rabbit message
        log.info("onProcessUnstructuredSSRequest  " + mapStack.getName());

        MapSupplementaryMessageRabbit message = new MapSupplementaryMessageRabbit();
        processUnstructuredSSRequest.getMAPDialog().getIdleTaskTimeout();
        message.setStackName(this.mapStack.getName());
        SccpRabbitAddress calledParty = this.sccpParametersService
                .createSccpRabbitAddress(processUnstructuredSSRequest.getMAPDialog().getLocalAddress());
        SccpRabbitAddress callingParty = this.sccpParametersService
                .createSccpRabbitAddress(processUnstructuredSSRequest.getMAPDialog().getRemoteAddress());
        message.setCalledParty(calledParty);
        message.setCallingParty(callingParty);
        message.setOperation(processUnstructuredSSRequest.getMessageType().name());
        message.setType(processUnstructuredSSRequest.getMAPDialog().getTCAPMessageType().name());
        message.setTcapId(processUnstructuredSSRequest.getMAPDialog().getLocalDialogId()); //TODO check continue
        message.setUssdString(processUnstructuredSSRequest.getUSSDString().toString()); //TODO work with encoding schemas
        message.setMsisdn((processUnstructuredSSRequest.getMSISDNAddressString() != null)
                ? processUnstructuredSSRequest.getMSISDNAddressString().getAddress()
                : processUnstructuredSSRequest.getMAPDialog().getReceivedDestReference().getAddress());

        this.producerService.send(message);
    }

    @Override
    public void onProcessUnstructuredSSResponse(ProcessUnstructuredSSResponse processUnstructuredSSResponse) {
        log.info("onProcessUnstructuredSSResponse  " + mapStack.getName());

        MapSupplementaryMessageRabbit message = new MapSupplementaryMessageRabbit();
        message.setStackName(this.mapStack.getName());
        SccpRabbitAddress calledParty = this.sccpParametersService
                .createSccpRabbitAddress(processUnstructuredSSResponse.getMAPDialog().getLocalAddress());
        SccpRabbitAddress callingParty = this.sccpParametersService
                .createSccpRabbitAddress(processUnstructuredSSResponse.getMAPDialog().getRemoteAddress());
        message.setCalledParty(calledParty);
        message.setCallingParty(callingParty);
        message.setOperation(processUnstructuredSSResponse.getMessageType().name());
        message.setType(processUnstructuredSSResponse.getMAPDialog().getTCAPMessageType().name());
        message.setTcapId(processUnstructuredSSResponse.getMAPDialog().getLocalDialogId()); //TODO check continue
        message.setUssdString(processUnstructuredSSResponse.getUSSDString().toString()); //TODO work with encoding schemas
        message.setMsisdn(Optional
                .ofNullable(processUnstructuredSSResponse.getMAPDialog().getReceivedDestReference().getAddress())
                .orElse(null));

        this.producerService.send(message);
    }

    @Override
    public void onUnstructuredSSRequest(UnstructuredSSRequest unstructuredSSRequest) {
        log.info("onUnstructuredSSRequest  " + mapStack.getName());

        MapSupplementaryMessageRabbit message = new MapSupplementaryMessageRabbit();
        message.setStackName(this.mapStack.getName());
        SccpRabbitAddress calledParty = this.sccpParametersService
                .createSccpRabbitAddress(unstructuredSSRequest.getMAPDialog().getLocalAddress());
        SccpRabbitAddress callingParty = this.sccpParametersService
                .createSccpRabbitAddress(unstructuredSSRequest.getMAPDialog().getRemoteAddress());
        message.setCalledParty(calledParty);
        message.setCallingParty(callingParty);
        message.setOperation(unstructuredSSRequest.getMessageType().name());
        message.setType(unstructuredSSRequest.getMAPDialog().getTCAPMessageType().name());
        message.setTcapId(unstructuredSSRequest.getMAPDialog().getLocalDialogId()); //TODO check continue
        message.setContinueTcapId(unstructuredSSRequest.getMAPDialog().getRemoteDialogId());
        message.setUssdString(unstructuredSSRequest.getUSSDString().toString()); //TODO work with encoding schemas
        message.setMsisdn(Optional
                .ofNullable(unstructuredSSRequest.getMAPDialog().getReceivedDestReference().getAddress())
                .orElse(null));

        this.producerService.send(message);
    }

    @Override
    public void onUnstructuredSSResponse(UnstructuredSSResponse unstructuredSSResponse) {
        log.info("onUnstructuredSSResponse  " + mapStack.getName());

        MapSupplementaryMessageRabbit message = new MapSupplementaryMessageRabbit();
        message.setStackName(this.mapStack.getName());
        SccpRabbitAddress calledParty = this.sccpParametersService
                .createSccpRabbitAddress(unstructuredSSResponse.getMAPDialog().getLocalAddress());
        SccpRabbitAddress callingParty = this.sccpParametersService
                .createSccpRabbitAddress(unstructuredSSResponse.getMAPDialog().getRemoteAddress());
        message.setCalledParty(calledParty);
        message.setCallingParty(callingParty);
        message.setOperation(unstructuredSSResponse.getMessageType().name());
        message.setType(unstructuredSSResponse.getMAPDialog().getTCAPMessageType().name());
        message.setTcapId(unstructuredSSResponse.getMAPDialog().getLocalDialogId()); //TODO check continue
        message.setContinueTcapId(unstructuredSSResponse.getMAPDialog().getRemoteDialogId());
        message.setUssdString(unstructuredSSResponse.getUSSDString().toString()); //TODO work with encoding schemas
        message.setMsisdn(Optional
                .ofNullable(unstructuredSSResponse.getMAPDialog().getReceivedDestReference().getAddress())
                .orElse(null));

        this.producerService.send(message);
    }

    @Override
    public void onUnstructuredSSNotifyRequest(UnstructuredSSNotifyRequest unstructuredSSNotifyRequest) {
        log.info("onUnstructuredSSNotifyRequest  " + mapStack.getName());
    }

    @Override
    public void onErrorComponent(MAPDialog mapDialog, Long aLong, MAPErrorMessage mapErrorMessage) {
        super.onErrorComponent(mapDialog, aLong, mapErrorMessage);
    }

    @Override
    public void onUnstructuredSSNotifyResponse(UnstructuredSSNotifyResponse unstructuredSSNotifyResponse) {
        log.info("onUnstructuredSSNotifyResponse  " + mapStack.getName());
    }
}



