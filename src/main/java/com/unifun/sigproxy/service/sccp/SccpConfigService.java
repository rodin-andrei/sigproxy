package com.unifun.sigproxy.service.sccp;

import com.unifun.sigproxy.models.config.sccp.*;

import java.util.Set;

public interface SccpConfigService {
    Set<SccpAddressConfig> getAddressConfigByStackId(Long sigtranStackId);

    SccpAddressRuleConfig getAddressRuleConfigByRuleConfigId(Integer sccpRuleConfigId);

    Set<SccpConcernedSignalingPointCodeConfig> getConcernedSignalingPointCodeConfigByStackId(Long sigtranStackId);

    Set<SccpLongMessageRuleConfig> getLongMessageRuleConfigByStackId(Long sigtranStackId);

    Set<SccpMtp3DestinationConfig> getMtp3DestinationConfigBySccpServiceAccessPointConfigId(Integer sccpServiceAccessPointConfigId);

    Set<SccpRemoteSignalingPointConfig> getRemoteSignalingPointConfigByStackId(Long sigtranStackId);

    Set<SccpRemoteSubsystemConfig> getRemoteSubsystemConfigByStackId(Long sigtranStackId);

    Set<SccpRuleConfig> getRuleConfigByStackId(Long sigtranStackId);

    Set<SccpServiceAccessPointConfig> getServiceAccessPointConfigByStackId(Long sigtranStackId);

    SccpSettingsConfig getSettingsConfigByStackId(Long sigtranStackId);

    SccpAddressConfig getAddressConfigById(Integer sccpAddressConfigId);

    SccpAddressRuleConfig getAddressRuleConfigById(Long sccpAddressRuleConfigId);

    SccpConcernedSignalingPointCodeConfig getConcernedSignalingPointCodeConfigById(Integer sccpConcernedSignalingPointCodeConfigId);

    SccpLongMessageRuleConfig getLongMessageRuleConfigById(Integer sccpLongMessageRuleConfigId);

    SccpMtp3DestinationConfig getMtp3DestinationConfigById(Integer sccpMtp3DestinationConfigId);

    SccpRemoteSignalingPointConfig getRemoteSignalingPointConfigById(Integer sccpRemoteSignalingPointConfigId);

    SccpRemoteSubsystemConfig getRemoteSubsystemConfigById(Integer sccpRemoteSubsystemConfigId);

    SccpRuleConfig getRuleConfigById(Integer sccpRuleConfigId);

    SccpServiceAccessPointConfig getServiceAccessPointConfigById(Integer sccpServiceAccessPointConfigId);

    SccpSettingsConfig getSettingsConfigById(Long sccpSettingsConfigId);

    void removeAddressConfig(Integer sccpAddressConfigId);

    void removeAddressRuleConfig(Long sccpAddressRuleConfigId);

    void removeConcernedSignalingPointCodeConfig(Integer sccpConcernedSignalingPointCodeConfigId);

    void removeLongMessageRuleConfig(Integer sccpLongMessageRuleConfigId);

    void removeMtp3DestinationConfig(Integer sccpMtp3DestinationConfigId);

    void removeRemoteSignalingPointConfig(Integer sccpRemoteSignalingPointConfigId);

    void removeRemoteSubsystemConfig(Integer sccpRemoteSubsystemConfigId);

    void removeRuleConfig(Integer sccpRuleConfigId);

    void removeServiceAccessPointConfig(Integer sccpServiceAccessPointConfigId);

    void removeSettingsConfig(Long sccpSettingsConfigId);

    SccpAddressConfig addAddressConfig(SccpAddressConfig sccpAddressConfig);

    SccpAddressRuleConfig addAddressRuleConfig(SccpAddressRuleConfig sccpAddressRuleConfig);

    SccpConcernedSignalingPointCodeConfig addConcernedSignalingPointCodeConfig(SccpConcernedSignalingPointCodeConfig sccpConcernedSignalingPointCodeConfig);

    SccpLongMessageRuleConfig addLongMessageRuleConfig(SccpLongMessageRuleConfig sccpLongMessageRuleConfig);

    SccpMtp3DestinationConfig addMtp3DestinationConfig(SccpMtp3DestinationConfig sccpMtp3DestinationConfig);

    SccpRemoteSignalingPointConfig addRemoteSignalingPointConfig(SccpRemoteSignalingPointConfig sccpRemoteSignalingPointConfig);

    SccpRemoteSubsystemConfig addRemoteSubsystemConfig(SccpRemoteSubsystemConfig sccpRemoteSubsystemConfig);

    SccpRuleConfig addRuleConfig(SccpRuleConfig sccpRuleConfig);

    SccpServiceAccessPointConfig addServiceAccessPointConfig(SccpServiceAccessPointConfig sccpServiceAccessPointConfig);

    SccpSettingsConfig addSettingsConfig(SccpSettingsConfig sccpSettingsConfig);


}
