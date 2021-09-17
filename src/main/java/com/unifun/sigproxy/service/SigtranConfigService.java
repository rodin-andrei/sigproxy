package com.unifun.sigproxy.service;

import com.unifun.sigproxy.models.config.SigtranStack;

import java.util.List;
import java.util.Set;

public interface SigtranConfigService {
    SigtranStack getSigtranStackById(Long stackId);
    List<SigtranStack> getSigtranStacks();
    SigtranStack addSigtranStack(SigtranStack sigtranStackDao);
}
