package com.unifun.sigproxy.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImpl;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SccpServiceImpl {
    private final M3uaServiceImpl m3uaService;
    private SccpStackImpl sccpStack;

//    private void init() {
//        this.sccpStack = new SccpStackImpl(GateConstants.STACKNAME + "_sccp", null);
//        sccpStack.setMtp3UserPart(1, m3uaService.getM3uaManagement());
//        sccpStack.start();
//        sccpStack.removeAllResourses();
//
//        try {
//            sccpStack.setZMarginXudtMessage(160);
//        } catch (Exception e) {
//            log.warn("Can not set sccp parameter ZMarginXudtMessage, cause {}", e.getMessage(), e);
//        }
//        SccpManagement sccpManagement = new SccpManagement();
//
//    }
}
