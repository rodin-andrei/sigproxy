package com.unifun.sigproxy.repository.sccp;

import com.unifun.sigproxy.models.config.sccp.SccpRemoteSubsystemConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SccpRemoteSubsystemConfigRepository extends JpaRepository<SccpRemoteSubsystemConfig, Integer> {
}
