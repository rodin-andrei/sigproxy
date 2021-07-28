package com.unifun.sigproxy.models.config.m3ua;

import com.unifun.sigproxy.models.config.SigtranStack;
import lombok.Data;
import lombok.ToString;
import org.restcomm.protocols.ss7.mtp.RoutingLabelFormat;

import javax.persistence.*;

@Entity
@Data
public class M3uaStackSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int deliveryMessageThreadCount;

    private int heartbeatTime;

    private int MaxAsForRoute;

    private int maxSequenceNumber;

    private boolean routingKeyManagementEnabled;

    @Enumerated(EnumType.STRING)
    private RoutingLabelFormat routingLabelFormat;

    private boolean statisticsEnabled;

    private boolean useLsbForLinksetSelection;

    @OneToOne
    @ToString.Exclude
    @JoinColumn(name = "stack_id", nullable = false)
    private SigtranStack sigtranStack;
}
