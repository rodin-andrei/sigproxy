package com.unifun.sigproxy.service.map;

import com.unifun.sigproxy.exception.InitializingException;
import com.unifun.sigproxy.exception.NoConfigurationException;
import com.unifun.sigproxy.models.config.SigtranStack;

public interface MapService {
    void initialize(SigtranStack sigtranStack) throws NoConfigurationException, InitializingException;

    void test(String stackName, int addrA, int addrB);
}
