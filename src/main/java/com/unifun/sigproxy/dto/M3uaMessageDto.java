package com.unifun.sigproxy.dto;

import lombok.Data;

@Data
public class M3uaMessageDto {
    private int opc;
    private int dpc;
    private int si;
    private int ni;
    private int sls;
    private int mp;
    private String data;
}
