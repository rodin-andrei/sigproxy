package com.unifun.sigproxy.models.config.sccp;

import com.unifun.sigproxy.models.config.SigtranStack;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.restcomm.protocols.ss7.sccp.LoadSharingAlgorithm;
import org.restcomm.protocols.ss7.sccp.OriginationType;
import org.restcomm.protocols.ss7.sccp.RuleType;

import javax.persistence.*;

@Data
@Entity
@DynamicInsert
public class SccpRuleConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //TODO change to ENUM
    private String mask;

    @OneToOne
    @JoinColumn(name = "address_id", nullable = false)
    private SccpAddressRuleConfig sccpAddressRuleConfig;

    @Enumerated(EnumType.STRING)
    private RuleType ruleType;

    private Integer primaryAddressId;


    //Optional parameters
    @Enumerated(EnumType.STRING)
    private LoadSharingAlgorithm loadSharingAlgorithm = LoadSharingAlgorithm.Undefined;

    @Enumerated(EnumType.STRING)
    private OriginationType originationType = OriginationType.ALL;

    @Column(columnDefinition = "int default -1")
    private Integer secondaryAddressId;

    private Integer newCallingPartyAddressAddressId;

    @Column(columnDefinition = "int default 0")
    private Integer networkId;

    @OneToOne
    @JoinColumn(name = "calling_address_id")
    private SccpAddressRuleConfig callingSccpAddressRuleConfig;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "stack_id", nullable = false)
    private SigtranStack sigtranStack;
}
