package com.unifun.sigproxy.repository.m3ua;

import com.unifun.sigproxy.models.config.m3ua.M3uaAsConfig;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author arodin
 */
public interface AsRepository extends JpaRepository<M3uaAsConfig, Long> {
}
