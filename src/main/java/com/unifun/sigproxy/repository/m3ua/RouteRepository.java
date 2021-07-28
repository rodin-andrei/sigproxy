package com.unifun.sigproxy.repository.m3ua;

import com.unifun.sigproxy.models.config.m3ua.M3uaRouteConfig;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author arodin
 */
public interface RouteRepository extends JpaRepository<M3uaRouteConfig, Long> {
}
