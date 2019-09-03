package com.unifun.sigproxy.service;

import com.unifun.sigproxy.exception.InitializingException;
import com.unifun.sigproxy.exception.NoConfigurationException;
import com.unifun.sigproxy.model.config.M3uaConfig;

public interface M3uaService {
    void initialize(M3uaConfig m3uaConfig) throws NoConfigurationException, InitializingException;

    void stop();
}
