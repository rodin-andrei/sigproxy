package com.unifun.sigproxy.models.config.sccp;

import com.unifun.sigproxy.models.config.SigtranStack;
import lombok.Data;
import lombok.ToString;
import org.restcomm.protocols.ss7.sccp.LongMessageRuleType;

import javax.persistence.*;

@Data
@Entity
public class SccpLongMessageRuleConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private int firstSignalingPointCode;

    private int lastSignalingPointCode;

    @Enumerated(EnumType.STRING)
    private LongMessageRuleType longMessageRuleType;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "stack_id", nullable = false)
    private SigtranStack sigtranStack;
}
