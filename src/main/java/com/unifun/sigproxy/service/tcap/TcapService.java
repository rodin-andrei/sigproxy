package com.unifun.sigproxy.service.tcap;

import com.unifun.sigproxy.exception.InitializingException;
import com.unifun.sigproxy.exception.NoConfigurationException;
import com.unifun.sigproxy.models.config.SigtranStack;
import org.restcomm.protocols.ss7.tcap.api.TCAPStack;

public interface TcapService {
    void initialize(SigtranStack sigtranStack) throws NoConfigurationException, InitializingException;

    TCAPStack getTcapStack(String stackName);
}
