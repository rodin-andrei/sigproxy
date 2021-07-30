package com.unifun.sigproxy.aaaaa;

import lombok.extern.slf4j.Slf4j;
import org.restcomm.protocols.ss7.map.MAPStackImpl;
import org.restcomm.protocols.ss7.map.api.service.sms.*;

@Slf4j
public class TestMapServiceSmsListener extends TestMAPServiceListener implements MAPServiceSmsListener {
    public TestMapServiceSmsListener(MAPStackImpl mapStack) {
        super(mapStack);
    }

    @Override
    public void onForwardShortMessageRequest(ForwardShortMessageRequest forwardShortMessageRequest) {
        log.info("onForwardShortMessageRequest  " + this.mapStack.getName());
    }

    @Override
    public void onForwardShortMessageResponse(ForwardShortMessageResponse forwardShortMessageResponse) {
        log.info("onForwardShortMessageResponse  " + this.mapStack.getName());

    }

    @Override
    public void onMoForwardShortMessageRequest(MoForwardShortMessageRequest moForwardShortMessageRequest) {
        log.info("onMoForwardShortMessageRequest  " + this.mapStack.getName());

    }

    @Override
    public void onMoForwardShortMessageResponse(MoForwardShortMessageResponse moForwardShortMessageResponse) {
        log.info("onMoForwardShortMessageResponse  " + this.mapStack.getName());

    }

    @Override
    public void onMtForwardShortMessageRequest(MtForwardShortMessageRequest mtForwardShortMessageRequest) {
        log.info("onMtForwardShortMessageRequest  " + this.mapStack.getName());
    }

    @Override
    public void onMtForwardShortMessageResponse(MtForwardShortMessageResponse mtForwardShortMessageResponse) {
        log.info("onMtForwardShortMessageResponse  " + this.mapStack.getName());
    }

    @Override
    public void onSendRoutingInfoForSMRequest(SendRoutingInfoForSMRequest sendRoutingInfoForSMRequest) {
        log.info("onSendRoutingInfoForSMRequest  " + this.mapStack.getName());
    }

    @Override
    public void onSendRoutingInfoForSMResponse(SendRoutingInfoForSMResponse sendRoutingInfoForSMResponse) {
        log.info("onSendRoutingInfoForSMResponse  " + this.mapStack.getName());
    }

    @Override
    public void onReportSMDeliveryStatusRequest(ReportSMDeliveryStatusRequest reportSMDeliveryStatusRequest) {
        log.info("onReportSMDeliveryStatusRequest  " + this.mapStack.getName());


    }

    @Override
    public void onReportSMDeliveryStatusResponse(ReportSMDeliveryStatusResponse reportSMDeliveryStatusResponse) {
        log.info("onReportSMDeliveryStatusResponse  " + this.mapStack.getName());
    }

    @Override
    public void onInformServiceCentreRequest(InformServiceCentreRequest informServiceCentreRequest) {
        log.info("onInformServiceCentreRequest  " + this.mapStack.getName());
    }

    @Override
    public void onAlertServiceCentreRequest(AlertServiceCentreRequest alertServiceCentreRequest) {
        log.info("onAlertServiceCentreRequest  " + this.mapStack.getName());
    }

    @Override
    public void onAlertServiceCentreResponse(AlertServiceCentreResponse alertServiceCentreResponse) {
        log.info("onAlertServiceCentreResponse  " + this.mapStack.getName());
    }

    @Override
    public void onReadyForSMRequest(ReadyForSMRequest readyForSMRequest) {
        log.info("onReadyForSMRequest  " + this.mapStack.getName());
    }

    @Override
    public void onReadyForSMResponse(ReadyForSMResponse readyForSMResponse) {
        log.info("onReadyForSMResponse  " + this.mapStack.getName());
    }

    @Override
    public void onNoteSubscriberPresentRequest(NoteSubscriberPresentRequest noteSubscriberPresentRequest) {
        log.info("onNoteSubscriberPresentRequest  " + this.mapStack.getName());
    }
}
