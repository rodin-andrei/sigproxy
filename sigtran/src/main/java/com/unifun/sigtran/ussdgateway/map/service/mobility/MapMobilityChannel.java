/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.map.service.mobility;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unifun.sigtran.ussdgateway.gw.Gateway;
import com.unifun.sigtran.ussdgateway.gw.LocalDialog;
import com.unifun.sigtran.ussdgateway.gw.MapBaseChannel;
import com.unifun.sigtran.ussdgateway.gw.context.ExecutionContext;
import com.unifun.sigtran.ussdgateway.map.dto.*;
import lombok.extern.log4j.Log4j2;
import org.restcomm.protocols.ss7.map.api.MAPDialog;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPMessage;
import org.restcomm.protocols.ss7.map.api.MAPServiceBase;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.restcomm.protocols.ss7.map.api.service.mobility.MAPDialogMobility;
import org.restcomm.protocols.ss7.map.api.service.mobility.MAPServiceMobilityListener;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationFailureReportRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationFailureReportResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.ForwardCheckSSIndicationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.ResetRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.RestoreDataRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.RestoreDataResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.CheckImeiRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.CheckImeiResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.*;
import org.restcomm.protocols.ss7.map.api.service.mobility.oam.ActivateTraceModeRequest_Mobility;
import org.restcomm.protocols.ss7.map.api.service.mobility.oam.ActivateTraceModeResponse_Mobility;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.*;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DeleteSubscriberDataRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DeleteSubscriberDataResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.InsertSubscriberDataRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.InsertSubscriberDataResponse;
import org.restcomm.protocols.ss7.map.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.asn.comp.Problem;

/**
 * This class implements MAP related RX/TX processes.
 *
 * @author okulikov
 */
@Log4j2
public class MapMobilityChannel extends MapBaseChannel implements MAPServiceMobilityListener {

    private final static String INFO = "(DID:%s) %s";
    private final MapMobilityParameters mobilityParameters;

    /**
     * Constructs new instance of this class.
     *
     * @param gateway
     */
    public MapMobilityChannel(Gateway gateway) {
        super(gateway);
        this.mobilityParameters = new MapMobilityParameters(getMapProvider().getMAPParameterFactory());
        this.getMapProvider().getMAPServiceMobility().addMAPServiceListener(this);
    }

    @Override
    public void start() throws Exception {
        this.getMapProvider().getMAPServiceMobility().acivate();
    }

    @Override
    public void stop() {
        this.getMapProvider().getMAPServiceMobility().deactivate();
    }

    @Override
    public void addComponent(MAPDialog dialog, JsonComponent component) throws MAPException {
        JsonMap map = mapMessage(component);
        //append MAP operation
        switch (map.operationName()) {
            case "any-time-interrogation":
                dialog.setDoNotSendProtocolVersion(true);
                anyTimeInterogation((MAPDialogMobility) dialog, component, component.getType());
                break;
            case "provide-subscriber-info":
                dialog.setDoNotSendProtocolVersion(true);
                provideSubscriberInfoRequest((MAPDialogMobility) dialog, component, component.getType());
                break;
            default:
                throw new IllegalArgumentException("Not implemented yet " + map.operationName());
        }
    }

    /**
     * Appends ProcessUnstructuredSSRequest message to the given dialog.
     *
     * @param dialog
     * @param mapMessage
     * @throws MAPException
     */
    private void anyTimeInterogation(MAPDialogMobility dialog, JsonComponent component, String type) throws MAPException {
        final JsonMapOperation mapMessage = (JsonMapOperation) mapMessage(component).operation();
        switch (type) {
            case "invoke":
                final SubscriberIdentity subscriberIdentity = mobilityParameters.subscriberIdentity(mapMessage.getSubscriberIdentity());
                final RequestedInfo requestedInfo = mobilityParameters.requestedInfo(mapMessage.getRequestedInfo());
                final ISDNAddressString gsmSCFAddress = getTcapParameters().isdnAddressString(mapMessage.getGsmSCFAddress());

                dialog.addAnyTimeInterrogationRequest(subscriberIdentity, requestedInfo, gsmSCFAddress, null);

                break;
            case "returnResultLast":
                final MAPExtensionContainer xContainer = null;
                final SubscriberInfo subscriberInfo = mobilityParameters.subscriberInfo(mapMessage.getSubscriberInfo());
                JsonReturnResultLast returnResultLast = (JsonReturnResultLast) component.getValue();
                dialog.addAnyTimeInterrogationResponse(returnResultLast.getInvokeId(), subscriberInfo, xContainer);
                break;
        }
    }

    private void provideSubscriberInfoRequest(MAPDialogMobility dialog, JsonComponent component, String type) throws MAPException {
        final JsonMapOperation mapMessage = (JsonMapOperation) mapMessage(component).operation();
        switch (type) {
            case "invoke":
                dialog.addProvideSubscriberInfoRequest(new IMSIImpl(mapMessage.getImsi()),
                        null,
                        mobilityParameters.allPossibleInfo(),
                        null,
                        null
                );
                break;
            case "returnResultLast":
                throw new UnsupportedOperationException("PSI ReturnResultLast is not supported yet.");
        }
    }

