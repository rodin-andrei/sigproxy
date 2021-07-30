package com.unifun.sigproxy.models.config.m3ua;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Data
@Entity
@DynamicInsert
public class M3uaRouteConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int opc;

    private int dpc;


    @Column(columnDefinition = "int default -1")
    private int si;

    /**
     * <a href="https://datatracker.ietf.org/doc/html/rfc4666#section-3.4.5"><h3>RFC-4666</h3></a>
     * <pre>MTP3-User Identity field: 16 bits (unsigned integer)
     *
     *       The MTP3-User Identity describes the specific MTP3-User that is
     *       unavailable (e.g., ISUP, SCCP, etc.).  Some of the valid values
     *       for the MTP3-User Identity are shown below.  The values align with
     *       those provided in the SS7 MTP3 User Part Unavailable message and
     *       Service Indicator.  Depending on the MTP3 protocol variant/version
     *       used in the Network Appearance, additional values may be used.
     *       The relevant MTP3 protocol variant/version recommendation is
     *       definitive.
     *
     *           0 to 2   Reserved
     *              3     SCCP
     *              4     TUP
     *              5     ISUP
     *           6 to 8   Reserved
     *              9     Broadband ISUP
     *             10     Satellite ISUP
     *             11     Reserved
     *             12     AAL type 2 Signalling
     *             13     Bearer Independent Call Control (BICC)
     *             14     Gateway Control Protocol
     *             15     Reserved</pre>
     *
     * @param si Service Indicator
     */
    public void setSi(int si) {
        this.si = si;
    }

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "as_id", nullable = false)
    private M3uaAsConfig as;

    @Enumerated(EnumType.STRING)
    private TrafficModeType trafficModeType;

}
