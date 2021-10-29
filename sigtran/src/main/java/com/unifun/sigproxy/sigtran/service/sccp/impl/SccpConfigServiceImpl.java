package com.unifun.sigproxy.sigtran.service.sccp.impl;

import com.unifun.sigproxy.sigtran.exception.SS7AddException;
import com.unifun.sigproxy.sigtran.exception.SS7NotContentException;
import com.unifun.sigproxy.sigtran.exception.SS7NotFoundException;
import com.unifun.sigproxy.sigtran.exception.SS7RemoveException;
import com.unifun.sigproxy.sigtran.models.config.SigtranStack;
import com.unifun.sigproxy.sigtran.models.config.sccp.*;
import com.unifun.sigproxy.sigtran.repository.SigtranStackRepository;
import com.unifun.sigproxy.sigtran.repository.sccp.*;
import com.unifun.sigproxy.sigtran.service.sccp.SccpConfigService;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Service
public class SccpConfigServiceImpl implements SccpConfigService {
    private final SigtranStackRepository sigtranStackRepository;
    private final SccpAddressConfigRepository sccpAddressConfigRepository;
    private final SccpAddressRuleConfigRepository sccpAddressRuleConfigRepository;
    private final SccpConcernedSignalingPointCodeConfigRepository sccpConcernedSignalingPointCodeConfigRepository;
    private final SccpLongMessageRuleConfigRepository sccpLongMessageRuleConfigRepository;
    private final SccpMtp3DestinationConfigRepository sccpMtp3DestinationConfigRepository;
    private final SccpRemoteSignalingPointConfigRepository sccpRemoteSignalingPointConfigRepository;
    private final SccpRemoteSubsystemConfigRepository sccpRemoteSubsystemConfigRepository;
    private final SccpRuleConfigRepository sccpRuleConfigRepository;
    private final SccpServiceAccessPointConfigRepository sccpServiceAccessPointConfigRepository;
    private final SccpSettingsConfigRepository sccpSettingsConfigRepository;

    @Override
    public Set<SccpAddressConfig> getAddressConfigByStackId(Long sigtranStackId) {
        SigtranStack sigtranStack = sigtranStackRepository.findById(sigtranStackId)
                .orElseThrow(() -> new SS7NotFoundException("Not found stack with id " + sigtranStackId));
        return Optional.ofNullable(sigtranStack.getSccpAddressConfigs())
                .orElseThrow(() -> new SS7NotContentException("Sigtran stack with id " + sigtranStackId + " doesn't contain Sccp Address Config"));
    }

    @Override
    public SccpAddressRuleConfig getAddressRuleConfigByRuleConfigId(Integer sccpRuleConfigId) {
        SccpRuleConfig sccpRuleConfig = sccpRuleConfigRepository.findById(sccpRuleConfigId)
                .orElseThrow(() -> new SS7NotFoundException("Not found stack with id " + sccpRuleConfigId));
        return Optional.ofNullable(sccpRuleConfig.getSccpAddressRuleConfig())
                .orElseThrow(() -> new SS7NotContentException("Sccp Rule Config with id " + sccpRuleConfigId + " doesn't contain Sccp Address Rule Config"));
    }

    @Override
    public Set<SccpConcernedSignalingPointCodeConfig> getConcernedSignalingPointCodeConfigByStackId(Long sigtranStackId) {
        SigtranStack sigtranStack = sigtranStackRepository.findById(sigtranStackId)
                .orElseThrow(() -> new SS7NotFoundException("Not found stack with id " + sigtranStackId));
        return Optional.ofNullable(sigtranStack.getSccpConcernedSignalingPointCodeConfigs())
                .orElseThrow(() -> new SS7NotContentException("Sigtran stack with id " + sigtranStackId + " doesn't contain Sccp Concerned Signaling Point Code Configs"));
    }

