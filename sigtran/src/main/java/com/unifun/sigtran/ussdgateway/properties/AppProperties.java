package com.unifun.sigtran.ussdgateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;


/**
 * @author asolopa
 */
@Component
@ConfigurationProperties(prefix = "app")
@Data
public class AppProperties {
    private Boolean sendAbortTimeout = false;
    private Boolean takeInvokeIdFromJson = false;
    @Min(1025)
    @Max(65535)
    private Integer embeddedTomcatPort = 7080;
    private Integer embeddedTomcatConnectionTimeout = 6000;
    private Integer embeddedTomcatMaxConnections = 1000;
    private Integer ussdServletTimeout = 2000;
    private String masterConfFile = "./master.conf";
    private String homeDirPath = "/opt/unifun/sigtran/unifun-gateway";
    private String ocsJsonPath = "/opt/unifun/sigtran/unifun-gateway/conf";
    private String ocsJsonFileNamePrefix = "ocs";
    private int customHttpChannelWebClientMaxConnections = 500;
    private int customHttpChannelWebClientPendingAcquireMaxCount = 1500;
    private int customHttpChannelWebClientResponseTimeout = 3;
}
