package com.unifun.sigproxy.sigtran.repository.sccp;

import com.unifun.sigproxy.sigtran.models.config.sccp.SccpRemoteSubsystemConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SccpRemoteSubsystemConfigRepository extends JpaRepository<SccpRemoteSubsystemConfig, Integer> {
}