    @Override
    public Set<SccpLongMessageRuleConfig> getLongMessageRuleConfigByStackId(Long sigtranStackId) {
        SigtranStack sigtranStack = sigtranStackRepository.findById(sigtranStackId)
                .orElseThrow(() -> new SS7NotFoundException("Not found stack with id " + sigtranStackId));
        return Optional.ofNullable(sigtranStack.getSccpLongMessageRuleConfigs())
                .orElseThrow(() -> new SS7NotContentException("Sigtran stack with id " + sigtranStackId + " doesn't contain Sccp Long Message Rule Configs"));
    }

    @Override
    public Set<SccpMtp3DestinationConfig> getMtp3DestinationConfigBySccpServiceAccessPointConfigId(Integer sccpServiceAccessPointConfigId) {
        SccpServiceAccessPointConfig sccpServiceAccessPointConfig = sccpServiceAccessPointConfigRepository.findById(sccpServiceAccessPointConfigId)
                .orElseThrow(() -> new SS7NotFoundException("Not found stack with id " + sccpServiceAccessPointConfigId));
        return Optional.ofNullable(sccpServiceAccessPointConfig.getSccpMtp3DestinationConfigs())
                .orElseThrow(() -> new SS7NotContentException("Sccp Service Access Point Config with id " + sccpServiceAccessPointConfigId + " doesn't contain Sccp Mtp3 Destination Config"));
    }

    @Override
    public Set<SccpRemoteSignalingPointConfig> getRemoteSignalingPointConfigByStackId(Long sigtranStackId) {
        SigtranStack sigtranStack = sigtranStackRepository.findById(sigtranStackId)
                .orElseThrow(() -> new SS7NotFoundException("Not found stack with id " + sigtranStackId));
        return Optional.ofNullable(sigtranStack.getSccpRemoteSignalingPointConfigs())
                .orElseThrow(() -> new SS7NotContentException("Sigtran stack with id " + sigtranStackId + " doesn't contain Sccp Remote Signaling Point Configs"));

    }

    @Override
    public Set<SccpRemoteSubsystemConfig> getRemoteSubsystemConfigByStackId(Long sigtranStackId) {
        SigtranStack sigtranStack = sigtranStackRepository.findById(sigtranStackId)
                .orElseThrow(() -> new SS7NotFoundException("Not found stack with id " + sigtranStackId));
        return Optional.ofNullable(sigtranStack.getSccpRemoteSubsystemConfigs())
                .orElseThrow(() -> new SS7NotContentException("Sigtran stack with id " + sigtranStackId + " doesn't contain Sccp Remote Subsystem Configs"));
    }

    @Override
    public Set<SccpRuleConfig> getRuleConfigByStackId(Long sigtranStackId) {
        SigtranStack sigtranStack = sigtranStackRepository.findById(sigtranStackId)
                .orElseThrow(() -> new SS7NotFoundException("Not found stack with id " + sigtranStackId));
        return Optional.ofNullable(sigtranStack.getSccpRuleConfigs())
                .orElseThrow(() -> new SS7NotContentException("Sigtran stack with id " + sigtranStackId + " doesn't contain Sccp Rule Configs"));
    }

    @Override
    public Set<SccpServiceAccessPointConfig> getServiceAccessPointConfigByStackId(Long sigtranStackId) {
        SigtranStack sigtranStack = sigtranStackRepository.findById(sigtranStackId)
                .orElseThrow(() -> new SS7NotFoundException("Not found stack with id " + sigtranStackId));
        return Optional.ofNullable(sigtranStack.getSccpServiceAccessPointConfigs())
                .orElseThrow(() -> new SS7NotContentException("Sigtran stack with id " + sigtranStackId + " doesn't contain Sccp Service Access Point Configs"));

    }

    @Override
    public SccpSettingsConfig getSettingsConfigByStackId(Long sigtranStackId) {
        SigtranStack sigtranStack = sigtranStackRepository.findById(sigtranStackId)
                .orElseThrow(() -> new SS7NotFoundException("Not found stack with id " + sigtranStackId));
        return Optional.ofNullable(sigtranStack.getSccpSettingsConfig())
                .orElseThrow(() -> new SS7NotContentException("Sigtran stack with id " + sigtranStackId + " doesn't contain Sccp Settings Config"));
    }

