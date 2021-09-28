package com.unifun.sigproxy.service.m3ua.impl;

import com.unifun.sigproxy.exception.SS7AddException;
import com.unifun.sigproxy.exception.SS7NotContentException;
import com.unifun.sigproxy.exception.SS7NotFoundException;
import com.unifun.sigproxy.exception.SS7RemoveException;
import com.unifun.sigproxy.models.config.SigtranStack;
import com.unifun.sigproxy.models.config.m3ua.M3uaAsConfig;
import com.unifun.sigproxy.models.config.m3ua.M3uaAspConfig;
import com.unifun.sigproxy.models.config.m3ua.M3uaRouteConfig;
import com.unifun.sigproxy.models.config.m3ua.M3uaStackSettingsConfig;
import com.unifun.sigproxy.repository.SigtranStackRepository;
import com.unifun.sigproxy.repository.m3ua.AsRepository;
import com.unifun.sigproxy.repository.m3ua.AspRepository;
import com.unifun.sigproxy.repository.m3ua.RouteRepository;
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
    private final AspRepository aspRepository;
    private final RouteRepository routeRepository;
    private final SigtranStackRepository sigtranStackRepository;

    @Override
    public Set<M3uaAsConfig> getM3uaAsConfigByStackId(Long sigtranStackId) {
        SigtranStack sigtranStack = sigtranStackRepository.findById(sigtranStackId)
                .orElseThrow(() -> new SS7NotFoundException("Not found Sigtran Stack with id " + sigtranStackId));
        return Optional.ofNullable(sigtranStack.getApplicationServers())
                .orElseThrow(() -> new SS7NotContentException("Not content m3ua As config in Sigtran Stack with id " + sigtranStackId));
    }

    @Override
    public Set<M3uaAspConfig> getM3uaAspConfigByStackId(Long sigtranStackId) {
        M3uaAsConfig m3uaAsConfig = asRepository.findById(sigtranStackId)
                .orElseThrow(() -> new SS7NotFoundException("Not found Sigtran Stack with id " + sigtranStackId));
        return Optional.ofNullable(m3uaAsConfig.getApplicationServerPoints())
                .orElseThrow(() -> new SS7NotContentException("Not content m3ua Asp config in Sigtran Stack with id " + sigtranStackId));
    }

    @Override
    public Set<M3uaRouteConfig> getM3uaRouteConfigByM3UaAsId(Long m3uaAsId) {
        M3uaAsConfig m3uaAsConfig = asRepository.findById(m3uaAsId)
                .orElseThrow(() -> new SS7NotFoundException("Not found M3ua As with id " + m3uaAsId));
        return Optional.ofNullable(m3uaAsConfig.getRoutes())
                .orElseThrow(() -> new SS7NotContentException("Not content M3Ua Route Config in M3ua As with id " + m3uaAsId));
    }

    @Override
    public M3uaStackSettingsConfig getM3uaStackSettingsConfigByStackId(Long sigtranStackId) {
        SigtranStack sigtranStack = sigtranStackRepository.findById(sigtranStackId)
                .orElseThrow(() -> new SS7NotFoundException("Not found Sigtran Stack with id " + sigtranStackId));
        return Optional.ofNullable(sigtranStack.getM3UaStackSettingsConfig())
                .orElseThrow(() -> new SS7NotContentException("Not content M3Ua Stack Settings Config in Sigtran Stack with id " + sigtranStackId));
    }

    @Override
    public M3uaAsConfig getM3uaAsConfigById(Long m3uaAsId) {
        return asRepository.findById(m3uaAsId).orElseThrow(() -> new SS7NotFoundException("Not found m3ua As with Id " + m3uaAsId));
    }

    @Override
    public M3uaAspConfig getM3uaAspConfigById(Long m3uaAspId) {
        return aspRepository.findById(m3uaAspId).orElseThrow(() -> new SS7NotFoundException("Not found m3ua Asp with Id " + m3uaAspId));
    }

    @Override
    public M3uaRouteConfig getM3uaRouteConfigById(Long m3uaRouteId) {
        return routeRepository.findById(m3uaRouteId).orElseThrow(() -> new SS7NotFoundException("Not found m3ua Route with Id " + m3uaRouteId));
    }

    @Override
    public M3uaStackSettingsConfig get3uaStackSettingsConfigById(Long m3uaStackSettingsId) {
        return stackSettingsReposidory.findById(m3uaStackSettingsId).orElseThrow(() -> new SS7NotFoundException("Not found M3ua Stack Settings with Id " + m3uaStackSettingsId));
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
    public M3uaAspConfig addM3uaAspConfig(M3uaAspConfig m3uaAspConfig) {
        try {
            return aspRepository.save(m3uaAspConfig);
        } catch (Exception e) {
            throw new SS7AddException("Add M3ua Asp Settings Config failed");
        }
    }

    @Override
    public M3uaRouteConfig addM3uaRouteConfig(M3uaRouteConfig m3uaRouteConfig) {
        try {
            return routeRepository.save(m3uaRouteConfig);
        } catch (Exception e) {
            throw new SS7AddException("Add M3ua Route Settings Config failed");
        }
    }

    @Override
    public void removeM3uaAsConfig(Long m3uaAsId) {
        try {
            asRepository.deleteById(m3uaAsId);
        } catch (Exception e) {
            throw new SS7RemoveException("Failed remove M3ua As with Id " + m3uaAsId);
        }
    }

    @Override
    public void removeM3uaStackSettingsConfig(Long m3uaStackSettingsId) {
        try {
            stackSettingsReposidory.deleteById(m3uaStackSettingsId);
        } catch (Exception e) {
            throw new SS7RemoveException("Failed remove m3ua Stack Settings with Id " + m3uaStackSettingsId);
        }
    }

    @Override
    public void removeM3uaAspConfig(Long m3uaAspId) {
        try {
            aspRepository.deleteById(m3uaAspId);
        } catch (Exception e) {
            throw new SS7RemoveException("Failed remove m3ua Asp with Id " + m3uaAspId);
        }
    }

    @Override
    public void removeM3uaRouteConfig(Long m3uaRouteId) {
        try {
            routeRepository.deleteById(m3uaRouteId);
        } catch (Exception e) {
            throw new SS7RemoveException("Failed remove m3ua Route with Id " + m3uaRouteId);
        }
    }
}
