/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.gw;

import com.unifun.sigtran.ussdgateway.map.dto.JsonGlobalTitle;
import com.unifun.sigtran.ussdgateway.map.dto.JsonSccpAddress;
import org.restcomm.protocols.ss7.indicator.NatureOfAddress;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.restcomm.protocols.ss7.sccp.parameter.*;

/**
 * @author okulikov
 */
public class SccpParameters {
    private final ParameterFactory factory;

    public SccpParameters(ParameterFactory sccpParameterFactory) {
        this.factory = sccpParameterFactory;
    }

    public SccpAddressImpl createSccpAddress(JsonSccpAddress address) {
        int ssn = address.getSsn() != null ? address.getSsn() : -1;
        int pc = address.getPc() != null ? address.getPc() : -1;

        String gti = address.getGtIndicator();

        GlobalTitle gt = null;
        switch (gti) {
            case "0001":
            case "GLOBAL_TITLE_INCLUDES_NATURE_OF_ADDRESS_INDICATOR_ONLY":
                gt = factory.createGlobalTitle(digits(address), na(address));
                break;
            case "0010":
            case "GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_ONLY":
                gt = factory.createGlobalTitle(digits(address), na(address));
                break;
            case "0011":
            case "GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_AND_ENCODING_SCHEME":
                gt = factory.createGlobalTitle(digits(address), na(address));
                break;
            case "0100":
            case "GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_ENCODING_SCHEME_AND_NATURE_OF_ADDRESS":
                gt = factory.createGlobalTitle(digits(address), 0, np(address), null, na(address));
                break;
        }

        return new SccpAddressImpl(
                RoutingIndicator.valueOf(address.getRoutingIndicator()),
                gt, pc, ssn);
    }

    public JsonSccpAddress toJsonFormat(SccpAddress address) {
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
     * Extracts digits from SCCP address in JSON format.
     *
     * @param address
     * @return
     */
    private String digits(JsonSccpAddress address) {
        return address.getGlobalTitle().getDigits();
    }

    /**
     * Constructs numbering plan from corresponding Json object.
     *
     * @param address
     * @return
     */
    private org.restcomm.protocols.ss7.indicator.NumberingPlan np(JsonSccpAddress address) {
        return org.restcomm.protocols.ss7.indicator.NumberingPlan.valueOf(address.getGlobalTitle().getNumberingPlan());
    }

    /**
     * Constructs nature of address indicator from corresponding Json object.
     *
     * @param address
     * @return
     */
    private NatureOfAddress na(JsonSccpAddress address) {
        return NatureOfAddress.valueOf(address.getGlobalTitle().getNatureOfAddressIndicator());
    }

    /**
     * Constructs translation type from corresponding Json object.
     *
     * @param address
     * @return
     */
    private int tt(JsonSccpAddress address) {
        return 0;
    }

}
