package com.unifun.sigproxy.model.config;

import lombok.Data;

import java.util.Set;

@Data
public class M3uaConfig {
    private Set<AsConfig> asConfig;
    private Set<AspConfig> aspConfig;
    private Set<RouteConfig> routeConfig;
}
