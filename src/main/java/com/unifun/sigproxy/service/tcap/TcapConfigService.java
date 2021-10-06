package com.unifun.sigproxy.service.tcap;

import com.unifun.sigproxy.models.config.tcap.TcapConfig;

public interface TcapConfigService {
    TcapConfig getTcapConfigById(Long id);
    TcapConfig getTcapConfigByStackId(Long stackId);
    TcapConfig addTcapConfig(TcapConfig tcapConfig);
    void removeTcap(Long tcapId);
}
