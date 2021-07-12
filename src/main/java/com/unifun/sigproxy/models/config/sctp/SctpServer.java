package com.unifun.sigproxy.models.config.sctp;

import com.unifun.sigproxy.models.config.SigtranStack;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(exclude = {"serverAssociations"})
@ToString(exclude = {"serverAssociations"})
public class SctpServer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String localAddress;

    private int localPort;

    private String[] multihomingAddresses;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "sctpServer")
    private Set<ServerAssociation> serverAssociations;

    @ManyToOne
    @JoinColumn(name = "stack_id", nullable = false)
    private SigtranStack sigtranStack;
}
