package com.unifun.sigproxy.controller.dto.sctp;

import lombok.Data;

@Data
public class SctpServerAssociationConfigDto {

    private Long id;
    private String linkName;
    private String remoteAddress;
    private int remotePort;
    private boolean status;

}