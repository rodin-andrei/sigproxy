package com.unifun.sigproxy.sigtran.repository.sccp;

import com.unifun.sigproxy.sigtran.models.config.sccp.SccpServiceAccessPointConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SccpServiceAccessPointConfigRepository extends JpaRepository<SccpServiceAccessPointConfig, Integer> {
}
