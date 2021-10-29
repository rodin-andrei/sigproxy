/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.gw;

import com.google.common.cache.CacheBuilder;
import com.unifun.sigtran.ussdgateway.gw.context.ExecutionContext;
import com.unifun.sigtran.ussdgateway.gw.context.HttpLocalContext;
import com.unifun.sigtran.ussdgateway.map.dto.*;
import lombok.extern.log4j.Log4j2;
import org.restcomm.protocols.ss7.map.api.*;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.restcomm.protocols.ss7.map.api.primitives.AddressString;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.api.TCAPStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Future;

/**
 * This class implements MAP related RX/TX processes.
 *
 * @author okulikov
 */
@Log4j2
public abstract class MapBaseChannel implements Channel {

    public static final ConcurrentMap<Long, JsonInteractiveProxyDTO> proxyIdMatcherMap =
            CacheBuilder.newBuilder().expireAfterAccess(Duration.ofMinutes(2)).<Long, JsonInteractiveProxyDTO>build().asMap();
    public final static String INFO = "(%s) %s";
    private static final Logger LOGGER_MAP = LoggerFactory.getLogger("MapLogger");
    private static final String MAP_LOGGING_STRING_FORMAT_TRANSMITTED = "[MAP-SEND ] [%-31s: %16s] [%12s] [DialogId: %11s] [PC: %5s] [SSN: %4s] [GT: %13s] [Tx: \"%-10s\"]";
    private final Gateway gateway;
    //Reference for the MAP Provider
    private final MAPProvider mapProvider;
    //Reference for the SCCP Provider
    private final TCAPStack tcapStack;
    private final SccpParameters sccpParameters;
    private final TcapParameters tcapParameters;

    /**
     * Constructs new instance of this class.
     *
     * @param gateway
     */
    public MapBaseChannel(Gateway gateway) {
        this.gateway = gateway;
        this.mapProvider = getGateway().getMapService().getMapStack(gateway.getMobile().getNAME()).getMAPProvider();
        this.tcapStack = getGateway().getTcapService().getTcapStack(getGateway().getMobile().getNAME());
        this.sccpParameters = new SccpParameters(getGateway().getSccpService().getSccpProvider(getGateway().getMobile().getNAME()).getParameterFactory());
        this.tcapParameters = new TcapParameters(mapProvider.getMAPParameterFactory());
    }

    public MAPProvider getMapProvider() {
        return mapProvider;
    }

    public TCAPStack tcapStack() {
        return tcapStack;
    }

    public TcapParameters getTcapParameters() {
        return tcapParameters;
    }

    public Gateway getGateway() {
        return gateway;
    }

    public abstract MAPServiceBase service();

    @Override
    public synchronized Future send(String url, JsonMessage msg, ExecutionContext context) {
        //verify parameters
        assert msg != null : "Message can not be null";

        //break original message into parts
        JsonTcap tcap = msg.getTcap();
        JsonSccp sccp = msg.getSccp();

        long dId = tcap.getDialog().getDialogId();

        //create or find dialog
        //if dialog exists the existing dialog will be returned
        //if not exits then new dialog will be created 
        MAPDialog mapDialog;

        try {
            if (log.isDebugEnabled()) {
                log.debug("Starting or creating dialog " + dId);
            }
            mapDialog = mapDialog(sccp, tcap, context);

            if (context instanceof HttpLocalContext) {
                ((HttpLocalContext) context).setMapDialog(mapDialog);
            }
        } catch (Exception e) {
            log.error("Could not access or create dialog", e);
            context.failed(e);
            return null;
        }

        ((LocalDialog) mapDialog.getUserObject()).setContext(context);

        if (log.isDebugEnabled()) {
            log.debug("Adding components " + dId);
        }
        //extract component from TCAP part        
        if (tcap.getComponents() != null) {
            try {
                JsonComponent component = tcap.getComponents().getComponent(0);
                addComponent(mapDialog, component);
            } catch (MAPException e) {
                log.error("Could not add component to the dialog", e);
                context.failed(e);
                return null;
            }
        }

        if (log.isTraceEnabled()) {
            log.trace("TX: " + msg);
        }

        if (log.isInfoEnabled()) {
            if (tcap.getComponents() != null) {
                JsonComponent component = tcap.getComponents().getComponent(0);
                JsonMap map = mapMessage(component);
                log.info(String.format(INFO, mapDialog.getUserObject().toString(), "---> " + tcap.getType() + ":" + map.operationName()));
            }
        }

        try {
            //wrap with suitable component and sendOverMap the message
            switch (tcap.getType()) {
                case "Begin":
                case "Continue":
                    mapDialog.send();
                    break;
                case "End":
                    mapDialog.close(false);
                    break;
            }

            //assign dialog id and send as provisional response
            if (context != null) {
                context.ack(provisionalResponse(msg.getTcap().getType(), mapDialog.getLocalDialogId()));
            }
        } catch (Exception e) {
            log.warn(String.format(INFO, dId, "Could not send message: " + e.getMessage()), e);
            logMapSend(tcap, mapDialog, dId, "Could not send message: " + e.getMessage());
            mapDialog.release();

            if (context != null) {
                context.failed(e);
            }
        }


        logMapSend(tcap, mapDialog, dId, "");

        return null;
    }

