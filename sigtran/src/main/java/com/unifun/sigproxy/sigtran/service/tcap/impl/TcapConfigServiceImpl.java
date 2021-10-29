package com.unifun.sigproxy.sigtran.service.tcap.impl;

import com.unifun.sigproxy.sigtran.exception.SS7AddException;
import com.unifun.sigproxy.sigtran.exception.SS7NotContentException;
import com.unifun.sigproxy.sigtran.exception.SS7NotFoundException;
import com.unifun.sigproxy.sigtran.exception.SS7RemoveException;
import com.unifun.sigproxy.sigtran.models.config.SigtranStack;
import com.unifun.sigproxy.sigtran.models.config.tcap.TcapConfig;
import com.unifun.sigproxy.sigtran.repository.SigtranStackRepository;
import com.unifun.sigproxy.sigtran.repository.tcap.TcapConfigRepository;
import com.unifun.sigproxy.sigtran.service.tcap.TcapConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TcapConfigServiceImpl implements TcapConfigService {

    private final TcapConfigRepository tcapConfigRepository;
    private final SigtranStackRepository sigtranStackRepository;

    @Override
    public TcapConfig getTcapConfigById(Long id) {
        return tcapConfigRepository.findById(id).orElseThrow(() -> new SS7NotFoundException("Not found Tcap Config with id " + id));
    }

    @Override
    public TcapConfig getTcapConfigByStackId(Long stackId) {
        SigtranStack sigtranStack = sigtranStackRepository.findById(stackId)
                .orElseThrow(() -> new SS7NotFoundException("Not found Sigtran Stack with id " + stackId));
        return Optional.ofNullable(sigtranStack.getTcapConfig())
                .orElseThrow(() -> new SS7NotContentException("Not content Tcap Setings in Sigtran Stack with id " + stackId));

    }

    @Override
    public TcapConfig addTcapConfig(TcapConfig tcapConfig) {
        try {
            return tcapConfigRepository.save(tcapConfig);
        } catch (Exception e) {
            throw new SS7AddException("Add failed new Tcap config");
        }
    }

    @Override
    public void removeTcap(Long tcapId) {
        try {
            tcapConfigRepository.deleteById(tcapId);
        } catch (Exception e) {
            throw new SS7RemoveException("Remove tcap with Id "+tcapId+" failed.");
        }
    }
}
