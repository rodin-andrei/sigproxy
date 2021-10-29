package com.unifun.sigproxy.sigtran.controller.dto.sctp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SctpServerAssociationConfigDto {

    private Long id;
    private String linkName;
    private String remoteAddress;
    private int remotePort;
    private boolean status;

}
