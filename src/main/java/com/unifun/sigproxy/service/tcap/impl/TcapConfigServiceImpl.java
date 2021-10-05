package com.unifun.sigproxy.service.tcap.impl;

import com.unifun.sigproxy.exception.SS7AddException;
import com.unifun.sigproxy.exception.SS7NotContentException;
import com.unifun.sigproxy.exception.SS7NotFoundException;
import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.models.config.tcap.TcapConfig;
import com.unifun.sigproxy.repository.SigtranStackRepository;
import com.unifun.sigproxy.repository.tcap.TcapConfigRepository;
import com.unifun.sigproxy.service.tcap.TcapConfigService;
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
        } catch (Exception e){
            throw new SS7AddException("Add failed new Tcap config");
        }
    }
}
