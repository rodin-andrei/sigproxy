package com.unifun.sigproxy.controller.dto.sccp;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@Builder
public class SccpMtp3DestinationConfigDto {

    private Integer id;
    private int firstSignalingPointCode;
    private int lastSignalingPointCode;
    private int firstSls;
    private int lastSls;
    private int slsMask;

}