    private void logMapSend(JsonTcap tcap, MAPDialog mapDialog, long dId, String dummyIfNoException) {
        try {
            String msisdn = ((LocalDialog) mapDialog.getUserObject()).getMsisdn();
            LOGGER_MAP.info(String.format(MAP_LOGGING_STRING_FORMAT_TRANSMITTED,
                    mapMessage(tcap.getComponents().getComponent(0)).operationName(),
                    tcap.getComponents().getComponent(0).getType(),
                    msisdn,
                    dId,
                    null,
                    null,
                    null,
                    dummyIfNoException + getUssdString(tcap.toString())
            ));
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            LOGGER_MAP.error(String.format(MAP_LOGGING_STRING_FORMAT_TRANSMITTED,
                    null,
                    null,
                    null,
                    dId,
                    null,
                    null,
                    null,
                    e.getMessage()
            ));
        }
    }

    private String getUssdString(String value) {
        try {
            if (value == null || "".equals(value)) {
                return "null";
            }
            int firstIndex = value.indexOf("ussdString") + 13;
            int secondIndex = value.indexOf("coding-scheme") - 3;

            return value.substring(firstIndex, secondIndex);
        } catch (NullPointerException | StringIndexOutOfBoundsException e) {
            return "null";
        }
    }

    /**
     * Creates components using json description.
     *
     * @param dialog
     * @param component
     * @throws MAPException
     */
    public abstract void addComponent(MAPDialog dialog, JsonComponent component) throws MAPException;

    /**
     * Extracts MAP message from TCAP component.
     *
     * @param component
     * @return
     */
    public JsonMap mapMessage(JsonComponent component) {
        switch (component.getType()) {
            case "invoke":
                JsonInvoke invoke = (JsonInvoke) component.getValue();
                return (JsonMap) invoke.component();
            case "returnResultLast":
                JsonReturnResultLast returnResultLast = (JsonReturnResultLast) component.getValue();
                return (JsonMap) returnResultLast.component();
            default:
                return null;
        }
    }

    private String contextName(JsonTcap tcap) {
        return tcap.getDialog().getApplicationContextName() == null
                || tcap.getDialog().getApplicationContextName().trim().length() == 0
                ? MAPApplicationContextName.networkUnstructuredSsContext.name()
                : tcap.getDialog().getApplicationContextName().trim();
    }

    private String version(JsonTcap tcap) {
        return tcap.getDialog().getVersion() == null
                ? MAPApplicationContextVersion.version2.name()
                : tcap.getDialog().getVersion().trim();
    }

    /**
     * Builds MAP dialog.
     * <p>
     * If other side works in load sharing mode then we can receive intermediate
     * messages related to some existing transaction. In case of TCAP BEGIN we
     * will always create new dialog. For other message types we will try to
     * find existing dialog first and create new one only when it does not
     * exist. Later this dialog might be closed explicit or expired.
     *
     * @param sccp
     * @param tcap
     * @return
     * @throws MAPException
     */
    public MAPDialog mapDialog(JsonSccp sccp, JsonTcap tcap, ExecutionContext executionContext) throws MAPException {
        MAPDialog dialog = mapProvider.getMAPDialog(tcap.getDialog().getDialogId());

        if (dialog == null) {
            assert sccp != null : "SCCP part should be defined for each new dialog";

            ISDNAddressString origReference = tcapParameters.isdnAddressString(tcap.getDialog().getOriginationReference());
            ISDNAddressString destReference = tcapParameters.isdnAddressString(tcap.getDialog().getDestinationReference());

            SccpAddress callingPartyAddress = sccpParameters.createSccpAddress(sccp.getCallingPartyAddress());
            SccpAddress calledPartyAddress = sccpParameters.createSccpAddress(sccp.getCalledPartyAddress());

            if (log.isTraceEnabled()) {
                String ctxName = contextName(tcap);
                String version = version(tcap);

                log.trace("Application context " + ctxName + ", version " + version);
            }

            MAPApplicationContextName context = MAPApplicationContextName.valueOf(contextName(tcap));
            MAPApplicationContextVersion version = MAPApplicationContextVersion.valueOf(version(tcap));

            dialog = service()
                    .createNewDialog(
                            MAPApplicationContext.getInstance(context, version),
                            callingPartyAddress,
                            origReference,
                            calledPartyAddress,
                            destReference
                    );

            if (executionContext instanceof LocalChannel.LocalContext) {
                proxyIdMatcherMap.putIfAbsent(((LocalChannel.LocalContext) executionContext).getDialogId(),
                        new JsonInteractiveProxyDTO(dialog.getLocalDialogId()));
            }
            tcap.getDialog().setDialogId(dialog.getLocalDialogId());
            LocalDialog localDialog = new LocalDialog(tcap.getDialog());

            //Return message on error
            if (tcap.getDialog().isReturnMessageOnError()) {
                dialog.setReturnMessageOnError(true);
            }

            if (this.isEricsson(tcap.getDialog())) {
                ISDNAddressString msisdn = null;
                if (tcap.getDialog().getMsisdn() != null) {
                    msisdn = tcapParameters.isdnAddressString(tcap.getDialog().getMsisdn());
                }

                ISDNAddressString vlrAddress = null;
                if (tcap.getDialog().getVlrAddress() != null) {
                    vlrAddress = tcapParameters.isdnAddressString(tcap.getDialog().getVlrAddress());
                }

                dialog.addEricssonData(msisdn, vlrAddress);

                if (msisdn != null) {
                    localDialog.setMsisdn(msisdn.getAddress());
                }
            }

            //create new local dialog for just created MAP dialog
            dialog.setUserObject(localDialog);
            log.info(String.format(INFO, localDialog, "---> Started"));
        } else if (sccp != null) {
            SccpAddress callingPartyAddress = sccpParameters.createSccpAddress(sccp.getCallingPartyAddress());
            SccpAddress calledPartyAddress = sccpParameters.createSccpAddress(sccp.getCalledPartyAddress());

            dialog.setRemoteAddress(calledPartyAddress);
            dialog.setLocalAddress(callingPartyAddress);
        }
        this.gateway.getUsageStats().getIncDialogs().updateStarted();
        return dialog;
    }

