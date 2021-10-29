package com.unifun.sigproxy.sigtran.service.map;

import com.unifun.sigproxy.sigtran.exception.InitializingException;
import com.unifun.sigproxy.sigtran.exception.NoConfigurationException;
import com.unifun.sigproxy.sigtran.models.config.SigtranStack;
import org.restcomm.protocols.ss7.map.MAPStackImpl;

public interface MapService {
    void initialize(SigtranStack sigtranStack) throws NoConfigurationException, InitializingException;

    void test(String stackName, int addrA, int addrB);

    MAPStackImpl getMapStack(String stackName);
}
