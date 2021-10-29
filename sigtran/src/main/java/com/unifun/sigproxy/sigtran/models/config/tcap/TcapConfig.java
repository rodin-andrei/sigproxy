package com.unifun.sigproxy.sigtran.models.config.tcap;

import com.unifun.sigproxy.sigtran.models.config.SigtranStack;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
public class TcapConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int localSsn;

    private int[] additionalSsns = new int[0];

    @OneToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "stack_id", nullable = false)
    private SigtranStack sigtranStack;
}
