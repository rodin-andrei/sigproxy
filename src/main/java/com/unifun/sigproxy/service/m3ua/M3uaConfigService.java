package com.unifun.sigproxy.service.m3ua;

import com.unifun.sigproxy.models.config.m3ua.M3uaAsConfig;
import com.unifun.sigproxy.models.config.m3ua.M3uaAspConfig;
import com.unifun.sigproxy.models.config.m3ua.M3uaRouteConfig;
import com.unifun.sigproxy.models.config.m3ua.M3uaStackSettingsConfig;

import java.util.Set;

public interface M3uaConfigService {
    Set<M3uaAsConfig> getM3uaAsConfigByStackId(Long sigtranStackId);

    Set<M3uaAspConfig> getM3uaAspConfigByAsId(Long sigtranStackId);

    Set<M3uaRouteConfig> getM3uaRouteConfigByM3UaAsId(Long m3uaAsId);

    M3uaStackSettingsConfig getM3uaStackSettingsConfigByStackId(Long sigtranStackId);

    M3uaAsConfig getM3uaAsConfigById(Long m3uaAsId);

    M3uaAspConfig getM3uaAspConfigById(Long m3uaAspId);

    M3uaRouteConfig getM3uaRouteConfigById(Long m3uaRouteId);

    M3uaStackSettingsConfig get3uaStackSettingsConfigById(Long m3uaStackSettingsId);

    M3uaAsConfig addM3uaAsConfig(M3uaAsConfig m3uaAsConfig);

    M3uaStackSettingsConfig addM3uaStackSettingsConfig(M3uaStackSettingsConfig m3uaStackSettingsConfig);

    M3uaAspConfig addM3uaAspConfig(M3uaAspConfig m3uaAspConfig);

    M3uaRouteConfig addM3uaRouteConfig(M3uaRouteConfig m3uaRouteConfig);

    void removeM3uaAsConfig(Long m3uaAsId);

    void removeM3uaStackSettingsConfig(Long m3uaStackSettingsId);

    void removeM3uaAspConfig(Long m3uaAspId);

    void removeM3uaRouteConfig(Long m3uaRouteId);
}