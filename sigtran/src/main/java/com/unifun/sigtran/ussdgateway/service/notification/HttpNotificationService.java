package com.unifun.sigtran.ussdgateway.service.notification;

import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;

import java.util.List;

/**
 * @author asolopa
 */
public interface HttpNotificationService {

    /**
     * @param uriList
     * @param mediaType
     * @param httpMethod
     * @param body          - null if GET Request
     * @param getParameters - null if POST Request
     */
    void notifyClients(List<String> uriList,
                       String mediaType,
                       HttpMethod httpMethod,
                       String body,
                       MultiValueMap<String, String> getParameters);
}
