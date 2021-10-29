package com.unifun.sigtran.ussdgateway.service.properiesmanagement;

import java.util.Properties;
import java.util.concurrent.CompletableFuture;

/**
 * @author asolopa
 */
public interface PropertiesFileManagementService {

    CompletableFuture<Properties> getApplicationProperties();

    <T> CompletableFuture<T> persist(T configuration);
}
