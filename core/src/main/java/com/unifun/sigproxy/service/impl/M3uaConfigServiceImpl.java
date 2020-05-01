package com.unifun.sigproxy.service.impl;

import com.unifun.sigproxy.repository.SigtranRepository;
import com.unifun.sigproxy.service.M3uaConfigService;
import com.unifun.sigproxy.service.M3uaService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class M3uaConfigServiceImpl implements M3uaConfigService {
    private static final Logger LOGGER = LoggerFactory.getLogger(M3uaConfigServiceImpl.class);

    private final SigtranRepository sigtranRepository;
    private final M3uaService m3uaService;


}
