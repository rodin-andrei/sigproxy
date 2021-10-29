package com.unifun.sigproxy.sigtran.repository.sccp;

import com.unifun.sigproxy.sigtran.models.config.sccp.SccpSettingsConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SccpSettingsConfigRepository extends JpaRepository<SccpSettingsConfig, Long> {
}
