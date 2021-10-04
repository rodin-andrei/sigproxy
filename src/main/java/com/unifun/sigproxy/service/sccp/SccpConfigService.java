package com.unifun.sigproxy.service.sccp;

import com.unifun.sigproxy.models.config.sccp.*;

import java.util.Set;

public interface SccpConfigService {
    Set<SccpAddressConfig> getAddressConfigByStackId(Long sigtranStackId);

    SccpAddressRuleConfig getAddressRuleConfigByRuleConfigId(Long sccpRuleConfigId);

    Set<SccpConcernedSignalingPointCodeConfig> getConcernedSignalingPointCodeConfigByStackId(Long sigtranStackId);

    Set<SccpLongMessageRuleConfig> getLongMessageRuleConfigByStackId(Long sigtranStackId);

    Set<SccpMtp3DestinationConfig> getMtp3DestinationConfigBySccpServiceAccessPointConfigId(Integer sccpServiceAccessPointConfigId);

    Set<SccpRemoteSignalingPointConfig> getRemoteSignalingPointConfigByStackId(Long sigtranStackId);

    Set<SccpRemoteSubsystemConfig> getRemoteSubsystemConfigByStackId(Long sigtranStackId);

    Set<SccpRuleConfig> getRuleConfigByStackId(Long sigtranStackId);

    Set<SccpServiceAccessPointConfig> getServiceAccessPointConfigByStackId(Long sigtranStackId);

    SccpSettingsConfig getSettingsConfigByStackId(Long sigtranStackId);

    SccpAddressConfig getAddressConfigById(Long sccpAddressConfigId);

    SccpAddressRuleConfig getAddressRuleConfigById(Long sccpAddressRuleConfigId);

    SccpConcernedSignalingPointCodeConfig getConcernedSignalingPointCodeConfigById(Long sccpConcernedSignalingPointCodeConfigId);

    SccpLongMessageRuleConfig getLongMessageRuleConfigById(Long sccpLongMessageRuleConfigId);

    SccpMtp3DestinationConfig getMtp3DestinationConfigById(Long sccpMtp3DestinationConfigId);

    SccpRemoteSignalingPointConfig getRemoteSignalingPointConfigById(Long sccpRemoteSignalingPointConfigId);

    SccpRemoteSubsystemConfig getRemoteSubsystemConfigById(Long sccpRemoteSubsystemConfigId);

    SccpRuleConfig getRuleConfigById(Long sccpRuleConfigId);

    SccpServiceAccessPointConfig getServiceAccessPointConfigById(Integer sccpServiceAccessPointConfigId);

    SccpSettingsConfig getSettingsConfigById(Long sccpSettingsConfigId);

    void removeAddressConfig(Long sccpAddressConfigId);

    void removeAddressRuleConfig(Long sccpAddressRuleConfigId);

    void removeConcernedSignalingPointCodeConfig(Long sccpConcernedSignalingPointCodeConfigId);

    void removeLongMessageRuleConfig(Long sccpLongMessageRuleConfigId);

    void removeMtp3DestinationConfig(Long sccpMtp3DestinationConfigId);

    void removeRemoteSignalingPointConfig(Long sccpRemoteSignalingPointConfigId);

    void removeRemoteSubsystemConfig(Long sccpRemoteSubsystemConfigId);

    void removeRuleConfig(Long sccpRuleConfigId);

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
