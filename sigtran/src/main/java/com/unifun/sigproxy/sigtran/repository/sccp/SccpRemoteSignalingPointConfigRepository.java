package com.unifun.sigproxy.sigtran.repository.sccp;

import com.unifun.sigproxy.sigtran.models.config.sccp.SccpRemoteSignalingPointConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SccpRemoteSignalingPointConfigRepository extends JpaRepository<SccpRemoteSignalingPointConfig, Integer> {
}
