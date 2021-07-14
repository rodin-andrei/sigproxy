package com.unifun.sigproxy.models.config.m3ua;

import com.unifun.sigproxy.models.config.SigtranStack;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.restcomm.protocols.ss7.m3ua.ExchangeType;
import org.restcomm.protocols.ss7.m3ua.Functionality;
import org.restcomm.protocols.ss7.m3ua.IPSPType;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
public class AsConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Functionality functionality;

    @Enumerated(EnumType.STRING)
    private ExchangeType exchangeType;

    @Enumerated(EnumType.STRING)
    private IPSPType ipspType;

    @Enumerated(EnumType.STRING)
    private TrafficModeType trafficModeType;

    private int networkIndicator;

    private int networkAppearance;

    private long[] routingContexts;

    @ManyToMany(mappedBy = "applicationServers")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<AspConfig> applicationServerPoints;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "as")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<RouteConfig> routes;

    @ManyToOne
    @JoinColumn(name = "stack_id", nullable = false)
    private SigtranStack sigtranStack;

}
