package com.unifun.sigproxy.sigtran.repository.sccp;

import com.unifun.sigproxy.sigtran.models.config.sccp.SccpAddressConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SccpAddressConfigRepository extends JpaRepository<SccpAddressConfig, Integer> {
}
