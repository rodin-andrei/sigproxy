package com.unifun.sigproxy.models.config;

import com.unifun.sigproxy.models.config.m3ua.AsConfig;
import com.unifun.sigproxy.models.config.m3ua.AspConfig;
import com.unifun.sigproxy.models.config.sctp.ClientAssociation;
import com.unifun.sigproxy.models.config.sctp.SctpServer;
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
}
