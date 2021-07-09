package com.unifun.sigproxy.repository.sctp;

import com.unifun.sigproxy.models.config.sctp.ClientLink;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author arodin
 */
public interface SctpLinkRepository extends JpaRepository<ClientLink, Long> {

}
