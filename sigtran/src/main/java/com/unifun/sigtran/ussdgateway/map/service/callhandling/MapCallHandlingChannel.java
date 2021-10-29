/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.map.service.callhandling;

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
import org.restcomm.protocols.ss7.map.api.primitives.ExternalSignalInfo;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.ProtocolId;
import org.restcomm.protocols.ss7.map.api.primitives.SignalInfo;
import org.restcomm.protocols.ss7.map.api.service.callhandling.*;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;
import org.restcomm.protocols.ss7.map.primitives.ExternalSignalInfoImpl;
import org.restcomm.protocols.ss7.map.primitives.SignalInfoImpl;
import org.restcomm.protocols.ss7.map.service.callhandling.CallReferenceNumberImpl;
import org.restcomm.protocols.ss7.map.service.callhandling.CamelInfoImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.asn.comp.Problem;

/**
 * This class implements MAP related RX/TX processes.
 *
 * @author okulikov
 */
@Log4j2
public class MapCallHandlingChannel extends MapBaseChannel implements MAPServiceCallHandlingListener {
    private final CallHandlingParameters callHandlingParameters;

    /**
     * Constructs new instance of this class.
     *
     * @param gateway
     */
    public MapCallHandlingChannel(Gateway gateway) {
        super(gateway);
        this.callHandlingParameters = new CallHandlingParameters(getMapProvider().getMAPParameterFactory());
        this.getMapProvider().getMAPServiceCallHandling().addMAPServiceListener(this);
    }

    @Override
    public void start() throws Exception {
        this.getMapProvider().getMAPServiceCallHandling().acivate();
    }

    @Override
    public void stop() {
        this.getMapProvider().getMAPServiceCallHandling().deactivate();
    }

    @Override
    public void addComponent(MAPDialog dialog, JsonComponent component) throws MAPException {
        JsonMap map = mapMessage(component);
        //append MAP operation
        switch (map.operationName()) {
            case "send-routing-info":
                dialog.setDoNotSendProtocolVersion(true);
                sendRoutingInfo((MAPDialogCallHandling) dialog, component, component.getType());
                break;
            default:
                throw new IllegalArgumentException("Not implemented yet " + map.operationName());
        }
    }

    /**
     * Appends SRI message to the given dialog.
     *
     * @param dialog
     * @param mapMessage
     * @throws MAPException
     */
    private void sendRoutingInfo(MAPDialogCallHandling dialog, JsonComponent component, String type) throws MAPException {
        final JsonMapOperation mapMessage = (JsonMapOperation) mapMessage(component).operation();
        switch (type) {
            case "invoke":
                SignalInfo signalInfo = new SignalInfoImpl(new byte[]{0x04, 0x03, (byte) 0x80, (byte) 0x90, (byte) 0xa3});
                ProtocolId protocolId = ProtocolId.ets_300102_1;
                ExternalSignalInfo networkSignalInfo = new ExternalSignalInfoImpl(signalInfo, protocolId, null);

                CallReferenceNumber crn = new CallReferenceNumberImpl(new byte[]{0, (byte) 237, 40, (byte) 236, 51});
                // camelInfo
                SupportedCamelPhases scf = new SupportedCamelPhasesImpl(true, true, true, false);
                CamelInfo camelInfo = new CamelInfoImpl(scf, false, null, null);

                final ISDNAddressString msisdn = getTcapParameters().isdnAddressString(mapMessage.getMsisdn());
                final ISDNAddressString gmscAddress = getTcapParameters().isdnAddressString(mapMessage.getGmscAddress());

                dialog.addSendRoutingInformationRequest(msisdn, null, null, InterrogationType.basicCall,
                        false, null, gmscAddress, crn, null, null, networkSignalInfo, camelInfo, false,
                        null, null, false, null, null, null, false, null, false, false, false, false, null,
                        null, null, false, null);

                dialog.setReturnMessageOnError(true);
                break;
            case "returnResultLast":
                JsonReturnResultLast returnResultLast = (JsonReturnResultLast) component.getValue();
                break;
        }
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
        return getMapProvider().getMAPServiceCallHandling();
    }

    @Override
    public void onSendRoutingInformationRequest(SendRoutingInformationRequest request) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onSendRoutingInformationResponse(SendRoutingInformationResponse msg) {
        long dialogID = msg.getMAPDialog().getLocalDialogId();

        final JsonMessage jsonMessage = new JsonMessage();

        if (log.isInfoEnabled()) {
            log.info(String.format(INFO, dialogID, "<--- " + msg.getMAPDialog().getTCAPMessageType() + ": send-routing-info"));
        }

        final JsonSccp sccp = sccp(msg.getMAPDialog());
        final JsonTcap tcap = tcap(msg.getMAPDialog(), (LocalDialog) msg.getMAPDialog().getUserObject());


        jsonMessage.setSccp(sccp);
        jsonMessage.setTcap(tcap);

        JsonMapOperation op = new JsonMapOperation();
        op.setImsi(msg.getIMSI().getData());
        op.setExtendedRoutingInfo(callHandlingParameters.jsonExtendedRoutingInfo(msg.getExtendedRoutingInfo()));


        JsonMap map = new JsonMap("send-routing-info", op);


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
    public void onProvideRoamingNumberRequest(ProvideRoamingNumberRequest request) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onProvideRoamingNumberResponse(ProvideRoamingNumberResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onIstCommandRequest(IstCommandRequest request) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onIstCommandResponse(IstCommandResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
