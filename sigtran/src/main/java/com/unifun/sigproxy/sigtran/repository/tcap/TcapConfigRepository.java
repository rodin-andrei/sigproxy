package com.unifun.sigproxy.sigtran.repository.tcap;

import com.unifun.sigproxy.sigtran.models.config.tcap.TcapConfig;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author arodin
 */
public interface TcapConfigRepository extends JpaRepository<TcapConfig, Long> {

}
