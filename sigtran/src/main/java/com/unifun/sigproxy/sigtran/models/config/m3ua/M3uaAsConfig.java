package com.unifun.sigproxy.sigtran.models.config.m3ua;

import com.unifun.sigproxy.sigtran.models.config.SigtranStack;
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
public class M3uaAsConfig {
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

    @ManyToMany(mappedBy = "applicationServers", fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<M3uaAspConfig> applicationServerPoints;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "as")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<M3uaRouteConfig> routes;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "stack_id", nullable = false)
    private SigtranStack sigtranStack;

}
