package com.unifun.sigproxy.service.m3ua.impl;

import com.unifun.sigproxy.exception.SS7AddException;
import com.unifun.sigproxy.exception.SS7NotContentException;
import com.unifun.sigproxy.exception.SS7NotFoundException;
import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.models.config.m3ua.M3uaAsConfig;
import com.unifun.sigproxy.models.config.m3ua.M3uaAspConfig;
import com.unifun.sigproxy.models.config.m3ua.M3uaRouteConfig;
import com.unifun.sigproxy.models.config.m3ua.M3uaStackSettingsConfig;
import com.unifun.sigproxy.repository.SigtranStackRepository;
import com.unifun.sigproxy.repository.m3ua.AsRepository;
import com.unifun.sigproxy.repository.m3ua.StackSettingsReposidory;
import com.unifun.sigproxy.service.m3ua.M3uaConfigService;
import com.unifun.sigproxy.service.m3ua.M3uaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class M3uaConfigServiceImpl implements M3uaConfigService {
    private final AsRepository asRepository;
    private final StackSettingsReposidory stackSettingsReposidory;
    private final SigtranStackRepository sigtranStackRepository;

    @Override
    public Set<M3uaAsConfig> getM3uaAsConfig(Long sigtranStackId) {
        SigtranStack sigtranStack = sigtranStackRepository.findById(sigtranStackId)
                .orElseThrow(() -> new SS7NotFoundException("Not found Sigtran Stack with id " + sigtranStackId));
        return Optional.ofNullable(sigtranStack.getApplicationServers())
                .orElseThrow(() -> new SS7NotContentException("Not content m3ua As config in Sigtran Stack with id " + sigtranStackId));
    }

    @Override
    public M3uaAsConfig addM3uaAsConfig(M3uaAsConfig m3uaAsConfig) {
        try {
            return asRepository.save(m3uaAsConfig);
        } catch (Exception e) {
            throw new SS7AddException("Failed add m3ua Asp config with name " + m3uaAsConfig.getName());
        }
    }

    @Override
    public M3uaStackSettingsConfig addM3uaStackSettingsConfig(M3uaStackSettingsConfig m3uaStackSettingsConfig) {
        try {
            return stackSettingsReposidory.save(m3uaStackSettingsConfig);
        } catch (Exception e) {
            throw new SS7AddException("Add M3ua Stack Settings Config failed");
        }
    }

    @Override
    public Set<M3uaAspConfig> getM3uaAspConfig(Long sigtranStackId) {
        SigtranStack sigtranStack = sigtranStackRepository.findById(sigtranStackId)
                .orElseThrow(() -> new SS7NotFoundException("Not found Sigtran Stack with id " + sigtranStackId));
        return Optional.ofNullable(sigtranStack.getApplicationServerPoints())
                .orElseThrow(() -> new SS7NotContentException("Not content m3ua Asp config in Sigtran Stack with id " + sigtranStackId));
    }

    @Override
    public Set<M3uaRouteConfig> getM3uaRouteConfig(Long m3uaAsId) {
        M3uaAsConfig m3uaAsConfig = asRepository.findById(m3uaAsId)
                .orElseThrow(()->new SS7NotFoundException("Not found M3ua As with id " + m3uaAsId));
        return Optional.ofNullable(m3uaAsConfig.getRoutes())
                .orElseThrow(()->new SS7NotContentException("Not content M3Ua Route Config in M3ua As with id " + m3uaAsId));
    }

    @Override
    public M3uaStackSettingsConfig getM3uaStackSettingsConfig(Long sigtranStackId) {
        SigtranStack sigtranStack = sigtranStackRepository.findById(sigtranStackId)
                .orElseThrow(() -> new SS7NotFoundException("Not found Sigtran Stack with id " + sigtranStackId));
        return Optional.ofNullable(sigtranStack.getM3UaStackSettingsConfig())
                .orElseThrow(()-> new SS7NotContentException("Not content M3Ua Stack Settings Config in Sigtran Stack with id " + sigtranStackId));
    }


}
