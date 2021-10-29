package com.unifun.sigproxy.sigtran.service;

import com.unifun.sigproxy.sigtran.models.config.SigtranStack;

import java.util.List;

public interface SigtranConfigService {
    List<SigtranStack> getSigtranStacks();
    SigtranStack getSigtranStackById(Long stackId);
    SigtranStack addSigtranStack(SigtranStack sigtranStackDao);
    void removeSigtranStack(Long sigtranStackId);
}
