package com.unifun.sigproxy.repository.m3ua;

import com.unifun.sigproxy.models.config.m3ua.M3uaStackSettingsConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StackSettingsReposidory extends JpaRepository<M3uaStackSettingsConfig, Long> {
}
