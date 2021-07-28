package com.unifun.sigproxy.models.config.sccp;

import com.unifun.sigproxy.models.config.SigtranStack;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
public class SccpRemoteSubsystemConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int remoteSignalingPointCode;

    private int remoteSubsystemNumber;

    private int remoteSubsystemFlag;

    boolean markProhibitedWhenSpcResuming;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "stack_id", nullable = false)
    private SigtranStack sigtranStack;
}
