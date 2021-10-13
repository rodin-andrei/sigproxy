package com.unifun.sigproxy.service;

import com.unifun.sigproxy.models.config.SigtranStack;

import java.util.List;
import java.util.Set;

public interface SigtranConfigService {
    List<SigtranStack> getSigtranStacks();
    SigtranStack getSigtranStackById(Long stackId);
    SigtranStack addSigtranStack(SigtranStack sigtranStackDao);
    void removeSigtranStack(Long sigtranStackId);
}
