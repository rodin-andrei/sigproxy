package com.unifun.sigproxy.sigtran.repository.m3ua;

import com.unifun.sigproxy.sigtran.models.config.m3ua.M3uaAsConfig;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author arodin
 */
public interface AsRepository extends JpaRepository<M3uaAsConfig, Long> {
}
