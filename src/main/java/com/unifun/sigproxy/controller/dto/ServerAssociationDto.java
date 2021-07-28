package com.unifun.sigproxy.controller.dto;

import lombok.Data;

@Data
public class ServerAssociationDto {

    private Long id;

    private String linkName;

    private String remoteAddress;

    private int remotePort;

}
