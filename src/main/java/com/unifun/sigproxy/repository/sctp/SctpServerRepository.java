package com.unifun.sigproxy.repository.sctp;

import com.unifun.sigproxy.models.config.sctp.SctpServerConfig;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author arodin
 */
public interface SctpServerRepository extends JpaRepository<SctpServerConfig, Long> {

}
