package com.unifun.sigproxy.models.config.sccp;

import com.unifun.sigproxy.models.config.SigtranStack;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
public class SccpMtp3DestinationConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int sapId;

    private int firstSignalingPointCode;

    private int lastSignalingPointCode;

    private int firstSls;

    private int lastSls;

    private int slsMask;


    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "stack_id", nullable = false)
    private SigtranStack sigtranStack;
}
