package com.unifun.sigproxy.models.config;

import com.unifun.sigproxy.models.config.m3ua.AsConfig;
import com.unifun.sigproxy.models.config.m3ua.AspConfig;
import com.unifun.sigproxy.models.config.m3ua.M3uaStackSettings;
import com.unifun.sigproxy.models.config.sccp.*;
import com.unifun.sigproxy.models.config.sctp.ClientAssociation;
import com.unifun.sigproxy.models.config.sctp.SctpServer;
import com.unifun.sigproxy.models.config.sctp.SctpStackSettings;
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
    private Set<SctpServer> sctpServers;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "sigtranStack")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<ClientAssociation> associations;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "sigtranStack")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<AsConfig> applicationServers;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "sigtranStack")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<AspConfig> applicationServerPoints;

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
    private Set<SccpLongMessageRule> sccpLongMessageRules;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "sigtranStack")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<SccpMtp3DestinationConfig> sccpMtp3DestinationConfigs;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "sigtranStack")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<SccpConcernedSignalingPointCodeConfig> sccpConcernedSignalingPointCodeConfigs;


    @OneToOne(mappedBy = "sigtranStack")
    private SctpStackSettings sctpStackSettings;

    @OneToOne(mappedBy = "sigtranStack")
    private M3uaStackSettings m3uaStackSettings;

    @OneToOne(mappedBy = "sigtranStack")
    private SccpSettingsConfig sccpSettingsConfig;
}
