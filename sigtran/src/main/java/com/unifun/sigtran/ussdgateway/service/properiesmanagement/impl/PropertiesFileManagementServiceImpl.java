package com.unifun.sigtran.ussdgateway.service.properiesmanagement.impl;

import com.unifun.sigtran.ussdgateway.service.properiesmanagement.PropertiesFileManagementService;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * @author asolopa
 */
@Service
@Log4j2
public class PropertiesFileManagementServiceImpl implements PropertiesFileManagementService {

    @Override
    @Async
    public CompletableFuture<Properties> getApplicationProperties() {
        try (final FileInputStream fileInputStream = new FileInputStream(System.getProperty("spring.config.location"))) {

            final Properties properties = new Properties();
            properties.load(fileInputStream);
            return CompletableFuture.completedFuture(properties);

        } catch (IOException e) {
            e.printStackTrace();
            final var properties = new Properties();
            properties.setProperty("error", e.getMessage());
            return CompletableFuture.completedFuture(properties);
        }
    }

    @Override
    @Async
    public <T> CompletableFuture<T> persist(T propertiesObject) {
        log.info("Trying to persist configuration");
        String propertyPath = System.getProperty("spring.config.location");
        Class<?> propertiesClass = propertiesObject.getClass();
        SortedProperties properties = new SortedProperties();

        String propertyPrefix = "";
        propertyPrefix = this.getPropertyPrefix(propertiesClass, propertyPrefix);

        try (final FileInputStream fileInputStream = new FileInputStream(propertyPath)) {
            properties.load(fileInputStream);
            this.rewritePropertiesWithCurrentValues(propertiesObject, propertiesClass, propertyPrefix, properties);

            try (final FileOutputStream fileOutputStream = new FileOutputStream(propertyPath)) {
                properties.store(fileOutputStream, "Write only in \"some-property-example\" format");
            }
            return CompletableFuture.completedFuture(propertiesObject);
        } catch (IOException | IllegalAccessException e) {
            log.error("Exception while " + propertiesClass.getName() + "  persistence", e);
            return CompletableFuture.completedFuture(propertiesObject);

        }

    }

    private <T> void rewritePropertiesWithCurrentValues(T fileConfig,
                                                        Class<?> configClass,
                                                        String propertyPrefix,
                                                        Properties properties) throws IllegalAccessException {
        for (Field field : configClass.getDeclaredFields()) {
            field.setAccessible(true);
            String propertyKey = this.getProperlyFormattedPropertyName(field);
            String propertyValue = String.valueOf(field.get(fileConfig));
            properties.setProperty(propertyPrefix + propertyKey, propertyValue.replaceAll("[\\[\\]]", ""));
            field.setAccessible(false);
        }
    }

    private String getPropertyPrefix(Class<?> configClass, String propertyPrefix) {
        for (Annotation annotation : configClass.getAnnotations()) {
            if (ConfigurationProperties.class.equals(annotation.annotationType())) {
                propertyPrefix = ((ConfigurationProperties) annotation).prefix() + ".";
                break;
            }
        }
        return propertyPrefix;
    }

    private String getProperlyFormattedPropertyName(Field field) {
        StringBuilder propertyKey = new StringBuilder();
        final String fieldName = field.getName();
        for (int i = 0; i < fieldName.length(); i++) {
            propertyKey.append(Character.isLowerCase(fieldName.charAt(i))
                    ? fieldName.charAt(i)
                    : "-" + Character.toLowerCase(fieldName.charAt(i)));
        }
        return propertyKey.toString();
    }

    private static class SortedProperties extends Properties {

        @Override
        public Set<Map.Entry<Object, Object>> entrySet() {
            /*
             * Using comparator to avoid the following exception on jdk >=9:
             * java.lang.ClassCastException: java.base/java.util.concurrent.ConcurrentHashMap$MapEntry cannot be cast to java.base/java.lang.Comparable
             */
            Set<Map.Entry<Object, Object>> sortedSet = new TreeSet<>(Comparator.comparing(o -> o.getKey().toString()));
            sortedSet.addAll(super.entrySet());
            return sortedSet;
        }

        @Override
        public Set<Object> keySet() {
            return new TreeSet<>(super.keySet());
        }

        @Override
        public synchronized Enumeration<Object> keys() {
            return Collections.enumeration(new TreeSet<Object>(super.keySet()));
        }

    }
}
