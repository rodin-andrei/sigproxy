package com.unifun.sigproxy.repository.sccp;

import com.unifun.sigproxy.models.config.sccp.SccpLongMessageRuleConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SccpLongMessageRuleConfigRepository extends JpaRepository<SccpLongMessageRuleConfig, Integer> {
}
