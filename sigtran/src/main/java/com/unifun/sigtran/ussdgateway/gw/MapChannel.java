/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.gw;

import com.unifun.sigtran.ussdgateway.gw.context.ExecutionContext;
import com.unifun.sigtran.ussdgateway.gw.stats.DialogStats;
import com.unifun.sigtran.ussdgateway.map.dto.JsonMessage;
import com.unifun.sigtran.ussdgateway.map.dto.JsonTcapDialog;
import com.unifun.sigtran.ussdgateway.map.service.callhandling.MapCallHandlingChannel;
import com.unifun.sigtran.ussdgateway.map.service.mobility.MapMobilityChannel;
import com.unifun.sigtran.ussdgateway.map.service.sm.MapSmChannel;
import com.unifun.sigtran.ussdgateway.map.service.ussd.*;
import com.unifun.sigtran.ussdgateway.properties.notification.DialogTimeoutProperties;
import com.unifun.sigtran.ussdgateway.service.notification.HttpNotificationService;
import com.unifun.sigtran.ussdgateway.util.URL;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.restcomm.protocols.ss7.map.api.MAPDialog;
import org.restcomm.protocols.ss7.map.api.MAPDialogListener;
import org.restcomm.protocols.ss7.map.api.MAPProvider;
import org.restcomm.protocols.ss7.map.api.dialog.*;
import org.restcomm.protocols.ss7.map.api.primitives.AddressString;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.tcap.api.MessageType;
import org.restcomm.protocols.ss7.tcap.asn.ApplicationContextName;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

/**
 * Generic MAP channel.
 *
 * @author okulikov
 */
@Component
@RequiredArgsConstructor
@Log4j2
public class MapChannel implements Channel, MAPDialogListener {

    private final static String INFO = "(DID:%s) %s";
    private final HttpNotificationService httpNotificationService;
    private final DialogTimeoutProperties timeoutProperties;
    private final Gateway gateway;
    //Reference for the MAP Provider
    private MAPProvider mapProvider;
    private TcapParameters tcapParameters;
    //MAP service specific channels;
    private MapMobilityChannel mobilityChannel;
    private MapUssdChannel ussdChannel;
    private MapCallHandlingChannel callHandlingChannel;
    private MapSmChannel smsChannel;
    private TestUssdChannel testUssdChannel;
    private SendAbortUssdChannel sendAbortUssdChannel;
    private SendEmptyResponseUssdChannel sendEmptyResponseUssdChannel;
    private SendEndUssdChannel sendEndUssdChannel;


    public void init() throws Exception {
        this.gateway.setMapChannel(this);
        this.mapProvider = gateway.getMapService().getMapStack(gateway.getMobile().getNAME()).getMAPProvider();
        this.tcapParameters = new TcapParameters(mapProvider.getMAPParameterFactory());

        this.mobilityChannel = new MapMobilityChannel(gateway);
        this.ussdChannel = new MapUssdChannel(gateway);
        this.callHandlingChannel = new MapCallHandlingChannel(gateway);
        this.smsChannel = new MapSmChannel(gateway);
        this.testUssdChannel = new TestUssdChannel();
        this.sendAbortUssdChannel = new SendAbortUssdChannel();
        this.sendEmptyResponseUssdChannel = new SendEmptyResponseUssdChannel();
        this.sendEndUssdChannel = new SendEndUssdChannel();
        mapProvider.addMAPDialogListener(this);
        this.start();
    }

    public Channel channel(URL url) throws UnknownProtocolException {
        switch (url.host()) {
            case "ussd":
                return ussdChannel;
            case "mobility":
                return mobilityChannel;
            case "callhandling":
                return callHandlingChannel;
            case "sms":
                return smsChannel;
            case "test-ussd":
                return testUssdChannel;
            case "abort-ussd":
                return sendAbortUssdChannel;
            case "empty-ussd":
                return sendEmptyResponseUssdChannel;
            case "end-ussd":
                return sendEndUssdChannel;
            default:
                throw new UnknownProtocolException("Unknown channel: " + url);
        }
    }

