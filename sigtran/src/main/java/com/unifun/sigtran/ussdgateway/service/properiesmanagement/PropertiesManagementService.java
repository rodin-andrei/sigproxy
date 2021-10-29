package com.unifun.sigtran.ussdgateway.service.properiesmanagement;

import com.unifun.sigtran.ussdgateway.properties.AppProperties;
import com.unifun.sigtran.ussdgateway.properties.MonitoringProperties;
import com.unifun.sigtran.ussdgateway.properties.notification.DialogTimeoutProperties;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

/**
 * @author asolopa
 */
public interface PropertiesManagementService {
    CompletableFuture<Properties> getApplicationProperties();

    CompletableFuture<MonitoringProperties> updateMonitoringProperties(MonitoringProperties properties);

    CompletableFuture<AppProperties> updateApplicationProperties(AppProperties properties);

    CompletableFuture<DialogTimeoutProperties> updateDialogTimeoutNotificationList(DialogTimeoutProperties properties);

    CompletableFuture<DialogTimeoutProperties> updateDialogTimeoutNotificationList(List<String> newNotificationEndpoints);


}
