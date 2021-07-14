package com.unifun.sigproxy.models.config.m3ua;

import com.unifun.sigproxy.models.config.SigtranStack;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
public class AspConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String sctpAssocName;

    private boolean heartbeat;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "ASP_AS",
            joinColumns = {@JoinColumn(name = "asp_id")},
            inverseJoinColumns = {@JoinColumn(name = "as_id")}
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<AsConfig> applicationServers;

    @ManyToOne
    @JoinColumn(name = "stack_id", nullable = false)
    private SigtranStack sigtranStack;
}
