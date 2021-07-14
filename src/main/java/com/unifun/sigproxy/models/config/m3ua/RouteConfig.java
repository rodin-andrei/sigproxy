package com.unifun.sigproxy.models.config.m3ua;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class RouteConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int opc;

    private int dpc;

    private int ssn;

    @ManyToOne
    @JoinColumn(name = "as_id", nullable = false)
    private AsConfig as;

    @Enumerated(EnumType.STRING)
    private TrafficModeType trafficModeType;

}
