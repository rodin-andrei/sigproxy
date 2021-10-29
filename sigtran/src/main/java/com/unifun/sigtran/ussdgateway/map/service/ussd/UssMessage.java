/**
 *
 */
package com.unifun.sigtran.ussdgateway.map.service.ussd;

import com.unifun.sigtran.ussdgateway.map.dto.*;
import org.restcomm.protocols.ss7.map.api.primitives.AddressString;
import org.restcomm.protocols.ss7.map.api.primitives.USSDString;
import org.restcomm.protocols.ss7.map.api.service.supplementary.*;
import org.restcomm.protocols.ss7.sccp.parameter.GlobalTitle0001;
import org.restcomm.protocols.ss7.sccp.parameter.GlobalTitle0011;
import org.restcomm.protocols.ss7.sccp.parameter.GlobalTitle0100;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

/**
 * @author rbabin
 *
 */
public class UssMessage implements Serializable {

    private JsonMessage jsonMessage;
    private long invokeId;

    public UssMessage() {
    }

    /**
     * Creates instance of this message using the json format.
     *
     * @param jsonMessage the message in json format.
     */
    public UssMessage(JsonMessage jsonMessage) {
        this.jsonMessage = jsonMessage;
    }

    /**
     * Creates instance of this message 
     * @param mapMessage
     * @param tcapDialog
     * @param operationName
     */
    public UssMessage(SupplementaryMessage mapMessage, JsonTcapDialog tcapDialog, String operationName) {
        this.invokeId = mapMessage.getInvokeId();

        final JsonSccp sccp = new JsonSccp();
        if (mapMessage.getMAPDialog().getLocalAddress() != null) {
            sccp.setCalledPartyAddress(valueOf(mapMessage.getMAPDialog().getLocalAddress()));
        }

        if (mapMessage.getMAPDialog().getRemoteAddress() != null) {
            sccp.setCallingPartyAddress(valueOf(mapMessage.getMAPDialog().getRemoteAddress()));
        }


        final JsonMapOperation operation = new JsonMapOperation();
        final JsonComponents components = new JsonComponents();

        switch (mapMessage.getMessageType()) {
            case processUnstructuredSSRequest_Request:
                ProcessUnstructuredSSRequest pussr = (ProcessUnstructuredSSRequest) mapMessage;

                JsonDataCodingScheme codingScheme = new JsonDataCodingScheme();
                codingScheme.setLanguage(pussr.getDataCodingScheme().getCharacterSet().name());
                codingScheme.setCodingGroup(pussr.getDataCodingScheme().getDataCodingGroup().name());
                if (pussr.getDataCodingScheme().getNationalLanguageShiftTable() != null) {
                    codingScheme.setNationalLanguage(pussr.getDataCodingScheme().getNationalLanguageShiftTable().name());
                }

                if (pussr.getDataCodingScheme().getMessageClass() != null) {
                    codingScheme.setMessageClass(pussr.getDataCodingScheme().getMessageClass().name());
                }

                operation.setMsisdn(valueOf(pussr.getMSISDNAddressString()));
                operation.setCodingScheme(codingScheme);
                operation.setUssdString(valueOf(pussr.getUSSDString()));

                JsonMap jsonMap = new JsonMap(operationName, operation);
                JsonInvoke invoke = new JsonInvoke(mapMessage.getInvokeId(), jsonMap);

                JsonComponent component = new JsonComponent();
                component.setType("invoke");
                component.setValue(invoke);

                components.addComponent(component);
                break;
            case processUnstructuredSSRequest_Response:
                ProcessUnstructuredSSResponse pussrr = (ProcessUnstructuredSSResponse) mapMessage;

                codingScheme = new JsonDataCodingScheme();
                codingScheme.setLanguage(pussrr.getDataCodingScheme().getCharacterSet().name());
                codingScheme.setCodingGroup(pussrr.getDataCodingScheme().getDataCodingGroup().name());

                if (pussrr.getDataCodingScheme().getNationalLanguageShiftTable() != null) {
                    codingScheme.setNationalLanguage(pussrr.getDataCodingScheme().getNationalLanguageShiftTable().name());
                }

                if (pussrr.getDataCodingScheme().getMessageClass() != null) {
                    codingScheme.setMessageClass(pussrr.getDataCodingScheme().getMessageClass().name());
                }

                operation.setCodingScheme(codingScheme);
                operation.setUssdString(valueOf(pussrr.getUSSDString()));

                jsonMap = new JsonMap(operationName, operation);
                JsonReturnResultLast returnResultLast = new JsonReturnResultLast(mapMessage.getInvokeId(), jsonMap);

                component = new JsonComponent();
                component.setType("returnResultLast");
                component.setValue(returnResultLast);

                components.addComponent(component);
                break;
            case unstructuredSSRequest_Request:
                UnstructuredSSRequest ussr = (UnstructuredSSRequest) mapMessage;

                codingScheme = new JsonDataCodingScheme();
                codingScheme.setLanguage(ussr.getDataCodingScheme().getCharacterSet().name());
                codingScheme.setCodingGroup(ussr.getDataCodingScheme().getDataCodingGroup().name());

                if (ussr.getDataCodingScheme().getNationalLanguageShiftTable() != null) {
                    codingScheme.setNationalLanguage(ussr.getDataCodingScheme().getNationalLanguageShiftTable().name());
                }

                if (ussr.getDataCodingScheme().getMessageClass() != null) {
                    codingScheme.setMessageClass(ussr.getDataCodingScheme().getMessageClass().name());
                }

                operation.setMsisdn(valueOf(ussr.getMSISDNAddressString()));
                operation.setCodingScheme(codingScheme);
                operation.setUssdString(valueOf(ussr.getUSSDString()));

                jsonMap = new JsonMap(operationName, operation);
                invoke = new JsonInvoke(mapMessage.getInvokeId(), jsonMap);

                component = new JsonComponent();
                component.setType("invoke");
                component.setValue(invoke);

                components.addComponent(component);
                break;
            case unstructuredSSRequest_Response:
                UnstructuredSSResponse ussrr = (UnstructuredSSResponse) mapMessage;

                codingScheme = new JsonDataCodingScheme();
                codingScheme.setLanguage(ussrr.getDataCodingScheme().getCharacterSet().name());
                codingScheme.setCodingGroup(ussrr.getDataCodingScheme().getDataCodingGroup().name());
                if (ussrr.getDataCodingScheme().getNationalLanguageShiftTable() != null) {
                    codingScheme.setNationalLanguage(ussrr.getDataCodingScheme().getNationalLanguageShiftTable().name());
                }

                if (ussrr.getDataCodingScheme().getMessageClass() != null) {
                    codingScheme.setMessageClass(ussrr.getDataCodingScheme().getMessageClass().name());
                }

                operation.setCodingScheme(codingScheme);
                operation.setUssdString(valueOf(ussrr.getUSSDString()));

                jsonMap = new JsonMap(operationName, operation);
                returnResultLast = new JsonReturnResultLast(mapMessage.getInvokeId(), jsonMap);

                component = new JsonComponent();
                component.setType("returnResultLast");
                component.setValue(returnResultLast);

                components.addComponent(component);
                break;
            case unstructuredSSNotify_Request:
                UnstructuredSSNotifyRequest ussn = (UnstructuredSSNotifyRequest) mapMessage;

                codingScheme = new JsonDataCodingScheme();
                codingScheme.setLanguage(ussn.getDataCodingScheme().getCharacterSet().name());
                codingScheme.setCodingGroup(ussn.getDataCodingScheme().getDataCodingGroup().name());

                if (ussn.getDataCodingScheme().getNationalLanguageShiftTable() != null) {
                    codingScheme.setNationalLanguage(ussn.getDataCodingScheme().getNationalLanguageShiftTable().name());
                }

                if (ussn.getDataCodingScheme().getMessageClass() != null) {
                    codingScheme.setMessageClass(ussn.getDataCodingScheme().getMessageClass().name());
                }

                operation.setMsisdn(valueOf(ussn.getMSISDNAddressString()));
                operation.setCodingScheme(codingScheme);
                operation.setUssdString(valueOf(ussn.getUSSDString()));

                jsonMap = new JsonMap(operationName, operation);
                invoke = new JsonInvoke(mapMessage.getInvokeId(), jsonMap);

                component = new JsonComponent();
                component.setType("invoke");
                component.setValue(invoke);

                components.addComponent(component);
                break;
            case unstructuredSSNotify_Response:
                UnstructuredSSNotifyResponse ussnr = (UnstructuredSSNotifyResponse) mapMessage;

                jsonMap = new JsonMap(operationName, operation);
                returnResultLast = new JsonReturnResultLast(mapMessage.getInvokeId(), jsonMap);

                component = new JsonComponent();
                component.setType("returnResultLast");
                component.setValue(returnResultLast);

                components.addComponent(component);
                break;


        }

        final JsonTcapDialog td = new JsonTcapDialog();
        //if dialogue portion is not known yet
        //then we will build new one following known data
        if (tcapDialog == null) {
            final AddressString origReference = mapMessage.getMAPDialog().getReceivedOrigReference();
            final AddressString destReference = mapMessage.getMAPDialog().getReceivedDestReference();

            td.setOriginationReference(valueOf(origReference));
            td.setDestinationReference(valueOf(destReference));
        } else {
            td.setApplicationContextName(tcapDialog.getApplicationContextName());
            td.setDestinationReference(tcapDialog.getDestinationReference());
            td.setDialogId(tcapDialog.getDialogId());
            td.setMsisdn(tcapDialog.getMsisdn());
            td.setOriginationReference(tcapDialog.getOriginationReference());
            td.setVersion(tcapDialog.getVersion());
            td.setVlrAddress(tcapDialog.getVlrAddress());
        }

        //always check the dialog id
        td.setDialogId(mapMessage.getMAPDialog().getLocalDialogId());

        final JsonTcap tcap = new JsonTcap();
        tcap.setDialog(td);
        tcap.setType(mapMessage.getMAPDialog().getTCAPMessageType().name());
        tcap.setComponents(components);

        jsonMessage = new JsonMessage();
        jsonMessage.setSccp(sccp);
        jsonMessage.setTcap(tcap);
    }

