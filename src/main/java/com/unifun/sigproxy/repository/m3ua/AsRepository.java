package com.unifun.sigproxy.repository.m3ua;

import com.unifun.sigproxy.models.config.m3ua.AsConfig;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author arodin
 */
public interface AsRepository extends JpaRepository<AsConfig, Long> {
}
