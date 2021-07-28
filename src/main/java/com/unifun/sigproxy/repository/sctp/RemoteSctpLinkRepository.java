package com.unifun.sigproxy.repository.sctp;

import com.unifun.sigproxy.models.config.sctp.SctpServerAssociationConfig;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author arodin
 */
public interface RemoteSctpLinkRepository extends JpaRepository<SctpServerAssociationConfig, Long> {

}
