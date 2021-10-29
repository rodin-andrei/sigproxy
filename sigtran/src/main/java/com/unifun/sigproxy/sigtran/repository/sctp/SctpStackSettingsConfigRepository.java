package com.unifun.sigproxy.sigtran.repository.sctp;

import com.unifun.sigproxy.sigtran.models.config.sctp.SctpStackSettingsConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SctpStackSettingsConfigRepository extends JpaRepository<SctpStackSettingsConfig, Long> {
}
