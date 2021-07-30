package com.unifun.sigproxy.aaaaa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.restcomm.protocols.ss7.m3ua.M3UAManagement;
import org.restcomm.protocols.ss7.mtp.*;

@Slf4j
@RequiredArgsConstructor
public class TestM3uaListener implements Mtp3UserPartListener {
    private final M3UAManagement m3uaManagement;

    @Override
    public void onMtp3TransferMessage(Mtp3TransferPrimitive mtp3TransferPrimitive) {
        log.info("onMtp3TransferMessage " + m3uaManagement.getName());
    }

    @Override
    public void onMtp3PauseMessage(Mtp3PausePrimitive mtp3PausePrimitive) {
        log.info("onMtp3PauseMessage " + m3uaManagement.getName());
    }

    @Override
    public void onMtp3ResumeMessage(Mtp3ResumePrimitive mtp3ResumePrimitive) {
        log.info("onMtp3ResumeMessage " + m3uaManagement.getName());
    }

    @Override
    public void onMtp3StatusMessage(Mtp3StatusPrimitive mtp3StatusPrimitive) {
        log.info("onMtp3StatusMessage " + m3uaManagement.getName());
    }

    @Override
    public void onMtp3EndCongestionMessage(Mtp3EndCongestionPrimitive mtp3EndCongestionPrimitive) {
        log.info("onMtp3EndCongestionMessage " + m3uaManagement.getName());
    }
}
