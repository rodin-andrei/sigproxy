package com.unifun.sigproxy.service.map;

import com.unifun.sigproxy.exception.InitializingException;
import com.unifun.sigproxy.exception.NoConfigurationException;
import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.service.rabbit.pojo.MapSupplementaryMessageRabbit;
import org.restcomm.protocols.ss7.map.MAPStackImpl;
import org.restcomm.protocols.ss7.map.api.MAPStack;
import org.restcomm.protocols.ss7.map.api.service.supplementary.MAPDialogSupplementary;

public interface MapService {
    void initialize(SigtranStack sigtranStack) throws NoConfigurationException, InitializingException;

    void test(String stackName, int addrA, int addrB);

    MAPDialogSupplementary createDialogProcessUnstructuredSSRequest(MapSupplementaryMessageRabbit mapSupplementaryMessageRabbit);

    MAPStackImpl getMapStack(String stackName);
}
