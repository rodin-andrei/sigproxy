package com.unifun.sigproxy.service.m3ua;


import com.unifun.sigproxy.exception.InitializingException;
import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.models.config.m3ua.M3uaAsConfig;
import com.unifun.sigproxy.models.config.m3ua.M3uaAspConfig;
import com.unifun.sigproxy.models.config.m3ua.M3uaRouteConfig;
import org.restcomm.protocols.ss7.m3ua.M3UAManagement;

public interface M3uaService {

    void initialize(SigtranStack sigtranStack) throws InitializingException;

    void initM3uaManagement(SigtranStack sigtranStack) throws InitializingException;

    void addAs(M3uaAsConfig m3uaAsConfig);

    void addAsp(M3uaAspConfig m3uaAspConfig, String sigtranStackName);

    void addRoute(M3uaRouteConfig m3uaRouteConfig);

    void stop(String stackName);

    void stopAsp(M3uaAspConfig m3uaAspConfig, String stackName);

    void removeAsp(M3uaAspConfig m3uaAspConfig, String stackName);

    M3UAManagement getManagement(String stackName);
}
