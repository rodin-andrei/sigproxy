package com.unifun.sigproxy.service.sccp;

import com.unifun.sigproxy.service.rabbit.pojo.SccpRabbitAddress;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;

public interface SccpParametersService {
    SccpAddressImpl createSccpAddress(SccpRabbitAddress address, String stackName);
    SccpRabbitAddress createSccpRabbitAddress(SccpAddress sccpAddress);
}
