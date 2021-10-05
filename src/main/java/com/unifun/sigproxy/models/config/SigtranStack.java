package com.unifun.sigproxy.models.config;

import com.unifun.sigproxy.models.config.m3ua.M3uaAsConfig;
import com.unifun.sigproxy.models.config.m3ua.M3uaAspConfig;
import com.unifun.sigproxy.models.config.m3ua.M3uaStackSettingsConfig;
import com.unifun.sigproxy.models.config.sccp.*;
import com.unifun.sigproxy.models.config.sctp.SctpClientAssociationConfig;
import com.unifun.sigproxy.models.config.sctp.SctpServerConfig;
import com.unifun.sigproxy.models.config.sctp.SctpStackSettingsConfig;
import com.unifun.sigproxy.models.config.tcap.TcapConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

/**
 * @author arodin
 */
@Data
@Entity
public class SigtranStack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String stackName;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "sigtranStack")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<SctpServerConfig> sctpServerConfigs;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "sigtranStack")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<SctpClientAssociationConfig> associations;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "sigtranStack")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<M3uaAsConfig> applicationServers;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "sigtranStack")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<M3uaAspConfig> applicationServerPoints;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "sigtranStack")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<SccpRuleConfig> sccpRuleConfigs;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "sigtranStack")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<SccpRemoteSignalingPointConfig> sccpRemoteSignalingPointConfigs;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "sigtranStack")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<SccpAddressConfig> sccpAddressConfigs;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "sigtranStack")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<SccpRemoteSubsystemConfig> sccpRemoteSubsystemConfigs;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "sigtranStack")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<SccpServiceAccessPointConfig> sccpServiceAccessPointConfigs;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "sigtranStack")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<SccpLongMessageRuleConfig> sccpLongMessageRuleConfigs;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "sigtranStack")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<SccpConcernedSignalingPointCodeConfig> sccpConcernedSignalingPointCodeConfigs;

    @OneToOne(mappedBy = "sigtranStack")
    private SctpStackSettingsConfig sctpStackSettingsConfig;

    @OneToOne(mappedBy = "sigtranStack")
    private M3uaStackSettingsConfig m3UaStackSettingsConfig;

    @OneToOne(mappedBy = "sigtranStack")
    private SccpSettingsConfig sccpSettingsConfig;

    @OneToOne(mappedBy = "sigtranStack")
    private TcapConfig tcapConfig;
}
