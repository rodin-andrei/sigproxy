package com.unifun.sigproxy.sigtran.repository.sctp;

import com.unifun.sigproxy.sigtran.models.config.sctp.SctpServerAssociationConfig;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author arodin
 */
public interface RemoteSctpLinkRepository extends JpaRepository<SctpServerAssociationConfig, Long> {

}