    @Override
    public SccpAddressConfig getAddressConfigById(Integer sccpAddressConfigId) {
        return sccpAddressConfigRepository.findById(sccpAddressConfigId)
                .orElseThrow(() -> new SS7NotFoundException("Not found Sccp Address Config with Id " + sccpAddressConfigId));
    }

    @Override
    public SccpAddressRuleConfig getAddressRuleConfigById(Long sccpAddressRuleConfigId) {
        return sccpAddressRuleConfigRepository.findById(sccpAddressRuleConfigId)
                .orElseThrow(() -> new SS7NotFoundException("Not found Sccp Address Rule Config with Id " + sccpAddressRuleConfigId));
    }

    @Override
    public SccpConcernedSignalingPointCodeConfig getConcernedSignalingPointCodeConfigById(Integer sccpConcernedSignalingPointCodeConfigId) {
        return sccpConcernedSignalingPointCodeConfigRepository.findById(sccpConcernedSignalingPointCodeConfigId)
                .orElseThrow(() -> new SS7NotFoundException("Not found Sccp Concerned Signaling Point Code Config with Id " + sccpConcernedSignalingPointCodeConfigId));
    }

    @Override
    public SccpLongMessageRuleConfig getLongMessageRuleConfigById(Integer sccpLongMessageRuleConfigId) {
        return sccpLongMessageRuleConfigRepository.findById(sccpLongMessageRuleConfigId)
                .orElseThrow(() -> new SS7NotFoundException("Not found Sccp Long Message Rule Config with Id " + sccpLongMessageRuleConfigId));
    }

    @Override
    public SccpMtp3DestinationConfig getMtp3DestinationConfigById(Integer sccpMtp3DestinationConfigId) {
        return sccpMtp3DestinationConfigRepository.findById(sccpMtp3DestinationConfigId)
                .orElseThrow(() -> new SS7NotFoundException("Not found Sccp Mtp3 Destination Config with Id " + sccpMtp3DestinationConfigId));
    }

    @Override
    public SccpRemoteSignalingPointConfig getRemoteSignalingPointConfigById(Integer sccpRemoteSignalingPointConfigId) {
        return sccpRemoteSignalingPointConfigRepository.findById(sccpRemoteSignalingPointConfigId)
                .orElseThrow(() -> new SS7NotFoundException("Not found Sccp Remote Signaling Point Config with Id " + sccpRemoteSignalingPointConfigId));
    }

    @Override
    public SccpRemoteSubsystemConfig getRemoteSubsystemConfigById(Integer sccpRemoteSubsystemConfigId) {
        return sccpRemoteSubsystemConfigRepository.findById(sccpRemoteSubsystemConfigId)
                .orElseThrow(() -> new SS7NotFoundException("Not found Sccp Remote Subsystem Config with Id " + sccpRemoteSubsystemConfigId));
    }

    @Override
    public SccpRuleConfig getRuleConfigById(Integer sccpRuleConfigId) {
        return sccpRuleConfigRepository.findById(sccpRuleConfigId)
                .orElseThrow(() -> new SS7NotFoundException("Not found Sccp Rule Config with Id " + sccpRuleConfigId));
    }

    @Override
    public SccpServiceAccessPointConfig getServiceAccessPointConfigById(Integer sccpServiceAccessPointConfigId) {
        return sccpServiceAccessPointConfigRepository.findById(sccpServiceAccessPointConfigId)
                .orElseThrow(() -> new SS7NotFoundException("Not found Sccp ServiceA ccess Point Config with Id " + sccpServiceAccessPointConfigId));
    }


    @Override
    public SccpSettingsConfig getSettingsConfigById(Long sccpSettingsConfigId) {
        return sccpSettingsConfigRepository.findById(sccpSettingsConfigId)
                .orElseThrow(() -> new SS7NotFoundException("Not found Sccp Settings Config with Id " + sccpSettingsConfigId));
    }

