package com.unifun.sigproxy.service.impl.config;

import com.unifun.sigproxy.service.M3uaConfigService;
import com.unifun.sigproxy.service.M3uaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class M3uaConfigServiceImpl implements M3uaConfigService {
    private final M3uaService m3uaService;


}
