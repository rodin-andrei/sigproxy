package com.unifun.sigproxy.models.config.sccp;

import org.restcomm.protocols.ss7.indicator.NatureOfAddress;
import org.restcomm.protocols.ss7.indicator.NumberingPlan;

import javax.persistence.*;

@Entity
public class SccpAddressRuleConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer addressIndicator;

    private Integer pointCode;

    private Integer ssn;

    @Deprecated
    private Integer translationType;

    @Enumerated(EnumType.STRING)
    private NumberingPlan numberingPlan;

    @Enumerated(EnumType.STRING)
    private NatureOfAddress natureOfAddress;

    private String digits;
}
