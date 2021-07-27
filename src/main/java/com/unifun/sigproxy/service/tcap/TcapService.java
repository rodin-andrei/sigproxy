package com.unifun.sigproxy.service.tcap;

import com.unifun.sigproxy.exception.InitializingException;
import com.unifun.sigproxy.exception.NoConfigurationException;
import com.unifun.sigproxy.models.config.SigtranStack;

public interface TcapService {
    void initialize(SigtranStack sigtranStack) throws NoConfigurationException, InitializingException;
}
