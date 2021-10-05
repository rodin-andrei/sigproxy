package com.unifun.sigproxy.repository.sccp;

import com.unifun.sigproxy.models.config.sccp.SccpServiceAccessPointConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SccpServiceAccessPointConfigRepository extends JpaRepository<SccpServiceAccessPointConfig, Integer> {
}
