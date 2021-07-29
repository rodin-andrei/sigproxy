package com.unifun.sigproxy.models.config.sccp;

import com.unifun.sigproxy.models.config.SigtranStack;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@DynamicInsert
public class SccpServiceAccessPointConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "int default 1")
    private Integer mtp3Id;

    private Integer opc;

    @Column(nullable = false)
    private Integer ni;

    @Column(columnDefinition = "int default 0")
    private Integer networkId;

    private String localGlobalTitleDigits;


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "sccpServiceAccessPointConfig")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<SccpMtp3DestinationConfig> sccpMtp3DestinationConfigs;


    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "stack_id", nullable = false)
    private SigtranStack sigtranStack;
}
