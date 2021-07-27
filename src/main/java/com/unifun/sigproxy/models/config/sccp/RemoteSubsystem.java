package com.unifun.sigproxy.models.config.sccp;

import com.unifun.sigproxy.models.config.SigtranStack;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class RemoteSubsystem {
    boolean markProhibitedWhenSpcResuming;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int remoteSignalingPointCode;
    private int remoteSubsystemNumber;
    private int remoteSubsystemFlag;
    @ManyToOne
    @JoinColumn(name = "stack_id", nullable = false)
    private SigtranStack sigtranStack;
}