    @Override
    public void onUpdateLocationRequest(UpdateLocationRequest ind) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onUpdateLocationResponse(UpdateLocationResponse ind) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onCancelLocationRequest(CancelLocationRequest request) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onCancelLocationResponse(CancelLocationResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onSendIdentificationRequest(SendIdentificationRequest request) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onSendIdentificationResponse(SendIdentificationResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onUpdateGprsLocationRequest(UpdateGprsLocationRequest request) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onUpdateGprsLocationResponse(UpdateGprsLocationResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onPurgeMSRequest(PurgeMSRequest request) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onPurgeMSResponse(PurgeMSResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onSendAuthenticationInfoRequest(SendAuthenticationInfoRequest ind) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onSendAuthenticationInfoResponse(SendAuthenticationInfoResponse ind) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onAuthenticationFailureReportRequest(AuthenticationFailureReportRequest ind) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onAuthenticationFailureReportResponse(AuthenticationFailureReportResponse ind) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onResetRequest(ResetRequest ind) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onForwardCheckSSIndicationRequest(ForwardCheckSSIndicationRequest ind) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onRestoreDataRequest(RestoreDataRequest ind) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onRestoreDataResponse(RestoreDataResponse ind) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onAnyTimeInterrogationRequest(AnyTimeInterrogationRequest msg) {
    }

    @Override
    public void onAnyTimeInterrogationResponse(AnyTimeInterrogationResponse msg) {
        long dialogID = msg.getMAPDialog().getLocalDialogId();

        final JsonMessage jsonMessage = new JsonMessage();

        if (log.isInfoEnabled()) {
            log.info(String.format(INFO, dialogID, "<--- " + msg.getMAPDialog().getTCAPMessageType() + ": any-time-interrogation"));
        }

        final JsonSccp sccp = sccp(msg.getMAPDialog());
        final JsonTcap tcap = tcap(msg.getMAPDialog(), (LocalDialog) msg.getMAPDialog().getUserObject());


        jsonMessage.setSccp(sccp);
        jsonMessage.setTcap(tcap);

        JsonMapOperation op = new JsonMapOperation();
        op.setSubscriberInfo(mobilityParameters.jsonSubscriberInfo(msg.getSubscriberInfo()));

        JsonMap map = new JsonMap("any-time-interrogation", op);

        final JsonComponent component = new JsonComponent();
        final JsonReturnResultLast returnResultLast = new JsonReturnResultLast(msg.getInvokeId(), map);

        component.setType("returnResultLast");
        component.setValue(returnResultLast);

        tcap.getComponents().addComponent(component);


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
    public void onAnyTimeSubscriptionInterrogationRequest(AnyTimeSubscriptionInterrogationRequest request) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onAnyTimeSubscriptionInterrogationResponse(AnyTimeSubscriptionInterrogationResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onProvideSubscriberInfoRequest(ProvideSubscriberInfoRequest request) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onProvideSubscriberInfoResponse(ProvideSubscriberInfoResponse response) {
        long dialogID = response.getMAPDialog().getLocalDialogId();

        final JsonMessage jsonMessage = new JsonMessage();

        if (log.isInfoEnabled()) {
            log.info(String.format(INFO, dialogID, "<--- " + response.getMAPDialog().getTCAPMessageType() + ": provided-subscriber-info"));
        }

        final JsonSccp sccp = sccp(response.getMAPDialog());
        final JsonTcap tcap = tcap(response.getMAPDialog(), (LocalDialog) response.getMAPDialog().getUserObject());


        jsonMessage.setSccp(sccp);
        jsonMessage.setTcap(tcap);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        JsonMap map = null;
        try {
            map = new JsonMap("provided-subscriber-info", objectMapper.writeValueAsString(response.getSubscriberInfo()));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }

        final JsonComponent component = new JsonComponent();
        final JsonReturnResultLast returnResultLast = new JsonReturnResultLast(response.getInvokeId(), map);

        component.setType("returnResultLast");
        component.setValue(returnResultLast);

        if (tcap.getComponents() == null) {
            tcap.setComponents(new JsonComponents());
        }
        tcap.getComponents().addComponent(component);


        if (log.isTraceEnabled()) {
            SccpAddress src = response.getMAPDialog().getRemoteAddress();
            int pc = src.getSignalingPointCode();
            log.trace(String.format("RX : %d : %s", pc, response));
        }

        ExecutionContext context = ((LocalDialog) response.getMAPDialog().getUserObject()).getContext();
        if (context == null) {
            throw new IllegalStateException("Out of context");
        }

        context.completed(jsonMessage);

    }

    @Override
    public void onInsertSubscriberDataRequest(InsertSubscriberDataRequest request) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onInsertSubscriberDataResponse(InsertSubscriberDataResponse request) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onDeleteSubscriberDataRequest(DeleteSubscriberDataRequest request) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onDeleteSubscriberDataResponse(DeleteSubscriberDataResponse request) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onCheckImeiRequest(CheckImeiRequest request) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onCheckImeiResponse(CheckImeiResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onActivateTraceModeRequest_Mobility(ActivateTraceModeRequest_Mobility ind) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onActivateTraceModeResponse_Mobility(ActivateTraceModeResponse_Mobility ind) {
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

    @Override
    public MAPServiceBase service() {
        return getMapProvider().getMAPServiceMobility();
    }


}
