package com.unifun.sigproxy.models.config.sccp;

import com.unifun.sigproxy.models.config.SigtranStack;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Mtp3Destination {
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
    @JoinColumn(name = "stack_id", nullable = false)
    private SigtranStack sigtranStack;
}
