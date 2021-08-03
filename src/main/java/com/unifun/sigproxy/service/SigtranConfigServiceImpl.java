package com.unifun.sigproxy.service;

import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.models.config.sctp.SctpClientAssociationConfig;
import com.unifun.sigproxy.models.config.sctp.SctpServerConfig;
import com.unifun.sigproxy.repository.SigtranStackRepository;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class SigtranConfigServiceImpl implements SigtranConfigService{
    private final SigtranStackRepository sigtranStackRepository;
    @Override
    public Set<SctpClientAssociationConfig> getClientLinksByStackId(Long stackId) throws NotFoundException {
        Optional<SigtranStack> sigtranStack = sigtranStackRepository.findById(stackId);
        return sigtranStack.map(SigtranStack::getAssociations).orElseThrow(() -> new NotFoundException("Not found stack with id " + stackId));
    }

    @Override
    public SigtranStack getSigtranStackById(Long stackId) throws NotFoundException {
        return sigtranStackRepository.findById(stackId)
                .orElseThrow(() -> new NotFoundException("Not found sigtran stack by id " + stackId));
    }

    public Set<SctpServerConfig> getSctpServersByStackId(Long stackId) throws NotFoundException {
        Optional<SigtranStack> sigtranStack = sigtranStackRepository.findById(stackId);
        return sigtranStack.map(SigtranStack::getSctpServerConfigs).orElseThrow(() -> new NotFoundException("Not found stack with id " + stackId));

    }
}