    public JsonMessage jsonMessage() {
        return jsonMessage;
    }

    public long invokeId() {
        return invokeId;
    }

    /**
     * Converts SS7 address string into json format.
     *
     * @param address
     * @return
     */
    private JsonAddressString valueOf(AddressString address) {
        if (address == null) {
            return null;
        }

        JsonAddressString value = new JsonAddressString();
        value.setNumberingPlan(address.getNumberingPlan().name());
        value.setNatureOfAddress(address.getAddressNature().name());
        value.setAddress(address.getAddress());

        return value;
    }

    private JsonSccpAddress valueOf(SccpAddress address) {
        final JsonGlobalTitle gt = new JsonGlobalTitle();
        JsonSccpAddress value = new JsonSccpAddress();
        if (address.getGlobalTitle() != null) {
            gt.setDigits(address.getGlobalTitle().getDigits());

            switch (address.getGlobalTitle().getGlobalTitleIndicator()) {
                case GLOBAL_TITLE_INCLUDES_NATURE_OF_ADDRESS_INDICATOR_ONLY:
                    gt.setNatureOfAddressIndicator(((GlobalTitle0001) address.getGlobalTitle()).getNatureOfAddress().name());
                    break;
                case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_ONLY:
                    break;
                case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_AND_ENCODING_SCHEME:
                    gt.setEncodingSchema(((GlobalTitle0011) address.getGlobalTitle()).getEncodingScheme().getType().name());
                    gt.setNumberingPlan(((GlobalTitle0011) address.getGlobalTitle()).getNumberingPlan().name());
                    break;
                case GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_ENCODING_SCHEME_AND_NATURE_OF_ADDRESS:
                    gt.setNatureOfAddressIndicator(((GlobalTitle0100) address.getGlobalTitle()).getNatureOfAddress().name());
                    gt.setEncodingSchema(((GlobalTitle0100) address.getGlobalTitle()).getEncodingScheme().getType().name());
                    gt.setNumberingPlan(((GlobalTitle0100) address.getGlobalTitle()).getNumberingPlan().name());
                    break;
            }
            value.setGlobalTitle(gt);
            value.setGtIndicator(address.getGlobalTitle().getGlobalTitleIndicator().name());
        }

        if (address.getAddressIndicator() != null) {
            value.setRoutingIndicator(address.getAddressIndicator().getRoutingIndicator().name());
        }
        value.setPc(address.getSignalingPointCode());
        value.setSsn(address.getSubsystemNumber());

        return value;
    }

    /**
     * Converts USSD string value into UTF-8 string.
     *
     * @param ussdString
     * @return
     */
    private String valueOf(USSDString ussdString) {
        if (ussdString == null) {
            return null;
        }

        try {
            String text = ussdString.getString(StandardCharsets.UTF_8).trim();
            text = text.replaceAll("\n", "--newline--");
            text = text.replaceAll("\r", "--return--");
            text = text.replace("\"", "'");
            return text;
        } catch (Exception e) {
            return "null";
        }
    }

    public JsonSccp getSccp() {
        return jsonMessage.getSccp();
    }

    public JsonTcap getTcap() {
        return jsonMessage.getTcap();
    }

    @Override
    public String toString() {
        return this.jsonMessage.toString();
    }

}
