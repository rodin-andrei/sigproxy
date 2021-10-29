package com.unifun.sigproxy.sigtran.repository.sccp;

import com.unifun.sigproxy.sigtran.models.config.sccp.SccpRuleConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SccpRuleConfigRepository extends JpaRepository<SccpRuleConfig, Integer> {
}
