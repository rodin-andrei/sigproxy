package com.unifun.sigproxy.json.model.dto;

import lombok.Data;

@Data
public class SctpServerLinkDto {
    private String linkName;
    private String remoteAddress;
    private int remotePort;
    private boolean up;
    private boolean started;
}
