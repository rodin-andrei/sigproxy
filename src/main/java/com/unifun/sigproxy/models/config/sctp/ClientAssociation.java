package com.unifun.sigproxy.models.config.sctp;

import com.unifun.sigproxy.models.config.SigtranStack;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class ClientAssociation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String linkName;

    private String remoteAddress;

    private int remotePort;

    private String localAddress;

    private int localPort;

    private String[] multihomingAddresses;

    @ManyToOne
    @JoinColumn(name = "stack_id", nullable = false)
    private SigtranStack sigtranStack;
}
