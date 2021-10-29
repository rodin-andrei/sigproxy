/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.map.service.callhandling;

import com.unifun.sigtran.ussdgateway.gw.TcapParameters;
import com.unifun.sigtran.ussdgateway.map.dto.JsonCamelRoutingInfo;
import com.unifun.sigtran.ussdgateway.map.dto.JsonExtendedRoutingInfo;
import com.unifun.sigtran.ussdgateway.map.dto.JsonRoutingInfo;
import org.restcomm.protocols.ss7.map.api.MAPParameterFactory;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CamelRoutingInfo;
import org.restcomm.protocols.ss7.map.api.service.callhandling.ExtendedRoutingInfo;
import org.restcomm.protocols.ss7.map.api.service.callhandling.RoutingInfo;

/**
 * @author okulikov
 */
public class CallHandlingParameters {
    private final TcapParameters tcapParameters;

    public CallHandlingParameters(MAPParameterFactory factory) {
        this.tcapParameters = new TcapParameters(factory);
    }

    public JsonExtendedRoutingInfo jsonExtendedRoutingInfo(ExtendedRoutingInfo ri) {
        JsonExtendedRoutingInfo jri = new JsonExtendedRoutingInfo();
        if (ri.getRoutingInfo() != null) {
            jri.setRoutingInfo(jsonRoutingInfo(ri.getRoutingInfo()));
        }
        if (ri.getCamelRoutingInfo() != null) {
            jri.setCamelRoutingInfo(jsonCamelRoutingInfo(ri.getCamelRoutingInfo()));
        }
        return jri;
    }

    public JsonRoutingInfo jsonRoutingInfo(RoutingInfo ri) {
        JsonRoutingInfo jri = new JsonRoutingInfo();
        jri.setRoamingNumber(tcapParameters.jsonAddressString(ri.getRoamingNumber()));
        return jri;
    }

    public JsonCamelRoutingInfo jsonCamelRoutingInfo(CamelRoutingInfo ri) {
        JsonCamelRoutingInfo jri = new JsonCamelRoutingInfo();
        return jri;
    }

}
