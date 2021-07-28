package com.unifun.sigproxy.models.config.sctp;

import com.unifun.sigproxy.models.config.SigtranStack;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
public class SctpServer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String localAddress;

    private int localPort;

    private String[] multihomingAddresses;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "sctpServer")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<SctpServerAssociationConfig> sctpServerAssociationConfigs;

    @ManyToOne
    @JoinColumn(name = "stack_id", nullable = false)
    private SigtranStack sigtranStack;
}
