package com.unifun.sigproxy.service;


import com.unifun.sigproxy.exception.InitializingException;
import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.models.config.m3ua.AspConfig;

public interface M3uaService {

    void initialize(SigtranStack sigtranStack) throws InitializingException;

    void stop(String stackName);

    void stopAsp(AspConfig aspConfig, String stackName);

    void removeAsp(AspConfig aspConfig, String stackName);
}
