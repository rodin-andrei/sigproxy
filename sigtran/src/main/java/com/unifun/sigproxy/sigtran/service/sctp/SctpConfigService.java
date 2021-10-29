package com.unifun.sigproxy.sigtran.service.sctp;

import com.unifun.sigproxy.sigtran.models.config.sctp.SctpClientAssociationConfig;
import com.unifun.sigproxy.sigtran.models.config.sctp.SctpServerAssociationConfig;
import com.unifun.sigproxy.sigtran.models.config.sctp.SctpServerConfig;
import com.unifun.sigproxy.sigtran.models.config.sctp.SctpStackSettingsConfig;

import java.util.Set;

public interface SctpConfigService {

    SctpClientAssociationConfig getSctpClientAssociationConfigById(Long SctpClientAssociationConfigId);

    SctpServerAssociationConfig getSctpServerAssociationConfigById(Long SctpServerAssociationConfigId);

    SctpServerConfig getSctpServerConfigById(Long SctpServerConfigId);

    SctpStackSettingsConfig getSctpStackSettingsConfigById(Long SctpStackSettingsConfigId);

    Set<SctpClientAssociationConfig> getSctpClientAssociationConfigByStackId(Long stackId);

    Set<SctpServerAssociationConfig> getSctpServerAssociationConfigBySctpServerConfigId(Long SctpServerConfig);

    Set<SctpServerConfig> getSctpServerConfigByStackId(Long stackId);

    SctpClientAssociationConfig addSctpClientAssociationConfig(SctpClientAssociationConfig sctpClientAssociationConfig);

    SctpServerAssociationConfig addSctpServerAssociationConfig(SctpServerAssociationConfig sctpServerAssociationConfig);

    SctpServerConfig addSctpServerConfig(SctpServerConfig sctpServer);

    void removeSctpClientAssociationConfigById(Long linkId);

    void removeSctpServerAssociationConfigById(Long serverLinkId);

    void removeSctpServerConfigById(Long sctpServerConfigId);

    void removeSctpStackSettingsConfigById(Long sctpStackSettingsConfigId);
}
