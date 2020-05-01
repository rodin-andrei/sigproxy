package com.unifun.sigproxy.service;

import com.unifun.sigproxy.exception.InitializingException;
import com.unifun.sigproxy.exception.NoConfigurationException;
import com.unifun.sigproxy.json.model.config.m3ua.AspConfig;
import com.unifun.sigproxy.json.model.config.m3ua.M3uaConfig;
import com.unifun.sigproxy.json.model.config.m3ua.RouteConfig;
import com.unifun.sigproxy.json.model.dto.M3uaAsDTO;

import java.util.Set;

public interface M3uaService {
    void initialize(M3uaConfig m3uaConfig) throws NoConfigurationException, InitializingException;

    void stop();

    void stopAsp(AspConfig aspConfig);

    void removeAsp(AspConfig aspConfig);

    void removeRoute(RouteConfig routeConfig);

    Set<M3uaAsDTO> getM3uaStatuses();

}
