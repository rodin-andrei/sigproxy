package com.unifun.sigproxy.repository;

import com.unifun.sigproxy.models.config.SigtranStack;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author arodin
 */
public interface SigtranStackRepository extends JpaRepository<SigtranStack, Long> {
    SigtranStack findByStackName(String stackName);
}
