package com.unifun.sigproxy.models.config.sctp;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
}
