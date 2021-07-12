package com.unifun.sigproxy.service;

import com.unifun.sigproxy.controller.dto.M3uaAsDTO;
import com.unifun.sigproxy.exception.InitializingException;
import com.unifun.sigproxy.exception.NoConfigurationException;
import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.models.config.m3ua.AspConfig;
import com.unifun.sigproxy.models.config.m3ua.RouteConfig;

import java.util.Set;

public interface M3uaService {
    void initialize(SigtranStack sigtranStack) throws NoConfigurationException, InitializingException;

    void stop();

    void stop(String stackName);

    void stopAsp(AspConfig aspConfig);

    void removeAsp(AspConfig aspConfig);

    void removeRoute(RouteConfig routeConfig);

    Set<M3uaAsDTO> getM3uaStatuses();

}
