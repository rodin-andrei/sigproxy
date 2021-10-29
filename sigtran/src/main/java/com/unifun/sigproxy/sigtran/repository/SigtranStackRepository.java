package com.unifun.sigproxy.sigtran.repository;

import com.unifun.sigproxy.sigtran.models.config.SigtranStack;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author arodin
 */
public interface SigtranStackRepository extends JpaRepository<SigtranStack, Long> {
    SigtranStack findByStackName(String stackName);
}
