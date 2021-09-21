package com.unifun.sigproxy.service;

import com.unifun.sigproxy.repository.m3ua.AsRepository;
import com.unifun.sigproxy.repository.m3ua.AspRepository;
import com.unifun.sigproxy.repository.m3ua.RouteRepository;
import com.unifun.sigproxy.service.m3ua.M3uaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.restcomm.protocols.ss7.m3ua.M3UAManagement;
import org.restcomm.protocols.ss7.m3ua.impl.M3UAManagementImpl;
import org.restcomm.protocols.ss7.ss7ext.Ss7ExtInterfaceDefault;
import org.springframework.beans.factory.annotation.Autowired;

import java.beans.Transient;

import static org.mockito.BDDMockito.given;

public class M3uaServiceTest {

    @Autowired
    private  M3uaService m3uaService;

    private AspRepository aspRepository;
    private AsRepository asRepository;
    private RouteRepository routeRepository;

    private M3UAManagementImpl m3UAManagement;


    @Test

    public void getManagementReturn(){
        given(m3uaService.getManagement("stack1")).willReturn(
                new M3UAManagementImpl("STACK","STACK", new Ss7ExtInterfaceDefault()));
       M3UAManagement a = m3uaService.getManagement("stack1");
    }
}
