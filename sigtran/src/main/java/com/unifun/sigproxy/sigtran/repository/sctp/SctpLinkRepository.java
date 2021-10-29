package com.unifun.sigproxy.sigtran.repository.sctp;

import com.unifun.sigproxy.sigtran.models.config.sctp.SctpClientAssociationConfig;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author arodin
 */
public interface SctpLinkRepository extends JpaRepository<SctpClientAssociationConfig, Long> {
    SctpClientAssociationConfig findByLinkName(String linkName);
}
