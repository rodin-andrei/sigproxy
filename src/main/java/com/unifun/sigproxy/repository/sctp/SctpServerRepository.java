package com.unifun.sigproxy.repository.sctp;

import com.unifun.sigproxy.models.config.sctp.SctpServer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author arodin
 */
public interface SctpServerRepository extends JpaRepository<SctpServer, Long> {

}
