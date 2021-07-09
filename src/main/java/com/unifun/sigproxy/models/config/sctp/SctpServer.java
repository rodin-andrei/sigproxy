package com.unifun.sigproxy.models.config.sctp;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(exclude = {"serverLinks"})
@ToString(exclude = {"serverLinks"})
public class SctpServer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String serverName;

    private String localAddress;

    private int localPort;

    private String[] multihomingAddresses;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "sctpServer")
    private Set<ServerLink> serverLinks;

}
