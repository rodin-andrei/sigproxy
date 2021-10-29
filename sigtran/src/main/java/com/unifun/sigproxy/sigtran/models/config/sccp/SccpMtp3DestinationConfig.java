package com.unifun.sigproxy.sigtran.models.config.sccp;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
public class SccpMtp3DestinationConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "sap_id", nullable = false)
    private SccpServiceAccessPointConfig sccpServiceAccessPointConfig;

    private int firstSignalingPointCode;

    private int lastSignalingPointCode;

    private int firstSls;

    private int lastSls;

    private int slsMask;

}
