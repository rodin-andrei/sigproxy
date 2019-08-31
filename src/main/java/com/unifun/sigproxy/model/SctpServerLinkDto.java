package com.unifun.sigproxy.model;

import lombok.Data;

@Data
public class SctpServerLinkDto {
    private String linkName;
    private String remoteAddress;
    private int remotePort;
    private String status;

}