    @Override
    public void start() throws Exception {
        this.ussdChannel.start();
        this.mobilityChannel.start();
        this.callHandlingChannel.start();
        this.smsChannel.start();
        this.testUssdChannel.start();
    }

    @Override
    public Future send(String url, JsonMessage msg, ExecutionContext context) {
        URL u = new URL(url);
        switch (u.host()) {
            case "ussd":
                return ussdChannel.send(url, msg, context);
            case "mobility":
                return mobilityChannel.send(url, msg, context);
            case "callhandling":
                return callHandlingChannel.send(url, msg, context);
            case "sms":
                return smsChannel.send(url, msg, context);
            case "test-ussd":
                return testUssdChannel.send(url, msg, context);
            default:
                context.failed(new IllegalArgumentException(u.host()));
        }
        return null;
    }

    @Override
    public void stop() {
        this.ussdChannel.stop();
        this.mobilityChannel.stop();
        this.callHandlingChannel.stop();
        this.smsChannel.stop();
        this.testUssdChannel.stop();
    }

    @Override
    public void onDialogDelimiter(MAPDialog mapDialog) {
    }

    @Override
    public void onDialogRequest(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
                                MAPExtensionContainer extensionContainer) {
        log.info(String.format(INFO, mapDialog.getLocalDialogId(), "<--- Started"));
//        checkLoop(mapDialog, destReference);
        JsonTcapDialog dialog = new JsonTcapDialog();
        try {
            log.info(String.format(INFO, mapDialog.getLocalDialogId(), "<--- 1"));
            dialog.setDialogId(mapDialog.getLocalDialogId());
            log.info(String.format(INFO, mapDialog.getLocalDialogId(), "<--- 2"));
            dialog.setDestinationReference(tcapParameters.jsonAddressString(destReference));
            log.info(String.format(INFO, mapDialog.getLocalDialogId(), "<--- 3"));
            dialog.setOriginationReference(tcapParameters.jsonAddressString(origReference));
            log.info(String.format(INFO, mapDialog.getLocalDialogId(), "<--- 4"));
            dialog.setReturnMessageOnError(mapDialog.getReturnMessageOnError());
            log.info(String.format(INFO, mapDialog.getLocalDialogId(), "<--- 5"));

            mapDialog.setUserObject(new LocalDialog(dialog));
            log.info(String.format(INFO, mapDialog.getLocalDialogId(), "<--- 6"));
        } catch (Exception e) {
            log.error("Unexpected error while creating local dialog", e);
        }

        if (log.isDebugEnabled()) {
            log.debug(String.format(INFO, mapDialog.getLocalDialogId(), "Local dialog created"));
        }
        gateway.getUsageStats().getIncDialogs().updateStarted();
    }

//    private void checkLoop(MAPDialog mapDialog, AddressString destReference) {
//        log.info("Message Type: " + mapDialog.getTCAPMessageType().getTag() +" "+  mapDialog.getTCAPMessageType().name() );
//        if (MessageType.Begin.getTag() == mapDialog.getTCAPMessageType().getTag()){
//            log.info("Loop protection check:");
//            if (semaphore.containsValue(destReference.getAddress())){
//                log.info("Dialog is detected as loop, close it");
//                try {
//                    mapDialog.close(false);
//                } catch (MAPException e) {
//                   log.warn("Unable to close Begin tcap dialog  of looped session",e);
//                }
//            }
//            semaphore.put(mapDialog.getLocalDialogId(),destReference.getAddress());
//        }
//    }

    @Override
    public void onDialogRequestEricsson(MAPDialog mapDialog, AddressString destReference, AddressString origReference, AddressString msisdn, AddressString vlrNo) {
        log.info(String.format(INFO, mapDialog.getLocalDialogId(), "<--- Started Ericsson"));
//        checkLoop(mapDialog, destReference);
        JsonTcapDialog dialog = new JsonTcapDialog();
        dialog.setDialogId(mapDialog.getLocalDialogId());
        dialog.setDestinationReference(tcapParameters.jsonAddressString(destReference));
        dialog.setOriginationReference(tcapParameters.jsonAddressString(origReference));
        dialog.setReturnMessageOnError(mapDialog.getReturnMessageOnError());
        dialog.setMsisdn(tcapParameters.jsonAddressString(msisdn));
        dialog.setVlrAddress(tcapParameters.jsonAddressString(vlrNo));

        mapDialog.setUserObject(new LocalDialog(dialog));
        gateway.getUsageStats().getIncDialogs().updateStarted();
    }

