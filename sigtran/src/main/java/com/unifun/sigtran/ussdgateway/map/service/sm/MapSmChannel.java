/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.map.service.sm;

import com.unifun.sigtran.ussdgateway.gw.Gateway;
import com.unifun.sigtran.ussdgateway.gw.LocalDialog;
import com.unifun.sigtran.ussdgateway.gw.MapBaseChannel;
import com.unifun.sigtran.ussdgateway.gw.TcapParameters;
import com.unifun.sigtran.ussdgateway.gw.context.ExecutionContext;
import com.unifun.sigtran.ussdgateway.map.dto.*;
import lombok.extern.log4j.Log4j2;
import org.restcomm.protocols.ss7.map.api.MAPDialog;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPMessage;
import org.restcomm.protocols.ss7.map.api.MAPServiceBase;
import org.restcomm.protocols.ss7.map.api.primitives.AddressString;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.service.sms.*;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.asn.comp.Problem;

/**
 * @author okulikov
 */
@Log4j2
public class MapSmChannel extends MapBaseChannel implements MAPServiceSmsListener {
    private final TcapParameters tcapParameters;

    /**
     * Constructs new instance of this class.
     *
     * @param gateway
     */
    public MapSmChannel(Gateway gateway) {
        super(gateway);
        this.getMapProvider().getMAPServiceSms().addMAPServiceListener(this);
        this.tcapParameters = new TcapParameters(getMapProvider().getMAPParameterFactory());
    }

    @Override
    public MAPServiceBase service() {
        return getMapProvider().getMAPServiceSms();
    }

    @Override
    public void start() throws Exception {
        this.getMapProvider().getMAPServiceSms().acivate();
    }

    @Override
    public void stop() {
        this.getMapProvider().getMAPServiceSms().deactivate();
    }

    @Override
    public void addComponent(MAPDialog dialog, JsonComponent component) throws MAPException {
        JsonMap map = mapMessage(component);
        //append MAP operation
        switch (map.operationName()) {
            case "send-routing-info-sm":
//                dialog.setDoNotSendProtocolVersion(true);
                sendRoutingInfoSm((MAPDialogSms) dialog, component, component.getType());
                break;
            default:
                throw new IllegalArgumentException("Not implemented yet " + map.operationName());
        }
    }

    private void sendRoutingInfoSm(MAPDialogSms dialog, JsonComponent component, String type) throws MAPException {
        final JsonMapOperation mapMessage = (JsonMapOperation) mapMessage(component).operation();
        switch (type) {
            case "invoke":
                ISDNAddressString msisdn = getTcapParameters().isdnAddressString(mapMessage.getMsisdn());
                AddressString scAddress = getTcapParameters().addressString(mapMessage.getScAddress());

                dialog.addSendRoutingInfoForSMRequest(
                        msisdn,
                        true,
                        scAddress,
                        null,
                        false,
                        SM_RP_MTI.SMS_Deliver,
                        null,
                        null,
                        false,
                        null,
                        true,
                        true,
                        null);

//                dialog.addSendRoutingInfoForSMRequest(msisdn, true, scAddress, null, false, SM_RP_MTI.SMS_Deliver, null, null, false, null, false, false, null);                
//                dialog.addSendRoutingInfoForSMRequest(msisdn, true, scAddress, null, true, SM_RP_MTI.SMS_Deliver, null, SMDeliveryNotIntended.onlyMCCMNCRequested, true, null, true, true, null);
                break;
        }
    }

    @Override
    public void onForwardShortMessageRequest(ForwardShortMessageRequest forwSmInd) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onForwardShortMessageResponse(ForwardShortMessageResponse forwSmRespInd) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onMoForwardShortMessageRequest(MoForwardShortMessageRequest moForwSmInd) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onMoForwardShortMessageResponse(MoForwardShortMessageResponse moForwSmRespInd) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onMtForwardShortMessageRequest(MtForwardShortMessageRequest mtForwSmInd) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onMtForwardShortMessageResponse(MtForwardShortMessageResponse mtForwSmRespInd) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onSendRoutingInfoForSMRequest(SendRoutingInfoForSMRequest sendRoutingInfoForSMInd) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onSendRoutingInfoForSMResponse(SendRoutingInfoForSMResponse msg) {
        long dialogID = msg.getMAPDialog().getLocalDialogId();

        final JsonMessage jsonMessage = new JsonMessage();

        if (log.isInfoEnabled()) {
            log.info(String.format(INFO, dialogID, "<--- " + msg.getMAPDialog().getTCAPMessageType() + ": send-routing-info-sm"));
        }

        final JsonSccp sccp = sccp(msg.getMAPDialog());
        final JsonTcap tcap = tcap(msg.getMAPDialog(), (LocalDialog) msg.getMAPDialog().getUserObject());


        jsonMessage.setSccp(sccp);
        jsonMessage.setTcap(tcap);

        JsonMapOperation op = new JsonMapOperation();
        op.setImsi(msg.getIMSI().getData());

        JsonAddressString networkNodeNumber = tcapParameters.jsonAddressString(
                msg.getLocationInfoWithLMSI().getNetworkNodeNumber());
        JsonLocationInfoWithLMSI lmsi = new JsonLocationInfoWithLMSI();
        lmsi.setNetworkNodeNumber(networkNodeNumber);
        op.setLocationInfoWithLMSI(lmsi);

        JsonMap map = new JsonMap("send-routing-info-sm", op);


        final JsonComponent component = new JsonComponent();
        final JsonReturnResultLast returnResultLast = new JsonReturnResultLast(msg.getInvokeId(), map);

        component.setType("returnResultLast");
        component.setValue(returnResultLast);

        final JsonComponents components = new JsonComponents();
        components.addComponent(component);
        tcap.setComponents(components);


        if (log.isTraceEnabled()) {
            SccpAddress src = msg.getMAPDialog().getRemoteAddress();
            int pc = src.getSignalingPointCode();
            log.trace(String.format("RX : %d : %s", pc, msg));
        }

        ExecutionContext context = ((LocalDialog) msg.getMAPDialog().getUserObject()).getContext();
        if (context == null) {
            throw new IllegalStateException("Out of context");
        }

        context.completed(jsonMessage);
    }

    @Override
    public void onReportSMDeliveryStatusRequest(ReportSMDeliveryStatusRequest reportSMDeliveryStatusInd) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onReportSMDeliveryStatusResponse(ReportSMDeliveryStatusResponse reportSMDeliveryStatusRespInd) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onInformServiceCentreRequest(InformServiceCentreRequest informServiceCentreInd) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onAlertServiceCentreRequest(AlertServiceCentreRequest alertServiceCentreInd) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onAlertServiceCentreResponse(AlertServiceCentreResponse alertServiceCentreInd) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onReadyForSMRequest(ReadyForSMRequest request) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onReadyForSMResponse(ReadyForSMResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onNoteSubscriberPresentRequest(NoteSubscriberPresentRequest request) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onRejectComponent(MAPDialog mapDialog, Long invokeId, Problem problem, boolean isLocalOriginated) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onInvokeTimeout(MAPDialog mapDialog, Long invokeId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onMAPMessage(MAPMessage mapMessage) {
    }

}
