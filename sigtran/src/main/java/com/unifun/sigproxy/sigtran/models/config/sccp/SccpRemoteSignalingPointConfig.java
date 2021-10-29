package com.unifun.sigproxy.sigtran.models.config.sccp;

import com.unifun.sigproxy.sigtran.models.config.SigtranStack;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Data
@Entity
@DynamicInsert
public class SccpRemoteSignalingPointConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private int remoteSignalingPointCode;

    @Deprecated //From SS7_Stack_User_Guide 7.4.21.1
    @Column(columnDefinition = "int default 0")
    private int rspcFlag;

    @Deprecated //From SS7_Stack_User_Guide 7.4.21.1
    @Column(columnDefinition = "int default 0")
    private int mask;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "stack_id", nullable = false)
    private SigtranStack sigtranStack;
}
