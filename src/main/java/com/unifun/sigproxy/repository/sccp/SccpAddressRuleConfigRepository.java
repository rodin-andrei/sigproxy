package com.unifun.sigproxy.repository.sccp;

import com.unifun.sigproxy.models.config.sccp.SccpAddressRuleConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SccpAddressRuleConfigRepository extends JpaRepository<SccpAddressRuleConfig, Long> {
}
