package com.unifun.sigtran.ussdgateway;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

/**
 * @author asolopa
 */
@Configuration
@EnableAsync(proxyTargetClass = true)
@EnableConfigurationProperties
public class UssdGatewayConfiguration {

    /**
     * For support additional format(ex. List) of properties file, ex. app.http.dialog.timeout.url-list=http://127.0.0.1:8888,http://127.0.0.1:8888
     * \@Value("${app.http.dialog.timeout.urlList}")
     * private final List<String> urlList = new ArrayList<>();
     */
    @Bean
    public ConversionService conversionService() {
        return new DefaultConversionService();
    }

    @Bean
    public WebClient notifyClientsWebClient() {
        HttpClient httpClient = HttpClient.create().responseTimeout(Duration.ofSeconds(3));
        return WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient)).build();
    }
}
