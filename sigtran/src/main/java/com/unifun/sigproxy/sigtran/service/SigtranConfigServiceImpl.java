package com.unifun.sigproxy.sigtran.service;

import com.unifun.sigproxy.sigtran.exception.SS7AddException;
import com.unifun.sigproxy.sigtran.exception.SS7NotFoundException;
import com.unifun.sigproxy.sigtran.exception.SS7RemoveException;
import com.unifun.sigproxy.sigtran.models.config.SigtranStack;
import com.unifun.sigproxy.sigtran.repository.SigtranStackRepository;
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
        } catch (Exception e) {
            throw new SS7AddException("Add Sigtran stack with name " + sigtranStack + "failed", e.getCause().getCause());
        }
    }

    @Override
    public void removeSigtranStack(Long sigtranStackId) {
        try {
            sigtranStackRepository.deleteById(sigtranStackId);
        } catch (Exception e) {
            throw new SS7RemoveException("Remove Sigtran Stack with id " + sigtranStackId + " failed", e);
        }
    }
}