    @Override
    public void removeAddressConfig(Integer sccpAddressConfigId) {
        try {
            sccpAddressConfigRepository.deleteById(sccpAddressConfigId);
        } catch (EmptyResultDataAccessException e) {
            throw new SS7RemoveException("Remove Sccp Address Config with id " + sccpAddressConfigId + " failed", e);
        }
    }

    @Override
    public void removeAddressRuleConfig(Long sccpAddressRuleConfigId) {
        try {
            sccpAddressRuleConfigRepository.deleteById(sccpAddressRuleConfigId);
        } catch (Exception e) {
            throw new SS7RemoveException("Remove Sccp Address Rule Config with id " + sccpAddressRuleConfigId + " failed", e);
        }
    }

    @Override
    public void removeConcernedSignalingPointCodeConfig(Integer sccpConcernedSignalingPointCodeConfigId) {
        try {
            sccpConcernedSignalingPointCodeConfigRepository.deleteById(sccpConcernedSignalingPointCodeConfigId);
        } catch (Exception e) {
            throw new SS7RemoveException("Remove Sccp Concerned Signaling Point Code Config with id " + sccpConcernedSignalingPointCodeConfigId + " failed", e);
        }
    }

    @Override
    public void removeLongMessageRuleConfig(Integer sccpLongMessageRuleConfigId) {
        try {
            sccpLongMessageRuleConfigRepository.deleteById(sccpLongMessageRuleConfigId);
        } catch (Exception e) {
            throw new SS7RemoveException("Remove Sccp Long Message Rule Config with id " + sccpLongMessageRuleConfigId + " failed", e);
        }
    }

    @Override
    public void removeMtp3DestinationConfig(Integer sccpMtp3DestinationConfigId) {
        try {
            sccpMtp3DestinationConfigRepository.deleteById(sccpMtp3DestinationConfigId);
        } catch (Exception e) {
            throw new SS7RemoveException("Remove Sccp Mtp3 Destination Config with id " + sccpMtp3DestinationConfigId + " failed", e);
        }
    }

    @Override
    public void removeRemoteSignalingPointConfig(Integer sccpRemoteSignalingPointConfigId) {
        try {
            sccpRemoteSignalingPointConfigRepository.deleteById(sccpRemoteSignalingPointConfigId);
        } catch (Exception e) {
            throw new SS7RemoveException("Remove Sccp Signaling Point with id " + sccpRemoteSignalingPointConfigId + " failed", e);
        }
    }

    @Override
    public void removeRemoteSubsystemConfig(Integer sccpRemoteSubsystemConfigId) {
        try {
            sccpRemoteSubsystemConfigRepository.deleteById(sccpRemoteSubsystemConfigId);
        } catch (Exception e) {
            throw new SS7RemoveException("Remove Sccp Remote Subsystem with id " + sccpRemoteSubsystemConfigId + " failed", e);
        }
    }

    @Override
    public void removeRuleConfig(Integer sccpRuleConfigId) {
        try {
            sccpRuleConfigRepository.deleteById(sccpRuleConfigId);
        } catch (Exception e) {
            throw new SS7RemoveException("Remove Sccp Rule Config with id " + sccpRuleConfigId + " failed", e);
        }
    }

    @Override
    public void removeServiceAccessPointConfig(Integer sccpServiceAccessPointConfigId) {
        try {
            sccpServiceAccessPointConfigRepository.deleteById(sccpServiceAccessPointConfigId);
        } catch (Exception e) {
            throw new SS7RemoveException("Remove Sccp Service Access Point Config with id " + sccpServiceAccessPointConfigId + " failed", e);
        }
    }

    @Override
    public void removeSettingsConfig(Long sccpSettingsConfigId) {
        try {
            sccpSettingsConfigRepository.deleteById(sccpSettingsConfigId);
        } catch (Exception e) {
            throw new SS7RemoveException("Remove Sccp Settings Config with id " + sccpSettingsConfigId + " failed", e);
        }
    }

