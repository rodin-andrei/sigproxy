package com.unifun.sigproxy.service;

import com.unifun.sigproxy.exception.InitializingException;
import com.unifun.sigproxy.exception.NoConfigurationException;

public interface SctpService {
    void initialize() throws NoConfigurationException, InitializingException;
}
