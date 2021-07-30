package com.unifun.sigproxy.models.config.sctp;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class SctpServerAssociationConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String linkName;

    private String remoteAddress;

    private int remotePort;

    @ManyToOne
    @JoinColumn(name = "server_id", nullable = false)
    private SctpServerConfig sctpServerConfig;
}
