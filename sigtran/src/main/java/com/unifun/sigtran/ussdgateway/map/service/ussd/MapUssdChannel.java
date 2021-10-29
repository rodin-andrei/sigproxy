/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.map.service.ussd;

import com.unifun.sigtran.ussdgateway.gw.*;
import com.unifun.sigtran.ussdgateway.gw.config.UssdConfig;
import com.unifun.sigtran.ussdgateway.gw.context.ExecutionContext;
import com.unifun.sigtran.ussdgateway.gw.context.UssdLocalContext;
import com.unifun.sigtran.ussdgateway.map.dto.*;
import lombok.extern.log4j.Log4j2;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.restcomm.protocols.ss7.map.MAPProviderImpl;
import org.restcomm.protocols.ss7.map.api.*;
import org.restcomm.protocols.ss7.map.api.datacoding.CBSDataCodingGroup;
import org.restcomm.protocols.ss7.map.api.datacoding.CBSNationalLanguage;
import org.restcomm.protocols.ss7.map.api.primitives.AlertingPattern;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.USSDString;
import org.restcomm.protocols.ss7.map.api.service.supplementary.*;
import org.restcomm.protocols.ss7.map.api.smstpdu.CharacterSet;
import org.restcomm.protocols.ss7.map.api.smstpdu.DataCodingSchemaMessageClass;
import org.restcomm.protocols.ss7.map.datacoding.CBSDataCodingSchemeImpl;
import org.restcomm.protocols.ss7.map.primitives.AlertingPatternImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.ProcessUnstructuredSSRequestImpl;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.asn.TcapFactory;
import org.restcomm.protocols.ss7.tcap.asn.comp.Invoke;
import org.restcomm.protocols.ss7.tcap.asn.comp.OperationCode;
import org.restcomm.protocols.ss7.tcap.asn.comp.Parameter;
import org.restcomm.protocols.ss7.tcap.asn.comp.Problem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * This class implements MAP related RX/TX processes.
 *
 * @author okulikov
 */
@Log4j2
public class MapUssdChannel extends MapBaseChannel implements MAPServiceSupplementaryListener {
    private static final Logger LOGGER_MAP = LoggerFactory.getLogger("MapLogger");
    private static final String MAP_LOGGING_STRING_FORMAT_RECEIVED = "[MAP-PROC ] [%-31s: %16s] [%12s] [DialogId: %11s] [PC: %5s] [SSN: %4s] [GT: %13s] [Rx: \"%-10s\"]";
    private static final String MAP_LOGGING_STRING_FORMAT_RECEIVED_EVNT = "[MAP-EVENT] [%-31s: %16s] [%12s] [DialogId: %11s] [PC: %5s] [SSN: %4s] [GT: %13s] [Rx: \"%-10s\"]";
    private static final String MAP_LOGGING_STRING_FORMAT_TRANSMITTED = "[MAP-PROC ] [%-31s: %16s] [%12s] [DialogId: %11s] [PC: %5s] [SSN: %4s] [GT: %13s] [Tx: \"%-10s\"]";
    private static final ScheduledExecutorService EXECUTOR = Executors.newSingleThreadScheduledExecutor();
    private final UssdRouter ussdRouter = new UssdRouter();
    private Long initialPUSSRInvokeId = 0L;

    /**
     * Constructs new instance of this class.
     *
     * @param gateway
     */
    public MapUssdChannel(Gateway gateway) {
        super(gateway);
    }

    public UssdRouter ussdRouter() {
        return ussdRouter;
    }

    @Override
    public void start() throws Exception {
        log.info("Loading USSD routes");
        UssdConfig ussdConfig = getGateway().getConfig().getUssdConfig();
        ussdConfig.routes().forEach(r -> ussdRouter.insert(-1, r));

        log.info("Activating USSD service");
        getMapProvider().getMAPServiceSupplementary().acivate();
        getMapProvider().getMAPServiceSupplementary().addMAPServiceListener(this);

        log.info("USSD channel successfully started");
    }

    @Override
    public void stop() {
        getMapProvider().getMAPServiceSupplementary().removeMAPServiceListener(this);
        getMapProvider().getMAPServiceSupplementary().deactivate();
    }

