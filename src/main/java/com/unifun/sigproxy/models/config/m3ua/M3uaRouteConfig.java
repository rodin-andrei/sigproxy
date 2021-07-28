package com.unifun.sigproxy.models.config.m3ua;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
public class M3uaRouteConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int opc;

    private int dpc;

    private int ssn;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "as_id", nullable = false)
    private M3uaAsConfig as;

    @Enumerated(EnumType.STRING)
    private TrafficModeType trafficModeType;

}
