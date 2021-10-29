package com.unifun.sigtran.ussdgateway.properties.notification;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author asolopa
 */
@Component
@ConfigurationProperties(prefix = "notification.dialog.timeout")
@Data
public class DialogTimeoutProperties {
    private List<String> urlList = new ArrayList<>();
}
