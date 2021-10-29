/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.gw.context;

import com.unifun.sigtran.ussdgateway.gw.Channel;
import com.unifun.sigtran.ussdgateway.map.dto.JsonMessage;
import org.restcomm.protocols.ss7.map.api.MAPDialog;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.dialog.MAPUserAbortChoiceImpl;

/**
 * @author okulikov
 */
public class UssdLocalContext implements ExecutionContext {

    private final MAPDialog mapDialog;
    private final Channel ussdChannel;
    private final String url;
    private final Channel spareChannel;
    private final JsonMessage msg;
    private int fcount = 0;

    /**
     * Creates context instance.
     *
     * @param mapDialog
     * @param ussdChannel  originated map channel to which this context belongs.
     * @param url          spare URL which should be used in case of failure.
     * @param msg          original message
     * @param spareChannel
     */
    public UssdLocalContext(MAPDialog mapDialog, Channel ussdChannel, String url, JsonMessage msg, Channel spareChannel) {
        this.mapDialog = mapDialog;
        this.ussdChannel = ussdChannel;
        this.url = url;
        this.msg = msg;
        this.spareChannel = spareChannel;
    }

    @Override
    public void completed(JsonMessage msg) {
        //reply on original ussd channel directly
        ussdChannel.send(null, msg, null);
    }

    @Override
    public void failed(Exception e) {
        if (++fcount < 2) {
            //try to deliver over spare channel
            spareChannel.send(url, msg, this);
        } else if (mapDialog != null) {

            //TODO: replace this part of code with canonnical channel.send(abortMessage)
            MAPUserAbortChoiceImpl mapUserAbortChoice = new MAPUserAbortChoiceImpl();
            mapUserAbortChoice.setUserResourceLimitation();

            try {
                mapDialog.abort(mapUserAbortChoice);
            } catch (Exception ex) {
            } finally {
                mapDialog.release();
            }
        }
    }

    @Override
    public void cancelled() {
        failed(null);
//        if (mapDialog != null) {
//            mapDialog.release();
//        }
    }

    @Override
    public void ack(JsonMessage msg) {
    }

    public void close() {
        try {
            mapDialog.close(false);
        } catch (MAPException e) {
            e.printStackTrace();
        }
    }

    public void abort() {
        MAPUserAbortChoiceImpl mapUserAbortChoice = new MAPUserAbortChoiceImpl();
        mapUserAbortChoice.setUserSpecificReason();
        try {
            mapDialog.abort(mapUserAbortChoice);
        } catch (MAPException e) {
            e.printStackTrace();
        } finally {
            mapDialog.release();
        }
    }

    public void empty() {
        try {
            mapDialog.send();
        } catch (MAPException e) {
            e.printStackTrace();
        }
    }

}
