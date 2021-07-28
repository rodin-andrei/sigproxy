package com.unifun.sigproxy.repository.sctp;

import com.unifun.sigproxy.models.config.sctp.SctpClientAssociationConfig;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author arodin
 */
public interface SctpLinkRepository extends JpaRepository<SctpClientAssociationConfig, Long> {
    SctpClientAssociationConfig findByLinkName(String linkName);
}
