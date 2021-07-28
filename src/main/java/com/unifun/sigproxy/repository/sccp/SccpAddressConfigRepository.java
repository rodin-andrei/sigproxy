package com.unifun.sigproxy.repository.sccp;

import com.unifun.sigproxy.models.config.sccp.SccpAddressConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SccpAddressConfigRepository extends JpaRepository<SccpAddressConfig, Long> {
}
