package com.unifun.sigproxy.controller.dto;

import com.unifun.sigproxy.controller.dto.m3ua.M3uaAsConfigDto;
import com.unifun.sigproxy.controller.dto.m3ua.M3uaAspConfigDto;
import com.unifun.sigproxy.controller.dto.m3ua.M3uaStackSettingsConfigDto;
import com.unifun.sigproxy.controller.dto.sccp.*;
import com.unifun.sigproxy.controller.dto.sctp.SctpClientAssociationConfigDto;
import com.unifun.sigproxy.controller.dto.sctp.SctpServerConfigDto;
import com.unifun.sigproxy.controller.dto.sctp.SctpStackSettingsConfigDto;
import com.unifun.sigproxy.controller.dto.tcap.TcapConfigDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Builder
public class SigtranStackDto {
    private Long id;
    private String stackName;
    private Set<SctpServerConfigDto> sctpServerConfigsDto;
    private Set<SctpClientAssociationConfigDto> associationsDto;
    private SctpStackSettingsConfigDto sctpStackSettingsConfigDto;
    private Set<M3uaAsConfigDto> applicationServersDto;
    private M3uaStackSettingsConfigDto m3UaStackSettingsConfigDto;
    private Set<SccpRuleConfigDto> sccpRuleConfigsDto;
    private Set<SccpRemoteSignalingPointConfigDto> sccpRemoteSignalingPointConfigsDto;
    private Set<SccpAddressConfigDto> sccpAddressConfigsDto;
    private Set<SccpRemoteSubsystemConfigDto> sccpRemoteSubsystemConfigsDto;
    private Set<SccpServiceAccessPointConfigDto> sccpServiceAccessPointConfigsDto;
    private Set<SccpConcernedSignalingPointCodeConfigDto> sccpConcernedSignalingPointCodeConfigsDto;
    private SccpSettingsConfigDto sccpSettingsConfigDto;
    private TcapConfigDto tcapConfigDto;
}
