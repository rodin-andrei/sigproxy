package com.unifun.sigproxy.service;

import com.unifun.sigproxy.exception.NoConfigurationException;
import com.unifun.sigproxy.model.config.M3uaConfig;

public interface M3uaConfigService {

    M3uaConfig getM3uaConfig() throws NoConfigurationException;
}
