package com.unifun.sigproxy.repository.sccp;

import com.unifun.sigproxy.models.config.sccp.SccpRemoteSignalingPointConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SccpRemoteSignalingPointConfigRepository extends JpaRepository<SccpRemoteSignalingPointConfig, Integer> {
}
