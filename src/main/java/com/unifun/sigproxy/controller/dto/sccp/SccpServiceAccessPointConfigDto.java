package com.unifun.sigproxy.controller.dto.sccp;

import com.unifun.sigproxy.controller.dto.SigtranStackDto;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.Set;

@Data
@Builder
public class SccpServiceAccessPointConfigDto {

    private Integer id;
    private Integer mtp3Id;
    private Integer opc;
    private Integer ni;
    private Integer networkId;
    private String localGlobalTitleDigits;
    private Set<SccpMtp3DestinationConfigDto> sccpMtp3DestinationConfigs;
}
