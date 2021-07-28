package com.unifun.sigproxy.models.config.sccp;

import com.unifun.sigproxy.models.config.SigtranStack;
import lombok.Data;
import lombok.ToString;
import org.restcomm.protocols.ss7.sccp.LoadSharingAlgorithm;
import org.restcomm.protocols.ss7.sccp.OriginationType;
import org.restcomm.protocols.ss7.sccp.RuleType;

import javax.persistence.*;

@Data
@Entity
public class SccpRuleConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mask;

    @OneToOne
    @JoinColumn(name = "address_id", nullable = false)
    private SccpAddressRuleConfig sccpAddressRuleConfigs;

    @Enumerated(EnumType.STRING)
    private RuleType ruleType;

    private Integer primaryAddressId;


    //Optional parameters
    @Enumerated(EnumType.STRING)
    private LoadSharingAlgorithm loadSharingAlgorithm = LoadSharingAlgorithm.Undefined;

    @Enumerated(EnumType.STRING)
    private OriginationType originationType = OriginationType.ALL;

    private Integer secondaryAddressId = -1;

    private Integer newCallingPartyAddressAddressId;

    private Integer networkId = 0;

    @OneToOne
    @JoinColumn(name = "calling_address_id", nullable = false)
    private SccpAddressRuleConfig callingSccpAddressRuleConfig;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "stack_id", nullable = false)
    private SigtranStack sigtranStack;
}
