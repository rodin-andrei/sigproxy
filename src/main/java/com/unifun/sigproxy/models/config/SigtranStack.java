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
    private Set<RemoteSignalingPoint> remoteSignalingPoints;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "sigtranStack")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<SccpAddressConfig> sccpAddressConfigs;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "sigtranStack")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<RemoteSubsystem> remoteSubsystems;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "sigtranStack")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<ServiceAccessPointConfig> serviceAccessPointConfigs;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "sigtranStack")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<LongMessageRule> longMessageRules;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "sigtranStack")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Mtp3Destination> mtp3Destinations;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "sigtranStack")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<ConcernedSignalingPointCode> concernedSignalingPointCodes;


    @OneToOne(mappedBy = "sigtranStack")
    private SctpStackSettings sctpStackSettings;

    @OneToOne(mappedBy = "sigtranStack")
    private M3uaStackSettings m3uaStackSettings;

    @OneToOne(mappedBy = "sigtranStack")
    private SccpSettings sccpSettings;
}
