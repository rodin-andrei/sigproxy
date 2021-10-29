package com.unifun.sigproxy.sigtran.repository.m3ua;

import com.unifun.sigproxy.sigtran.models.config.m3ua.M3uaAspConfig;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author arodin
 */
public interface AspRepository extends JpaRepository<M3uaAspConfig, Long> {
}