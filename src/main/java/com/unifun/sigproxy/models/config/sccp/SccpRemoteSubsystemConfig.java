package com.unifun.sigproxy.models.config.sccp;

import com.unifun.sigproxy.models.config.SigtranStack;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Data
@Entity
@DynamicInsert
public class SccpRemoteSubsystemConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private int remoteSignalingPointCode;

    private int remoteSubsystemNumber;

    @Deprecated //From SS7_Stack_User_Guide 7.4.25.1
    @Column(columnDefinition = "int default 0")
    private int remoteSubsystemFlag;

    boolean markProhibitedWhenSpcResuming;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "stack_id", nullable = false)
    private SigtranStack sigtranStack;
}