    @Override
    public void addComponent(MAPDialog dialog, JsonComponent component) throws MAPException {
        JsonMap map = mapMessage(component);
        if (log.isDebugEnabled()) {
            log.debug("Component: " + map.operationName());
        }
        switch (map.operationName()) {
            case "process-unstructured-ss-request":
                processUnstructuredSSRequest((MAPDialogSupplementary) dialog, component, component.getType());
                break;
            case "unstructured-ss-request":
                unstructuredSSRequest((MAPDialogSupplementary) dialog, component, component.getType());
                break;
            case "unstructured-ss-notify-request":
                unstructuredNotifySSRequest((MAPDialogSupplementary) dialog, component, component.getType());
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
    private void processUnstructuredSSRequest(MAPDialogSupplementary dialog, JsonComponent component, String type) throws MAPException {
        if (log.isDebugEnabled()) {
            log.debug("process-unstructured-ss-request, type= " + type);
        }

        JsonMapOperation mapMessage = (JsonMapOperation) mapMessage(component).operation();

        MAPParameterFactory mapParameterFactory = getMapProvider().getMAPParameterFactory();

        ISDNAddressString msisdn = getTcapParameters().isdnAddressString(mapMessage.getMsisdn());
        CBSDataCodingSchemeImpl codingScheme = codingScheme(mapMessage.getCodingScheme());

        String text = mapMessage.getUssdString();
        text = text.replaceAll("--newline--", "\n").replaceAll("--return--", "\r");

        USSDString ussdString = mapParameterFactory.createUSSDString(text, codingScheme, StandardCharsets.UTF_8);

        AlertingPattern alertingPattern = mapMessage.getAlertingPattern() != null
                ? new AlertingPatternImpl(Integer.parseInt(mapMessage.getAlertingPattern(), 16)) : null;

        if (log.isDebugEnabled()) {
            log.debug("process-unstructured-ss-request, build parameters ");
        }
        switch (type) {
            case "invoke":
                if (log.isDebugEnabled()) {
                    log.debug("process-unstructured-ss-request: invoke ");
                }

                if (super.getGateway().getAppProperties().getTakeInvokeIdFromJson()) {
                    this.addProcessUnstructuredSSRequest(((JsonInvoke) component.getValue()).getInvokeId(),
                            codingScheme,
                            ussdString,
                            alertingPattern,
                            msisdn,
                            dialog);
                } else {
                    dialog.addProcessUnstructuredSSRequest(codingScheme, ussdString, alertingPattern, msisdn);
                }
                if (msisdn != null) {
                    ((LocalDialog) dialog.getUserObject()).setMsisdn(msisdn.getAddress());
                }

                logProcessingRequestsReceived(dialog, component, type, msisdn, text);
                break;
            case "returnResultLast":
                if (log.isDebugEnabled()) {
                    log.debug("process-unstructured-ss-request: returnResultLast ");
                    log.debug("process-unstructured-ss-request: invokeID: " + initialPUSSRInvokeId);
                }
                dialog.addProcessUnstructuredSSResponse(initialPUSSRInvokeId, codingScheme, ussdString);

                logProcessingRequestsReceived(dialog, component, type, msisdn, text);
                break;
        }
    }

    /**
     * Sends PUSSR w/ custom InvokeId
     *
     * @param invokeId
     * @param codingScheme
     * @param ussdString
     * @param alertingPattern
     * @param msisdn
     * @param dialog
     * @return
     * @throws MAPException
     */
    private Long addProcessUnstructuredSSRequest(long invokeId, CBSDataCodingSchemeImpl codingScheme, USSDString ussdString, AlertingPattern alertingPattern, ISDNAddressString msisdn, MAPDialogSupplementary dialog) throws MAPException {
        Invoke invoke = ((MAPProviderImpl) this.getMapProvider()).getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();

        OperationCode oc = TcapFactory.createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.processUnstructuredSS_Request);
        invoke.setOperationCode(oc);

        ProcessUnstructuredSSRequestImpl req = new ProcessUnstructuredSSRequestImpl(codingScheme, ussdString,
                alertingPattern, msisdn);
        AsnOutputStream aos = new AsnOutputStream();
        req.encodeData(aos);

        Parameter p = ((MAPProviderImpl) this.getMapProvider()).getTCAPProvider().getComponentPrimitiveFactory().createParameter();
        p.setTagClass(req.getTagClass());
        p.setPrimitive(req.getIsPrimitive());
        p.setTag(req.getTag());
        p.setData(aos.toByteArray());
        invoke.setParameter(p);

        invoke.setInvokeId(invokeId);
        dialog.sendInvokeComponent(invoke);

        return invokeId;
    }

    /**
     * Appends UnstructuredSSRequest to the given Dialog.
     *
     * @param dialog
     * @param component
     * @param type
     * @throws MAPException
     */
    private void unstructuredSSRequest(MAPDialogSupplementary dialog, JsonComponent component, String type) throws MAPException {
        JsonMapOperation mapMessage = (JsonMapOperation) mapMessage(component).operation();
        MAPParameterFactory mapParameterFactory = getMapProvider().getMAPParameterFactory();

        ISDNAddressString msisdn = getTcapParameters().isdnAddressString(mapMessage.getMsisdn());
        CBSDataCodingSchemeImpl codingScheme = codingScheme(mapMessage.getCodingScheme());

        String text = mapMessage.getUssdString();
        text = text.replaceAll("--newline--", "\n").replaceAll("--return--", "\r");

        USSDString ussdString = mapParameterFactory.createUSSDString(text, codingScheme, StandardCharsets.UTF_8);
        AlertingPattern alertingPattern = mapMessage.getAlertingPattern() != null
                ? new AlertingPatternImpl(Integer.parseInt(mapMessage.getAlertingPattern(), 16)) : null;

        switch (type) {
            case "invoke":
                dialog.addUnstructuredSSRequest(codingScheme, ussdString, alertingPattern, msisdn);
                if (msisdn != null) {
                    ((LocalDialog) dialog.getUserObject()).setMsisdn(msisdn.getAddress());
                }

                logProcessingRequestsTransmitted(dialog, component, type, msisdn, text);

                break;
            case "returnResultLast":
                JsonReturnResultLast returnResultLast = (JsonReturnResultLast) component.getValue();
                dialog.addUnstructuredSSResponse(returnResultLast.getInvokeId(), codingScheme, ussdString);

                logProcessingRequestsReceived(dialog, component, type, msisdn, text);
                break;
        }
    }

    /**
     * Appends NotifySSRequest to the given Dialog.
     *
     * @param dialog
     * @param component
     * @param type
     * @throws MAPException
     */
    private void unstructuredNotifySSRequest(MAPDialogSupplementary dialog, JsonComponent component, String type) throws MAPException {
        JsonMapOperation mapMessage = (JsonMapOperation) mapMessage(component).operation();
        MAPParameterFactory mapParameterFactory = getMapProvider().getMAPParameterFactory();

        ISDNAddressString msisdn = getTcapParameters().isdnAddressString(mapMessage.getMsisdn());
        CBSDataCodingSchemeImpl codingScheme = codingScheme(mapMessage.getCodingScheme());

        String text = mapMessage.getUssdString();
        text = text.replaceAll("--newline--", "\n").replaceAll("--return--", "\r");

        USSDString ussdString = mapParameterFactory.createUSSDString(text, codingScheme, StandardCharsets.UTF_8);
        AlertingPattern alertingPattern = mapMessage.getAlertingPattern() != null
                ? new AlertingPatternImpl(Integer.parseInt(mapMessage.getAlertingPattern(), 16)) : null;

        switch (type) {
            case "invoke":
                dialog.addUnstructuredSSNotifyRequest(codingScheme, ussdString, alertingPattern, msisdn);
                if (msisdn != null) {
                    ((LocalDialog) dialog.getUserObject()).setMsisdn(msisdn.getAddress());
                }

                logProcessingRequestsReceived(dialog, component, type, msisdn, text);

                break;
            case "returnResultLast":
                JsonReturnResultLast returnResultLast = (JsonReturnResultLast) component.getValue();
                dialog.addUnstructuredSSNotifyResponse(returnResultLast.getInvokeId());

                logProcessingRequestsTransmitted(dialog, component, type, msisdn, text);

                break;
        }
    }

    private void logProcessingRequestsTransmitted(MAPDialogSupplementary dialog, JsonComponent component, String type, ISDNAddressString msisdn, String text) {
        try {
            LOGGER_MAP.info(String.format(MAP_LOGGING_STRING_FORMAT_TRANSMITTED,
                    mapMessage(component).operationName(),
                    type,
                    msisdn != null ? msisdn.getAddress() : "null",
                    dialog.getRemoteDialogId(),
                    dialog.getRemoteAddress().getSignalingPointCode(),
                    dialog.getRemoteAddress().getSubsystemNumber(),
                    dialog.getRemoteAddress().getGlobalTitle().getDigits(),
                    text
            ));
        } catch (NullPointerException e) {
            LOGGER_MAP.error(String.format(MAP_LOGGING_STRING_FORMAT_TRANSMITTED,
                    null,
                    type,
                    msisdn != null ? msisdn.getAddress() : "null",
                    null,
                    null,
                    null,
                    null,
                    e.getMessage()
            ));
        }
    }

    private void logProcessingRequestsReceived(MAPDialogSupplementary dialog, JsonComponent component, String type, ISDNAddressString msisdn, String text) {
        try {
            LOGGER_MAP.info(String.format(MAP_LOGGING_STRING_FORMAT_RECEIVED,
                    mapMessage(component).operationName(),
                    type,
                    msisdn != null ? msisdn.getAddress() : ((LocalDialog) dialog.getUserObject()).getMsisdn(),
                    dialog.getRemoteDialogId(),
                    dialog.getRemoteAddress().getSignalingPointCode(),
                    dialog.getRemoteAddress().getSubsystemNumber(),
                    dialog.getRemoteAddress().getGlobalTitle().getDigits(),
                    text
            ));
        } catch (NullPointerException e) {
            LOGGER_MAP.error(String.format(MAP_LOGGING_STRING_FORMAT_RECEIVED,
                    null,
                    type,
                    msisdn != null ? msisdn.getAddress() : "null",
                    null,
                    null,
                    null,
                    null,
                    e.getMessage()
            ));
        }
    }

    /**
     * Constructs coding scheme from corresponding Json object.
     *
     * @param scheme
     * @return
     */
    private CBSDataCodingSchemeImpl codingScheme(JsonDataCodingScheme scheme) {
        return new CBSDataCodingSchemeImpl(
                CBSDataCodingGroup.valueOf(scheme.getCodingGroup()),
                CharacterSet.valueOf(scheme.getLanguage()),
                scheme.getNationalLanguage() != null ? CBSNationalLanguage.valueOf(scheme.getNationalLanguage()) : null,
                scheme.getMessageClass() != null ? DataCodingSchemaMessageClass.valueOf(scheme.getMessageClass()) : null,
                false
        );
    }

    private String ussdString(UssMessage msg) {
        JsonComponent component = msg.getTcap().getComponents().getComponent(0);
        JsonMap map = mapMessage(component);
        JsonMapOperation op = (JsonMapOperation) map.operation();
        return op.getUssdString();
    }

    @Override
    public void onRegisterSSRequest(RegisterSSRequest request) {
    }

    @Override
    public void onRegisterSSResponse(RegisterSSResponse response) {
    }

    @Override
    public void onEraseSSRequest(EraseSSRequest request) {
    }

    @Override
    public void onEraseSSResponse(EraseSSResponse response) {
    }

    @Override
    public void onActivateSSRequest(ActivateSSRequest request) {
    }

    @Override
    public void onActivateSSResponse(ActivateSSResponse response) {
    }

    @Override
    public void onDeactivateSSRequest(DeactivateSSRequest request) {
    }

    @Override
    public void onDeactivateSSResponse(DeactivateSSResponse response) {
    }

    @Override
    public void onInterrogateSSRequest(InterrogateSSRequest request) {
    }

    @Override
    public void onInterrogateSSResponse(InterrogateSSResponse response) {
    }

    @Override
    public void onGetPasswordRequest(GetPasswordRequest request) {
    }

    @Override
    public void onGetPasswordResponse(GetPasswordResponse response) {
    }

    @Override
    public void onRegisterPasswordRequest(RegisterPasswordRequest request) {
    }

    @Override
    public void onRegisterPasswordResponse(RegisterPasswordResponse response) {
    }

    @Override
    public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest mapMessage) {
        long dialogID = mapMessage.getMAPDialog().getLocalDialogId();

        if (log.isDebugEnabled()) {
            log.debug(String.format("(DID:%d) evt: process-unstructured-ss-request", dialogID));
        }

        UssMessage msg = jsonMessage(mapMessage, "process-unstructured-ss-request");
        UssMessage msg2 = jsonMessage(mapMessage, "process-unstructured-ss-request");

        if (log.isDebugEnabled()) {
            log.debug(String.format("(DID:%d) message conversation completed", dialogID));
        }

        LocalDialog localDialog = (LocalDialog) mapMessage.getMAPDialog().getUserObject();

        if (log.isDebugEnabled()) {
            log.debug(String.format("(DID:%d) Local dialog exists: %s ", dialogID, (localDialog != null)));
        }

        if (mapMessage.getMSISDNAddressString() != null) {
            localDialog.setMsisdn(mapMessage.getMSISDNAddressString().getAddress());
        }

        String ussdString = "";
        try {
            ussdString = mapMessage.getUSSDString() != null
                    ? mapMessage.getUSSDString().getString(Charset.defaultCharset()) : "";
        } catch (MAPException e) {
        }

        if (log.isInfoEnabled()) {
            log.info(String.format(INFO, localDialog.toString(), "<--- " + mapMessage.getMAPDialog().getTCAPMessageType() + ": process-unstructured-ss-request" + ":" + ussdString));
        }

//        ExecutionContext.EXECUTOR.execute(() -> {
        SccpAddress src = mapMessage.getMAPDialog().getRemoteAddress();
        int pc = src.getSignalingPointCode();
        //To save initial InvokeId
        this.initialPUSSRInvokeId = mapMessage.getInvokeId();

        try {
            if (log.isTraceEnabled()) {
                log.trace(String.format("RX : %d : %s", pc, msg));
                log.trace("InitialInvokeId: " + initialPUSSRInvokeId);
            }

            logOnProcessUnstructuredSSRequest(mapMessage, msg, localDialog, ussdString, src, pc);

            //This message is initiated by provider and must be forwarded to the
            //user using another transport
            UssdRoute route = ussdRouter.find(ussdString(msg));
            if (route == null) {
                throw new IllegalArgumentException("Unknown or undefined key: " + ussdString(msg));
            }

            //store destination into memory
            ((LocalDialog) mapMessage.getMAPDialog().getUserObject()).setRoute(route);

            //select primary destination
            String url = route.nextDestination();
            String url2 = route.failureDestination();

            Channel channel = getGateway().channel(url);
            Channel channel2 = getGateway().channel(url2);

            ExecutionContext ctx = new UssdLocalContext(mapMessage.getMAPDialog(), MapUssdChannel.this, url2, msg2.jsonMessage(), channel2);

            Future future = channel.send(url, msg.jsonMessage(), ctx);
            EXECUTOR.schedule(new TaskInterrupter(future),
                    getGateway().getConfig().getDialogConfig().getInvokeTimeout(),
                    TimeUnit.MILLISECONDS);
        } catch (IllegalArgumentException | UnknownProtocolException e) {
            log.error(String.format(INFO, dialogID, "Could not start dialog"), e);
        }
//        });
    }

