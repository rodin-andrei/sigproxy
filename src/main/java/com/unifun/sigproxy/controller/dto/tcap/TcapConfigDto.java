package com.unifun.sigproxy.controller.dto.tcap;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TcapConfigDto {

    private Long id;

    private int localSsn;

    private int[] additionalSsns = new int[0];

}
