package com.unifun.sigproxy.service.m3ua;

import com.unifun.sigproxy.models.config.m3ua.M3uaAsConfig;
import com.unifun.sigproxy.models.config.m3ua.M3uaAspConfig;
import com.unifun.sigproxy.models.config.m3ua.M3uaRouteConfig;
import com.unifun.sigproxy.models.config.m3ua.M3uaStackSettingsConfig;

import java.util.Set;

public interface M3uaConfigService {
    Set<M3uaAsConfig> getM3uaAsConfig(Long sigtranStackId);

    M3uaAsConfig addM3uaAsConfig(M3uaAsConfig m3uaAsConfig);

    M3uaStackSettingsConfig addM3uaStackSettingsConfig(M3uaStackSettingsConfig m3uaStackSettingsConfig);

    Set<M3uaAspConfig> getM3uaAspConfig(Long sigtranStackId);

    Set<M3uaRouteConfig> getM3uaRouteConfig(Long m3uaAsId);

    M3uaStackSettingsConfig getM3uaStackSettingsConfig(Long sigtranStackId);
}
