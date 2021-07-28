package com.unifun.sigproxy.repository.m3ua;

import com.unifun.sigproxy.models.config.m3ua.M3uaAspConfig;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author arodin
 */
public interface AspRepository extends JpaRepository<M3uaAspConfig, Long> {
}
