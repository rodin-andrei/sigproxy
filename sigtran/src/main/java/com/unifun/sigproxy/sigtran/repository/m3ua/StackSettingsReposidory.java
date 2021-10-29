package com.unifun.sigproxy.sigtran.repository.m3ua;

import com.unifun.sigproxy.sigtran.models.config.m3ua.M3uaStackSettingsConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StackSettingsReposidory extends JpaRepository<M3uaStackSettingsConfig, Long> {
}