    /**
     * Test given TCAP dialog for Ericsson style.
     *
     * @param dialog
     * @return
     */
    public boolean isEricsson(JsonTcapDialog dialog) {
        return dialog.getVlrAddress() != null || dialog.getMsisdn() != null;
    }

    public JsonSccp sccp(MAPDialog mapDialog) {
        final JsonSccp sccp = new JsonSccp();
        if (mapDialog.getLocalAddress() != null) {
            sccp.setCallingPartyAddress(sccpParameters.toJsonFormat(mapDialog.getLocalAddress()));
        }

        if (mapDialog.getRemoteAddress() != null) {
            sccp.setCalledPartyAddress(sccpParameters.toJsonFormat(mapDialog.getRemoteAddress()));
        }
        return sccp;
    }

    public JsonTcap tcap(MAPDialog mapDialog, LocalDialog dialog) {
        JsonTcapDialog tcapDialog = dialog.getDialog();
        if (tcapDialog == null) {
            tcapDialog = new JsonTcapDialog();

            AddressString origReference = mapDialog.getReceivedDestReference();
            AddressString destReference = mapDialog.getReceivedDestReference();

            tcapDialog.setOriginationReference(tcapParameters.jsonAddressString(origReference));
            tcapDialog.setDestinationReference(tcapParameters.jsonAddressString(destReference));
        }

        //always check the dialog id
        tcapDialog.setDialogId(mapDialog.getLocalDialogId());

        final JsonTcap tcap = new JsonTcap();
        tcap.setDialog(tcapDialog);

        return tcap;
    }

    private JsonMessage provisionalResponse(String type, long dialogId) {
        final JsonTcapDialog dialog = new JsonTcapDialog();
        dialog.setDialogId(dialogId);

        final JsonTcap tcap = new JsonTcap();
        tcap.setDialog(dialog);
        tcap.setType(type);

        final JsonMessage msg = new JsonMessage();
        msg.setTcap(tcap);

        return msg;
    }

    public void onErrorComponent(MAPDialog mapDialog, Long invokeId, MAPErrorMessage mapErrorMessage) {
        long dialogID = mapDialog.getLocalDialogId();

        final JsonMessage jsonMessage = new JsonMessage();

        if (log.isInfoEnabled()) {
            log.info(String.format(INFO, dialogID, "<--- " + mapDialog.getTCAPMessageType() + ": returnError"));
        }

        final JsonSccp sccp = sccp(mapDialog);
        final JsonTcap tcap = tcap(mapDialog, (LocalDialog) mapDialog.getUserObject());

        jsonMessage.setSccp(sccp);
        jsonMessage.setTcap(tcap);

        final JsonComponent component = new JsonComponent();
        final JsonReturnError returnError = new JsonReturnError(invokeId);
        returnError.setErrorCode(mapErrorMessage.getErrorCode());
        component.setType("returnError");
        component.setValue(returnError);

        final JsonComponents components = new JsonComponents();
        components.addComponent(component);
        tcap.setComponents(components);

        if (log.isTraceEnabled()) {
            SccpAddress src = mapDialog.getRemoteAddress();
            int pc = src.getSignalingPointCode();
            log.trace(String.format("RX : %d : %s", pc, jsonMessage));
        }

        ExecutionContext context = ((LocalDialog) mapDialog.getUserObject()).getContext();

        if (context == null) {
            log.warn(String.format(INFO, mapDialog.getUserObject().toString(), "Out of context"));
            return;
        }

        context.completed(jsonMessage);
    }

}
