package com.unifun.sigproxy.sigtran.models.config.sccp;

import lombok.Data;
import org.restcomm.protocols.ss7.indicator.NatureOfAddress;
import org.restcomm.protocols.ss7.indicator.NumberingPlan;

import javax.persistence.*;

@Data
@Entity
public class SccpAddressRuleConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Byte addressIndicator;

    private Integer pointCode;

    private Integer ssn;

    private Integer translationType;

    @Enumerated(EnumType.STRING)
    private NumberingPlan numberingPlan;

    @Enumerated(EnumType.STRING)
    private NatureOfAddress natureOfAddress;

    private String digits;
}
