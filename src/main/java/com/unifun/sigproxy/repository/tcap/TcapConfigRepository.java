package com.unifun.sigproxy.repository.tcap;

import com.unifun.sigproxy.models.config.tcap.TcapConfig;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author arodin
 */
public interface TcapConfigRepository extends JpaRepository<TcapConfig, Long> {

}
