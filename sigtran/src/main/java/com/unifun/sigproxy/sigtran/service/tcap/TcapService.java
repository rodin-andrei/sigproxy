package com.unifun.sigproxy.sigtran.service.tcap;

import com.unifun.sigproxy.sigtran.exception.InitializingException;
import com.unifun.sigproxy.sigtran.exception.NoConfigurationException;
import com.unifun.sigproxy.sigtran.models.config.SigtranStack;
import org.restcomm.protocols.ss7.tcap.api.TCAPStack;

public interface TcapService {
    void initialize(SigtranStack sigtranStack) throws NoConfigurationException, InitializingException;

    TCAPStack getTcapStack(String stackName);
}
