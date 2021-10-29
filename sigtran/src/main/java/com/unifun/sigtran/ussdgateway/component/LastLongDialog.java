package com.unifun.sigtran.ussdgateway.component;

import com.unifun.sigtran.ussdgateway.properties.MonitoringProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

/**
 * @author a.rusu
 */
@Component
@Setter
@Getter
@RequiredArgsConstructor
public class LastLongDialog {
    private final MonitoringProperties monitoringProperties;
    private LocalDateTime processedDateTime = LocalDateTime.now().minusYears(1);
    private int lowTimePercent;
    private int highTimePercent;

    @PostConstruct
    void initialize() {
        lowTimePercent = monitoringProperties.getLowTimePercent();
        highTimePercent = monitoringProperties.getHighTimePercent();
    }
}