    @Override
    public void onDialogAccept(MAPDialog mapDialog, MAPExtensionContainer extensionContainer) {
        log.info(String.format(INFO, mapDialog.getLocalDialogId(), "<--- Accepted"));
    }

    @Override
    public void onDialogReject(MAPDialog mapDialog, MAPRefuseReason refuseReason, ApplicationContextName alternativeApplicationContext, MAPExtensionContainer extensionContainer) {
        log.info(String.format(INFO, mapDialog.getLocalDialogId(), "<--- Rejected"));
        this.getDialogStats(mapDialog).updateRejected();
        this.getDialogStats(mapDialog).updateUserAborted();
        try {
            LocalDialog d = (LocalDialog) mapDialog.getUserObject();
            if (d.getContext() != null) {
                d.getContext().failed(new IllegalStateException("Released Component"));
            }
        } catch (Exception e) {
            log.error("onDialogRelease", e);
        }
    }

    @Override
    public void onDialogUserAbort(MAPDialog mapDialog, MAPUserAbortChoice userReason, MAPExtensionContainer extensionContainer) {
        log.info(String.format(INFO, mapDialog.getLocalDialogId(), "<--- Aborted by user"));
        this.getDialogStats(mapDialog).updateUserAborted();
    }

    @Override
    public void onDialogProviderAbort(MAPDialog mapDialog, MAPAbortProviderReason abortProviderReason, MAPAbortSource abortSource, MAPExtensionContainer extensionContainer) {
        log.info(String.format(INFO, mapDialog.getLocalDialogId(), "<--- Aborted by provider"));
        this.getDialogStats(mapDialog).updateProviderAborted();
    }

    @Override
    public void onDialogClose(MAPDialog mapDialog) {
        this.getDialogStats(mapDialog).updateClosed();
    }

    @Override
    public void onDialogNotice(MAPDialog mapDialog, MAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
        log.info(String.format(INFO, mapDialog.getLocalDialogId(), "<--- Noticed"));
    }

    @Override
    public void onDialogRelease(MAPDialog mapDialog) {
//        try{
//           if (semaphore.containsKey(mapDialog.getLocalDialogId())){
//               semaphore.remove(mapDialog.getLocalDialogId());
//           }
//        }catch(Exception e){
//            log.warn("Can'r release",e);
//        }
        this.getDialogStats(mapDialog).updateReleased();
        if (log.isInfoEnabled()) {
            log.info(String.format(INFO, mapDialog.getLocalDialogId(), "<--- Released"));
        }
    }

    @Override
    public void onDialogTimeout(MAPDialog mapDialog) {
        log.info(String.format(INFO, mapDialog.getLocalDialogId(), "Timeout"));

        Long dialogId = mapDialog.getLocalDialogId();
        if (mapDialog.getTCAPMessageType() == MessageType.Continue &&
                mapDialog.getRemoteDialogId() != null &&
                !mapDialog.getRemoteDialogId().equals(mapDialog.getLocalDialogId())) {
            dialogId = mapDialog.getRemoteDialogId();
        }

        httpNotificationService.notifyClients(timeoutProperties.getUrlList(),
                MediaType.APPLICATION_JSON_VALUE,
                HttpMethod.POST,
                "{\"dialogId\":\"" + dialogId + "\"}",
                null
        );

        this.getDialogStats(mapDialog).updateTimedOut();
    }

    public DialogStats getDialogStats(final MAPDialog mapDialog) {
        final LocalDialog d = (LocalDialog) mapDialog.getUserObject();
        if (d == null) {
            return this.gateway.getUsageStats().getOutDialogs();
        }
        if (d.isRemoteOriginated()) {
            return this.gateway.getUsageStats().getIncDialogs();
        }
        return this.gateway.getUsageStats().getOutDialogs();
    }

}
