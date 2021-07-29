package com.unifun.sigproxy.models.config.sccp;

import com.unifun.sigproxy.models.config.SigtranStack;
import lombok.Data;
import lombok.ToString;
import org.restcomm.protocols.ss7.indicator.NatureOfAddress;
import org.restcomm.protocols.ss7.indicator.NumberingPlan;

import javax.persistence.*;

@Data
@Entity
public class SccpAddressConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Byte addressIndicator;

    private Integer pointCode;

    private Integer ssn;

    private Integer translationType;

    @Enumerated(EnumType.STRING)
    private NumberingPlan numberingPlan;

    @Enumerated(EnumType.STRING)
    private NatureOfAddress natureOfAddress;

    private String digits;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "stack_id", nullable = false)
    private SigtranStack sigtranStack;
}
