package com.unifun.sigproxy.repository.sccp;

import com.unifun.sigproxy.models.config.sccp.SccpSettingsConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SccpSettingsConfigRepository extends JpaRepository<SccpSettingsConfig, Long> {
}
