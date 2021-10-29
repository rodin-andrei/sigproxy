/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.gw;

import com.unifun.sigtran.ussdgateway.map.dto.JsonAddressString;
import org.restcomm.protocols.ss7.map.api.MAPParameterFactory;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.AddressString;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.primitives.AddressStringImpl;

/**
 * @author okulikov
 */
public class TcapParameters {
    private final MAPParameterFactory factory;

    public TcapParameters(MAPParameterFactory factory) {
        this.factory = factory;
    }

    /**
     * Converts SS7 address string into json format.
     *
     * @param address
     * @return
     */
    public JsonAddressString jsonAddressString(AddressString address) {
        if (address == null) {
            return null;
        }

        JsonAddressString value = new JsonAddressString();
        value.setExtension(((AddressStringImpl) address).isExtension());
        value.setNumberingPlan(address.getNumberingPlan().name());
        value.setNatureOfAddress(address.getAddressNature().name());
        value.setAddress(address.getAddress());

        return value;
    }

    /**
     * Constructs ISDNAddressString from related Json object.
     *
     * @param address
     * @return
     */
    public ISDNAddressString isdnAddressString(JsonAddressString address) {
        if (address == null) {
            return null;
        }
        return factory.createISDNAddressString(
                AddressNature.valueOf(address.getNatureOfAddress()),
                NumberingPlan.valueOf(address.getNumberingPlan()),
                address.getAddress());
    }

    public AddressString addressString(JsonAddressString address) {
        if (address == null) {
            return null;
        }
        return factory.createAddressString(
                AddressNature.valueOf(address.getNatureOfAddress()),
                NumberingPlan.valueOf(address.getNumberingPlan()),
                address.getAddress());
    }
}