    private void logOnProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest mapMessage, UssMessage msg, LocalDialog localDialog, String ussdString, SccpAddress src, int pc) {
        try {
            LOGGER_MAP.info(String.format(MAP_LOGGING_STRING_FORMAT_RECEIVED_EVNT,
                    mapMessage(msg.getTcap().getComponents().getComponent(0)).operationName(),
                    msg.getTcap().getComponents().getComponent(0).getType(),
                    localDialog != null ? localDialog.getMsisdn() : "null",
                    mapMessage.getMAPDialog().getRemoteDialogId(),
                    pc,
                    src.getSubsystemNumber(),
                    src.getGlobalTitle().getDigits(),
                    ussdString
            ));
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            LOGGER_MAP.error(String.format(MAP_LOGGING_STRING_FORMAT_RECEIVED_EVNT,
                    null,
                    null,
                    localDialog != null ? localDialog.getMsisdn() : "null",
                    null,
                    pc,
                    null,
                    null,
                    e.getMessage()
            ));
        }
    }

    @Override
    public void onProcessUnstructuredSSResponse(ProcessUnstructuredSSResponse msg) {
//        ExecutionContext.EXECUTOR.execute(() -> {
        long dialogID = msg.getMAPDialog().getLocalDialogId();

        LocalDialog localDialog = (LocalDialog) msg.getMAPDialog().getUserObject();

        if (log.isInfoEnabled()) {
            log.info(String.format(INFO, localDialog.toString(), "<--- " + msg.getMAPDialog().getTCAPMessageType() + ": process-unstructured-ss-request"));
        }

        SccpAddress src = msg.getMAPDialog().getRemoteAddress();
        int pc = src.getSignalingPointCode();

        try {
            UssMessage m = jsonMessage(msg, "process-unstructured-ss-request");

            if (log.isTraceEnabled()) {
                log.trace(String.format("RX : %d : %s", pc, m));
            }

            logOnProcessUnstructuredSSResponse(msg, localDialog, src, pc, m);

            //This message has arrived from map domain as response for message
            //initiated by user so we need to reply using context
            ExecutionContext context = ((LocalDialog) msg.getMAPDialog().getUserObject()).getContext();
            if (context == null) {
                throw new IllegalStateException("Unknown context");
            }

            if (msg.getUSSDString() == null) {
                context.failed(new IllegalArgumentException());
            } else {
                context.completed(m.jsonMessage());
            }
        } catch (Throwable t) {
            log.error(String.format(INFO, dialogID, "Unexpected error"), t);
        }
//        });
    }

    private void logOnProcessUnstructuredSSResponse(ProcessUnstructuredSSResponse msg, LocalDialog localDialog, SccpAddress src, int pc, UssMessage m) {
        try {
            LOGGER_MAP.info(String.format(MAP_LOGGING_STRING_FORMAT_RECEIVED_EVNT,
                    mapMessage(m.getTcap().getComponents().getComponent(0)).operationName(),
                    m.getTcap().getComponents().getComponent(0).getType(),
                    localDialog != null ? localDialog.getMsisdn() : "null",
                    msg.getMAPDialog().getRemoteDialogId(),
                    pc,
                    src.getSubsystemNumber(),
                    src.getGlobalTitle().getDigits(),
                    msg.getUSSDString()
            ));
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            LOGGER_MAP.error(String.format(MAP_LOGGING_STRING_FORMAT_RECEIVED_EVNT,
                    null,
                    null,
                    localDialog != null ? localDialog.getMsisdn() : "null",
                    null,
                    pc,
                    null,
                    null,
                    e.getMessage()
            ));
        }
    }

    @Override
    public void onUnstructuredSSRequest(UnstructuredSSRequest msg) {
//        ExecutionContext.EXECUTOR.execute(() -> {
        long dialogID = msg.getMAPDialog().getLocalDialogId();

        LocalDialog localDialog = (LocalDialog) msg.getMAPDialog().getUserObject();
        if (msg.getMSISDNAddressString() != null) {
            localDialog.setMsisdn(msg.getMSISDNAddressString().getAddress());
        }

        String ussdString = "";
        try {
            ussdString = msg.getUSSDString() != null
                    ? msg.getUSSDString().getString(Charset.defaultCharset()) : "";
        } catch (MAPException e) {
        }

        if (log.isInfoEnabled()) {
            log.info(String.format(INFO, localDialog.toString(), "<--- " + msg.getMAPDialog().getTCAPMessageType() + ": unstructured-ss-request" + ":" + ussdString));
        }

        SccpAddress src = msg.getMAPDialog().getRemoteAddress();
        int pc = src.getSignalingPointCode();

        try {
            UssMessage m = jsonMessage(msg, "unstructured-ss-request");
            UssMessage m2 = jsonMessage(msg, "unstructured-ss-request");

            if (log.isTraceEnabled()) {
                log.trace(String.format("RX : %d : %s", pc, m));
            }

            //To save invokeId from response in interactive proxy and use in if session will continue
            Consumer<Map.Entry<Long, JsonInteractiveProxyDTO>> updateInvokeId = proxyDTOEntry -> {
                JsonComponent jsonComponent = m.getTcap().getComponents().getComponent(0);
                if (jsonComponent.getType().equals("invoke")) {
                    proxyDTOEntry.getValue().setInvokeId((short) ((JsonInvoke) jsonComponent.getValue()).getInvokeId());
                    proxyIdMatcherMap.put(proxyDTOEntry.getKey(), proxyDTOEntry.getValue());
                }
            };
            proxyIdMatcherMap
                    .entrySet()
                    .stream()
                    .filter(proxyDTOEntry -> proxyDTOEntry.getValue().getDialogId() == dialogID)
                    .findFirst()
                    .ifPresent(updateInvokeId);

            this.logOnUnstructuredSSRequest(localDialog, ussdString, src, pc, m, msg.getMAPDialog(), ussdString);

            LocalDialog dialog = ((LocalDialog) msg.getMAPDialog().getUserObject());
            if (dialog != null && dialog.getContext() != null) {
                dialog.getContext().completed(m.jsonMessage());
                dialog.setContext(null);
                return;
            }
            //Route object might exists
            UssdRoute route = ((LocalDialog) msg.getMAPDialog().getUserObject()).getRoute();
            if (route == null) {
                //if Route object is not defined yet, then we can define it now
                route = ussdRouter.find(ussdString(m));
                if (route == null) {
                    throw new IllegalArgumentException("Unknown or undefined key: " + ussdString(m));
                }

                //store destination into memory
                ((LocalDialog) msg.getMAPDialog().getUserObject()).setRoute(route);
            }

            //select primary destination
            String url = route.nextDestination();
            String url2 = route.failureDestination();

            Channel channel = getGateway().channel(url);
            Channel channel2 = getGateway().channel(url2);

            Future future = channel.send(url, m.jsonMessage(), new UssdLocalContext(msg.getMAPDialog(), MapUssdChannel.this, url2, m2.jsonMessage(), channel2));
            EXECUTOR.schedule(new TaskInterrupter(future),
                    getGateway().getConfig().getDialogConfig().getInvokeTimeout(),
                    TimeUnit.MILLISECONDS);
        } catch (IllegalArgumentException | UnknownProtocolException t) {
            log.error(String.format(INFO, dialogID, "Unexpected error"), t);
        }
//        });
    }


    private void logOnUnstructuredSSRequest(LocalDialog localDialog, String ussdString, SccpAddress src, int pc, UssMessage m, MAPDialogSupplementary mapDialog, String ussdString2) {
        try {
            LOGGER_MAP.info(String.format(MAP_LOGGING_STRING_FORMAT_RECEIVED_EVNT,
                    mapMessage(m.getTcap().getComponents().getComponent(0)).operationName(),
                    m.getTcap().getComponents().getComponent(0).getType(),
                    localDialog != null ? localDialog.getMsisdn() : "null",
                    mapDialog.getRemoteDialogId(),
                    pc,
                    src.getSubsystemNumber(),
                    src.getGlobalTitle().getDigits(),
                    ussdString
            ));
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            LOGGER_MAP.error(String.format(MAP_LOGGING_STRING_FORMAT_RECEIVED_EVNT,
                    null,
                    null,
                    localDialog != null ? localDialog.getMsisdn() : "null",
                    null,
                    pc,
                    null,
                    null,
                    e.getMessage()
            ));
        }
    }

    @Override
    public void onUnstructuredSSResponse(UnstructuredSSResponse msg) {
//        ExecutionContext.EXECUTOR.execute(() -> {
        long dialogID = msg.getMAPDialog().getLocalDialogId();

        LocalDialog localDialog = (LocalDialog) msg.getMAPDialog().getUserObject();

        if (log.isInfoEnabled()) {
            log.info(String.format(INFO, localDialog.toString(), "<---" + msg.getMAPDialog().getTCAPMessageType() + ": unstrcutured-ss-request"));
        }

        SccpAddress src = msg.getMAPDialog().getRemoteAddress();
        int pc = src.getSignalingPointCode();

        try {
            UssMessage m = jsonMessage(msg, "unstructured-ss-request");

            if (log.isTraceEnabled()) {
                log.trace(String.format("RX : %d : %s", pc, m));
            }

            logOnUnstructuredSSResponse(msg, localDialog, src, pc, m);

            LocalDialog dialog = ((LocalDialog) msg.getMAPDialog().getUserObject());
            if (dialog != null && dialog.getContext() != null) {
                if (msg.getUSSDString() == null) {
                    dialog.getContext().failed(new IllegalArgumentException());
                } else {
                    dialog.getContext().completed(m.jsonMessage());
                    dialog.setContext(null);
                }
                return;
            }


            //Route object might exists
            UssdRoute route = ((LocalDialog) msg.getMAPDialog().getUserObject()).getRoute();
            if (route == null) {
                //if Route object is not defined yet, then we can define it now
                route = ussdRouter.find(ussdString(m));
                if (route == null) {
                    throw new IllegalArgumentException("Unknown or undefined key: " + ussdString(m));
                }

                //store destination into memory
                ((LocalDialog) msg.getMAPDialog().getUserObject()).setRoute(route);
            }

            //select primary destination
            String url = route.nextDestination();
            String url2 = route.failureDestination();

            Channel channel = getGateway().channel(url);
            Channel channel2 = getGateway().channel(url2);

            channel.send(url, m.jsonMessage(), new UssdLocalContext(msg.getMAPDialog(), MapUssdChannel.this, url2, m.jsonMessage(), channel2));
        } catch (IllegalArgumentException | UnknownProtocolException t) {
            log.error(String.format(INFO, dialogID, "Unexpected error"), t);
        }
//        });
    }

    private void logOnUnstructuredSSResponse(UnstructuredSSResponse msg, LocalDialog localDialog, SccpAddress src, int pc, UssMessage m) {
        try {
            LOGGER_MAP.info(String.format(MAP_LOGGING_STRING_FORMAT_RECEIVED_EVNT,
                    mapMessage(m.getTcap().getComponents().getComponent(0)).operationName(),
                    m.getTcap().getComponents().getComponent(0).getType(),
                    localDialog != null ? localDialog.getMsisdn() : "null",
                    msg.getMAPDialog().getRemoteDialogId(),
                    pc,
                    src.getSubsystemNumber(),
                    src.getGlobalTitle().getDigits(),
                    msg.getUSSDString()
            ));
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            LOGGER_MAP.error(String.format(MAP_LOGGING_STRING_FORMAT_RECEIVED_EVNT,
                    null,
                    null,
                    localDialog != null ? localDialog.getMsisdn() : "null",
                    null,
                    pc,
                    null,
                    null,
                    e.getMessage()
            ));
        }
    }

    @Override
    public void onUnstructuredSSNotifyRequest(UnstructuredSSNotifyRequest unstrNotifyInd) {
    }

    @Override
    public void onUnstructuredSSNotifyResponse(UnstructuredSSNotifyResponse unstrNotifyInd) {
    }

    @Override
    public void onRejectComponent(MAPDialog mapDialog, Long invokeId, Problem problem, boolean isLocalOriginated) {

    }

    @Override
    public void onInvokeTimeout(MAPDialog mapDialog, Long invokeId) {
        log.warn("(Dialog Id: " + mapDialog.getLocalDialogId() + ") Invoke timeout: " + invokeId);
        if (mapDialog.getUserObject() != null) {
            LocalDialog localDialog = (LocalDialog) mapDialog.getUserObject();
            if (localDialog.getContext() != null) {
                localDialog.getContext().failed(new IllegalStateException("Invoke timeout"));
            }
        }
    }

    @Override
    public void onMAPMessage(MAPMessage mapMessage) {
    }

    /**
     * Converts
     *
     * @param msg
     * @param evt
     * @return
     */
    private UssMessage jsonMessage(SupplementaryMessage msg, String evt) {
        return new UssMessage(msg, ((LocalDialog) msg.getMAPDialog().getUserObject()).getDialog(), evt);
    }

    @Override
    public MAPServiceBase service() {
        return getMapProvider().getMAPServiceSupplementary();
    }

    private class TaskInterrupter implements Runnable {

        private final Future future;

        public TaskInterrupter(Future future) {
            this.future = future;
        }

        @Override
        public void run() {
            try {
                if (future != null && !(future.isDone() || future.isCancelled())) {
                    future.cancel(true);
                }
            } catch (Exception e) {
            }
        }
    }
}
