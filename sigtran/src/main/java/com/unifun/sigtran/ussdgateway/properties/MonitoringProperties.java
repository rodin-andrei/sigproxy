package com.unifun.sigtran.ussdgateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @author a.rusu
 */
@Component
@ConfigurationProperties(prefix = "monitoring")
@Data
public class MonitoringProperties {

    @Max(100)
    @Min(0)
    private Byte lowTimePercent;

    @Max(100)
    @Min(0)
    private Byte highTimePercent;

}
