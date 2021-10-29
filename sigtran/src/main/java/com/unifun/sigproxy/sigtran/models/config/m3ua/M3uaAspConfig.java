package com.unifun.sigproxy.sigtran.models.config.m3ua;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
public class M3uaAspConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String sctpAssocName;

    private boolean heartbeat;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "M3UA_ASP_AS",
            joinColumns = {@JoinColumn(name = "asp_id")},
            inverseJoinColumns = {@JoinColumn(name = "as_id")}
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<M3uaAsConfig> applicationServers;

}
