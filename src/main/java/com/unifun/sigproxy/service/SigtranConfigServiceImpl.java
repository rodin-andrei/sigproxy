package com.unifun.sigproxy.service;

import com.unifun.sigproxy.controller.dto.SigtranStackDto;
import com.unifun.sigproxy.exception.SS7NotContentException;
import com.unifun.sigproxy.exception.SS7NotFoundException;
import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.models.config.sctp.SctpClientAssociationConfig;
import com.unifun.sigproxy.models.config.sctp.SctpServerConfig;
import com.unifun.sigproxy.repository.SigtranStackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class SigtranConfigServiceImpl implements SigtranConfigService {

    private final SigtranStackRepository sigtranStackRepository;

    @Override
    public Set<SctpClientAssociationConfig> getClientLinksByStackId(Long stackId) {

        Optional<SigtranStack> sigtranStack = sigtranStackRepository.findById(stackId);
        if (sigtranStack.map(SigtranStack::getAssociations).isPresent()) {
            if (sigtranStack.map(SigtranStack::getAssociations).get().isEmpty())
                throw new SS7NotContentException("Sigtran stack with id " + stackId + " doesn't contain sctp client links");
            else return sigtranStack.map(SigtranStack::getAssociations).get();
        } else throw new SS7NotFoundException("Not found stack with id " + stackId);
    }

    @Override
    public SigtranStack getSigtranStackById(Long stackId) {

        return sigtranStackRepository.findById(stackId)
                .orElseThrow(() -> new SS7NotFoundException("Not found sigtran stack with id " + stackId));
    }

    public Set<SctpServerConfig> getSctpServersByStackId(Long stackId) {

        Optional<SigtranStack> sigtranStack = sigtranStackRepository.findById(stackId);
        if(sigtranStack.map(SigtranStack::getSctpServerConfigs).isPresent()){
            if(sigtranStack.map(SigtranStack::getSctpServerConfigs).get().isEmpty()){
                throw new SS7NotContentException("Sigtran stack with id " + stackId + " doesn't contain sctp Servers");
            } else return sigtranStack.map(SigtranStack::getSctpServerConfigs).get();
        } else throw new SS7NotFoundException("Not found stack with id " + stackId);
    }

    @Override
    public List<SigtranStack> getSigtranStacks() {
        return sigtranStackRepository.findAll();
    }
}
