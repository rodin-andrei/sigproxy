package com.unifun.sigproxy.service;

import com.unifun.sigproxy.exception.SS7AddException;
import com.unifun.sigproxy.exception.SS7NotFoundException;
import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.repository.SigtranStackRepository;
import com.unifun.sigproxy.service.m3ua.M3uaConfigService;
import com.unifun.sigproxy.service.sctp.SctpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SigtranConfigServiceImpl implements SigtranConfigService {

    private final SigtranStackRepository sigtranStackRepository;

    @Override
    public SigtranStack getSigtranStackById(Long stackId) {

        return sigtranStackRepository.findById(stackId)
                .orElseThrow(() -> new SS7NotFoundException("Not found sigtran stack with id " + stackId));
    }

    @Override
    public List<SigtranStack> getSigtranStacks() {
        return sigtranStackRepository.findAll();
    }

    @Override
    public SigtranStack addSigtranStack(SigtranStack sigtranStack) {
        try {
            return sigtranStackRepository.save(sigtranStack);
        } catch (Exception e){
            throw new SS7AddException("Add Sigtran stack with name "+sigtranStack+ "failed", e.getCause().getCause());
        }
    }
}
