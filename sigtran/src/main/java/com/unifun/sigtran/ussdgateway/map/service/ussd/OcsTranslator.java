/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.map.service.ussd;

import com.unifun.sigtran.ussdgateway.gw.MapBaseChannel;
import com.unifun.sigtran.ussdgateway.gw.el.Expression;
import com.unifun.sigtran.ussdgateway.map.dto.*;
import com.unifun.sigtran.ussdgateway.util.URL;

/**
 * @author okulikov
 */
public class OcsTranslator {
    public JsonMessage translate(URL url, JsonMessage origin, JsonMessage pattern) {
        origin.getSccp().setCalledPartyAddress(pattern.getSccp().getCalledPartyAddress().clone());
        origin.getSccp().setCallingPartyAddress(pattern.getSccp().getCallingPartyAddress().clone());

        JsonTcap tcap = origin.getTcap();
        JsonTcapDialog dialog = tcap.getDialog();

        JsonMapOperation op = operation(tcap);
        String ussdString = url.params().get("ussdString");

        //If it's interactive proxy --> to continue it
        JsonInteractiveProxyDTO proxyDTO = MapBaseChannel.proxyIdMatcherMap.getOrDefault(dialog.getDialogId(),
                new JsonInteractiveProxyDTO(-1L));
        long dialogId = proxyDTO.getDialogId();
        if (dialogId != -1L) {
            if (tcap.getComponents().getComponent(0).getValue() instanceof JsonReturnResultLast) {
                ((JsonReturnResultLast) tcap.getComponents().getComponent(0).getValue()).setInvokeId(proxyDTO.getInvokeId());
            } else if (tcap.getComponents().getComponent(0).getValue() instanceof JsonInvoke) {
                dialogId = -1;
            } else if (tcap.getComponents().getComponent(0).getValue() instanceof JsonReturnError) {
                dialogId = -1;
            }
        }
        dialog.setDialogId(dialogId);

        if (ussdString != null && dialogId == -1L) {
            ussdString = ussdString.replace("${ussdString}", op.getUssdString());
            Expression e = new Expression(ussdString);
            op.setUssdString(e.exec(op.getUssdString()));
        }

        return origin;
    }

    private JsonSccpAddress cloneAddress(JsonSccpAddress address) {
        JsonSccpAddress res = new JsonSccpAddress();
        res.setPc(address.getPc());
        res.setSsn(address.getSsn());
        res.setGtIndicator(address.getGtIndicator());
        res.setRoutingIndicator(address.getRoutingIndicator());

        if (address.getGlobalTitle() != null) {
            JsonGlobalTitle gt = new JsonGlobalTitle();
            gt.setDigits(address.getGlobalTitle().getDigits());
            gt.setEncodingSchema(address.getGlobalTitle().getEncodingSchema());
            gt.setNatureOfAddressIndicator(address.getGlobalTitle().getNatureOfAddressIndicator());
            gt.setNumberingPlan(address.getGlobalTitle().getNumberingPlan());
            res.setGlobalTitle(gt);
        }

        return res;
    }

    private JsonMapOperation operation(JsonTcap tcap) {
        JsonComponent component1 = tcap.getComponents().getComponent(0);
        switch (component1.getType()) {
            case "invoke":
                JsonInvoke invoke = (JsonInvoke) component1.getValue();
                JsonMap map = (JsonMap) invoke.component();
                return (JsonMapOperation) map.operation();
            case "returnResultLast":
                JsonReturnResultLast returnResultLast = (JsonReturnResultLast) component1.getValue();
                return (JsonMapOperation) (((JsonMap) returnResultLast.component()).operation());
        }
        return null;
    }

}
