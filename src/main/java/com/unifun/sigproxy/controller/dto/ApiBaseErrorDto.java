package com.unifun.sigproxy.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiBaseErrorDto {
    private String patch;
    private int code;
    private String type;
    private String detail;
}
