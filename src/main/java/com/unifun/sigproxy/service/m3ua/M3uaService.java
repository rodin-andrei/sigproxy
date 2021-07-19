package com.unifun.sigproxy.service.m3ua;


import com.unifun.sigproxy.exception.InitializingException;
import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.models.config.m3ua.AspConfig;
import org.restcomm.protocols.ss7.m3ua.M3UAManagement;

public interface M3uaService {

    void initialize(SigtranStack sigtranStack) throws InitializingException;

    void stop(String stackName);

    void stopAsp(AspConfig aspConfig, String stackName);

    void removeAsp(AspConfig aspConfig, String stackName);

    M3UAManagement getManagement(String stackName);
}