    @Override
    public SccpAddressConfig addAddressConfig(SccpAddressConfig sccpAddressConfig) {
        try {
            return sccpAddressConfigRepository.save(sccpAddressConfig);
        } catch (Exception e){
            throw  new SS7AddException("Add Sccp Address Config failed", e);
        }
    }

    @Override
    public SccpAddressRuleConfig addAddressRuleConfig(SccpAddressRuleConfig sccpAddressRuleConfig) {
        try {
            return sccpAddressRuleConfigRepository.save(sccpAddressRuleConfig);
        } catch (Exception e){
            throw  new SS7AddException("Add Sccp Address Rule Config failed", e);
        }
    }

    @Override
    public SccpConcernedSignalingPointCodeConfig addConcernedSignalingPointCodeConfig(SccpConcernedSignalingPointCodeConfig sccpConcernedSignalingPointCodeConfig) {
        try {
            return sccpConcernedSignalingPointCodeConfigRepository.save(sccpConcernedSignalingPointCodeConfig);
        } catch (Exception e){
            throw  new SS7AddException("Add Sccp Concerned Signaling Point Code Config failed", e);
        }
    }

    @Override
    public SccpLongMessageRuleConfig addLongMessageRuleConfig(SccpLongMessageRuleConfig sccpLongMessageRuleConfig) {
        try {
            return sccpLongMessageRuleConfigRepository.save(sccpLongMessageRuleConfig);
        } catch (Exception e){
            throw  new SS7AddException("Add Sccp Long Message Rule Config failed", e);
        }
    }

    @Override
    public SccpMtp3DestinationConfig addMtp3DestinationConfig(SccpMtp3DestinationConfig sccpMtp3DestinationConfig) {
        try {
            return sccpMtp3DestinationConfigRepository.save(sccpMtp3DestinationConfig);
        } catch (Exception e){
            throw  new SS7AddException("Add Sccp Mtp3 Destination Config failed", e);
        }
    }

    @Override
    public SccpRemoteSignalingPointConfig addRemoteSignalingPointConfig(SccpRemoteSignalingPointConfig sccpRemoteSignalingPointConfig) {
        try {
            return sccpRemoteSignalingPointConfigRepository.save(sccpRemoteSignalingPointConfig);
        } catch (Exception e){
            throw  new SS7AddException("Add Sccp Remote Signaling Point Config failed", e);
        }
    }

    @Override
    public SccpRemoteSubsystemConfig addRemoteSubsystemConfig(SccpRemoteSubsystemConfig sccpRemoteSubsystemConfig) {
        try {
            return sccpRemoteSubsystemConfigRepository.save(sccpRemoteSubsystemConfig);
        } catch (Exception e){
            throw  new SS7AddException("Add Sccp Remote Subsystem Config failed", e);
        }
    }

    @Override
    public SccpRuleConfig addRuleConfig(SccpRuleConfig sccpRuleConfig) {
        try {
            return sccpRuleConfigRepository.save(sccpRuleConfig);
        } catch (Exception e){
            throw  new SS7AddException("Add Sccp RuleConfig failed", e);
        }
    }

    @Override
    public SccpServiceAccessPointConfig addServiceAccessPointConfig(SccpServiceAccessPointConfig sccpServiceAccessPointConfig) {
        try {
            return sccpServiceAccessPointConfigRepository.save(sccpServiceAccessPointConfig);
        } catch (Exception e){
            throw  new SS7AddException("Add Sccp Service Access Point Config failed", e);
        }
    }

    @Override
    public SccpSettingsConfig addSettingsConfig(SccpSettingsConfig sccpSettingsConfig) {
        try {
            return sccpSettingsConfigRepository.save(sccpSettingsConfig);
        } catch (Exception e){
            throw  new SS7AddException("Add Sccp Settings Config failed", e);
        }
    }
}
