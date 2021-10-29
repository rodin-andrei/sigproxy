package com.unifun.sigtran.ussdgateway.restcontroller;

import com.unifun.sigtran.ussdgateway.properties.AppProperties;
import com.unifun.sigtran.ussdgateway.properties.MonitoringProperties;
import com.unifun.sigtran.ussdgateway.properties.notification.DialogTimeoutProperties;
import com.unifun.sigtran.ussdgateway.service.properiesmanagement.PropertiesManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author asolopa
 */
@RestController
@RequestMapping("application-properties")
@RequiredArgsConstructor
@Log4j2
public class PropertiesManagementRestController {
    private static final String REQUEST_EXECUTION_INTERRUPTED = "Request execution interrupted";
    private final PropertiesManagementService propertiesService;

    @GetMapping("/current")
    public Properties getApplicationProperties() {
        try {
            return propertiesService.getApplicationProperties().get(10, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.error(REQUEST_EXECUTION_INTERRUPTED, e);
            Thread.currentThread().interrupt();
            return new Properties();
        }
    }

    @PostMapping("/set-app")
    public AppProperties setAppProperties(@RequestBody @Valid AppProperties properties) {
        try {
            return propertiesService.updateApplicationProperties(properties).get(10, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.error(REQUEST_EXECUTION_INTERRUPTED, e);
            Thread.currentThread().interrupt();
            return new AppProperties();
        }
    }

    @PostMapping("/set-monitoring")
    public MonitoringProperties setMonitoringProperties(@RequestBody @Valid MonitoringProperties properties) {
        try {
            return propertiesService.updateMonitoringProperties(properties).get(10, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.error(REQUEST_EXECUTION_INTERRUPTED, e);
            Thread.currentThread().interrupt();
            return new MonitoringProperties();
        }
    }

    @PostMapping("/set-notification-dialog-timeout")
    public DialogTimeoutProperties setDialogTimeoutProperties(@RequestBody @Valid DialogTimeoutProperties properties) {
        try {
            return propertiesService.updateDialogTimeoutNotificationList(properties).get(10, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.error(REQUEST_EXECUTION_INTERRUPTED, e);
            Thread.currentThread().interrupt();
            return new DialogTimeoutProperties();
        }
    }

    @PostMapping("/add-to-notification-dialog-timeout-list")
    public DialogTimeoutProperties setDialogTimeoutProperties(@RequestBody List<String> list) {
        try {
            return propertiesService.updateDialogTimeoutNotificationList(list).get(10, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.error(REQUEST_EXECUTION_INTERRUPTED, e);
            Thread.currentThread().interrupt();
            return new DialogTimeoutProperties();
        }
    }

}
