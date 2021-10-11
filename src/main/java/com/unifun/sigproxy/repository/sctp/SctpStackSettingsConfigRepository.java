package com.unifun.sigproxy.repository.sctp;

import com.unifun.sigproxy.models.config.sctp.SctpStackSettingsConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SctpStackSettingsConfigRepository extends JpaRepository<SctpStackSettingsConfig, Long> {
}
