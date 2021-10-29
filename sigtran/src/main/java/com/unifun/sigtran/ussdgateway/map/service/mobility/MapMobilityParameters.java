/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.map.service.mobility;

import com.unifun.sigtran.ussdgateway.gw.TcapParameters;
import com.unifun.sigtran.ussdgateway.map.dto.JsonRequestedInfo;
import com.unifun.sigtran.ussdgateway.map.dto.JsonSubscriberIdentity;
import com.unifun.sigtran.ussdgateway.map.dto.JsonSubscriberInfo;
import org.restcomm.protocols.ss7.map.api.MAPParameterFactory;
import org.restcomm.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.DomainType;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberInfo;

/**
 * @author okulikov
 */
public class MapMobilityParameters {

    private final MAPParameterFactory factory;
    private final TcapParameters tcapParameters;

    public MapMobilityParameters(MAPParameterFactory factory) {
        this.factory = factory;
        this.tcapParameters = new TcapParameters(factory);
    }

    public JsonSubscriberInfo jsonSubscriberInfo(SubscriberInfo si) {
        final JsonSubscriberInfo jsi = new JsonSubscriberInfo();
        jsi.setImei(si.getIMEI().getIMEI());
        return jsi;
    }

    public SubscriberIdentity subscriberIdentity(JsonSubscriberIdentity si) {
        if (si.getImsi() != null) {
            return factory.createSubscriberIdentity(factory.createIMSI(si.getImsi()));
        } else if (si.getMsisdn() != null) {
            return factory.createSubscriberIdentity(tcapParameters.isdnAddressString(si.getMsisdn()));
        } else {
            return null;
        }
    }

    public RequestedInfo requestedInfo(JsonRequestedInfo ri) {
        return factory.createRequestedInfo(false, false, null, false, null, true, false, false);
    }

    public RequestedInfo allPossibleInfo() {
        return factory.createRequestedInfo(true, true, null, true, DomainType.csDomain, true, true, true);
    }

    public SubscriberInfo subscriberInfo(JsonSubscriberInfo si) {
        return factory.createSubscriberInfo(null, null, null, null, null, factory.createIMEI(si.getImei()), null, null, null);
    }
}
