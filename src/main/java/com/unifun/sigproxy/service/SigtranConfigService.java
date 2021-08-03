package com.unifun.sigproxy.service;

import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.models.config.sctp.SctpClientAssociationConfig;
import com.unifun.sigproxy.models.config.sctp.SctpServerConfig;
import javassist.NotFoundException;

import java.util.List;
import java.util.Set;

public interface SigtranConfigService {
    Set<SctpClientAssociationConfig> getClientLinksByStackId(Long stackId) throws NotFoundException;
    SigtranStack getSigtranStackById(Long stackId) throws NotFoundException;
    Set<SctpServerConfig> getSctpServersByStackId(Long stackId) throws NotFoundException;
}
