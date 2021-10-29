package com.unifun.sigtran.ussdgateway.service.properiesmanagement.impl;

import com.unifun.sigtran.ussdgateway.properties.AppProperties;
import com.unifun.sigtran.ussdgateway.properties.MonitoringProperties;
import com.unifun.sigtran.ussdgateway.properties.notification.DialogTimeoutProperties;
import com.unifun.sigtran.ussdgateway.service.properiesmanagement.PropertiesFileManagementService;
import com.unifun.sigtran.ussdgateway.service.properiesmanagement.PropertiesManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

/**
 * @author asolopa
 */
@Service
@RequiredArgsConstructor
public class PropertiesManagementServiceImpl implements PropertiesManagementService {
    private final PropertiesFileManagementService fileManagementService;
    private final MonitoringProperties monitoringProperties;
    private final AppProperties appProperties;
    private final DialogTimeoutProperties dialogTimeoutProperties;

    public CompletableFuture<Properties> getApplicationProperties() {
        return fileManagementService.getApplicationProperties();
    }

    public CompletableFuture<MonitoringProperties> updateMonitoringProperties(MonitoringProperties properties) {
        if (properties.getLowTimePercent() != null) {
            monitoringProperties.setLowTimePercent(properties.getLowTimePercent());
        }
        if (properties.getHighTimePercent() != null) {
            monitoringProperties.setHighTimePercent(properties.getHighTimePercent());
        }
        return fileManagementService.persist(monitoringProperties);
    }

    public CompletableFuture<AppProperties> updateApplicationProperties(AppProperties properties) {
        if (properties.getEmbeddedTomcatMaxConnections() != null) {
            appProperties.setEmbeddedTomcatMaxConnections(properties.getEmbeddedTomcatMaxConnections());
        }
        if (properties.getEmbeddedTomcatConnectionTimeout() != null) {
            appProperties.setEmbeddedTomcatConnectionTimeout(properties.getEmbeddedTomcatConnectionTimeout());
        }
        if (properties.getEmbeddedTomcatPort() != null) {
            appProperties.setEmbeddedTomcatPort(properties.getEmbeddedTomcatPort());
        }
        if (properties.getSendAbortTimeout() != null) {
            appProperties.setSendAbortTimeout(properties.getSendAbortTimeout());
        }
        if (properties.getTakeInvokeIdFromJson() != null) {
            appProperties.setTakeInvokeIdFromJson(properties.getTakeInvokeIdFromJson());
        }

        return fileManagementService.persist(appProperties);
    }

    public CompletableFuture<DialogTimeoutProperties> updateDialogTimeoutNotificationList(DialogTimeoutProperties properties) {
        if (properties.getUrlList() != null) {
            dialogTimeoutProperties.setUrlList(properties.getUrlList());
        }
        return fileManagementService.persist(dialogTimeoutProperties);
    }

    public CompletableFuture<DialogTimeoutProperties> updateDialogTimeoutNotificationList(List<String> newNotificationEndpoints) {
        if (newNotificationEndpoints != null) {
            dialogTimeoutProperties.getUrlList().addAll(newNotificationEndpoints);
        }
        return fileManagementService.persist(dialogTimeoutProperties);
    }

}
