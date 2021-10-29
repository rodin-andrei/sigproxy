package com.unifun.sigproxy.sigtran.repository.sctp;

import com.unifun.sigproxy.sigtran.models.config.sctp.SctpServerConfig;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author arodin
 */
public interface SctpServerRepository extends JpaRepository<SctpServerConfig, Long> {

}
