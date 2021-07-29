package com.unifun.sigproxy.service.sccp;

import com.unifun.sigproxy.exception.InitializingException;
import com.unifun.sigproxy.exception.NoConfigurationException;
import com.unifun.sigproxy.models.config.SigtranStack;
import org.restcomm.protocols.ss7.sccp.SccpProvider;

public interface SccpService {
    void initialize(SigtranStack sigtranStack) throws NoConfigurationException, InitializingException;

    void test(String stackName, int addressA, int addressB);

    SccpProvider getSccpProvider(String stackName);
}
