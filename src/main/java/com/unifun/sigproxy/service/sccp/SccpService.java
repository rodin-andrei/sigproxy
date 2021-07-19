package com.unifun.sigproxy.service.sccp;

import com.unifun.sigproxy.exception.InitializingException;
import com.unifun.sigproxy.exception.NoConfigurationException;
import com.unifun.sigproxy.models.config.SigtranStack;

public interface SccpService {
    void initialize(SigtranStack sigtranStack) throws NoConfigurationException, InitializingException;
}
