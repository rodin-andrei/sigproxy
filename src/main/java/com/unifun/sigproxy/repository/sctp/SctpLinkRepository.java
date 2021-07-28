package com.unifun.sigproxy.repository.sctp;

import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.models.config.sctp.ClientAssociation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author arodin
 */
public interface SctpLinkRepository extends JpaRepository<ClientAssociation, Long> {
    ClientAssociation findByLinkName(String linkName);
}
