package com.unifun.sigproxy.models.config.m3ua;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"applicationServers"})
@ToString(exclude = {"applicationServers"})
@Entity
public class AspConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String sctpAssocName;

    private Integer aspId;

    private boolean heartbeat;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "ASP_AS",
            joinColumns = {@JoinColumn(name = "asp_id")},
            inverseJoinColumns = {@JoinColumn(name = "as_id")}
    )
    private Set<AsConfig> applicationServers;
}
