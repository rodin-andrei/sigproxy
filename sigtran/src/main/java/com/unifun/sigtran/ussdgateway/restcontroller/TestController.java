package com.unifun.sigtran.ussdgateway.restcontroller;

import com.unifun.sigtran.ussdgateway.gw.Gateway;
import com.unifun.sigtran.ussdgateway.properties.AppProperties;
import com.unifun.sigtran.ussdgateway.properties.notification.DialogTimeoutProperties;
import com.unifun.sigtran.ussdgateway.service.notification.HttpNotificationService;
import com.unifun.sigtran.ussdgateway.service.properiesmanagement.PropertiesFileManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author asolopa
 */
@RestController
@RequiredArgsConstructor
public class TestController {
    private final AppProperties appProperties;
    private final PropertiesFileManagementService propertiesFileManagementService;
    private final HttpNotificationService notifyClients;
    private final DialogTimeoutProperties timeoutProperties;
    private final Gateway gateway;


    @GetMapping(path = "/test-web-client")
    public void getAsdas() {
        MultiValueMap<String, String> objectObjectMultiValueMap = new LinkedMultiValueMap<>();
        objectObjectMultiValueMap.add("request", "tets");
        notifyClients.notifyClients(timeoutProperties.getUrlList(),
                MediaType.APPLICATION_JSON_VALUE,
                HttpMethod.GET,
                null,
                objectObjectMultiValueMap);

    }

    @GetMapping("/save-config")
    public void saveConfiguration() throws IOException {
        gateway.getConfig().write();
    }

    @GetMapping("/get-config")
    public String getConfig() {
//        appProperties.setSendAbortTimeout(true);
        return "";
    }

}
