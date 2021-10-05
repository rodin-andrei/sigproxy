package com.unifun.sigproxy.repository.sccp;

import com.unifun.sigproxy.models.config.sccp.SccpRuleConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SccpRuleConfigRepository extends JpaRepository<SccpRuleConfig, Long> {
}
