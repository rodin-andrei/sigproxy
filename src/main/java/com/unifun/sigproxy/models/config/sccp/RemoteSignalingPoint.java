package com.unifun.sigproxy.models.config.sccp;

import com.unifun.sigproxy.models.config.SigtranStack;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class RemoteSignalingPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int dpc;

    private int rspcFlag;

    private int mask;

    @ManyToOne
    @JoinColumn(name = "stack_id", nullable = false)
    private SigtranStack sigtranStack;
}
