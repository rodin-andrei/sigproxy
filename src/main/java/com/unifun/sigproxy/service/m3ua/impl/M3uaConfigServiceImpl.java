package com.unifun.sigproxy.service.m3ua.impl;

import com.unifun.sigproxy.service.m3ua.M3uaConfigService;
import com.unifun.sigproxy.service.m3ua.M3uaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class M3uaConfigServiceImpl implements M3uaConfigService {
    private final M3uaService m3uaService;


}
