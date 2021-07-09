package com.unifun.sigproxy.repository.m3ua;

import com.unifun.sigproxy.models.config.m3ua.AspConfig;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author arodin
 */
public interface AspRepository extends JpaRepository<AspConfig, Long> {
}
