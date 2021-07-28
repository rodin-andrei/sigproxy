package com.unifun.sigproxy.models.config.sccp;

import com.unifun.sigproxy.models.config.SigtranStack;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
public class SccpServiceAccessPointConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer mtp3Id;

    private Integer opc;

    private Integer ni;

    private Integer networkId;

    private String localGlobalTitleDigits;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "stack_id", nullable = false)
    private SigtranStack sigtranStack;
}
