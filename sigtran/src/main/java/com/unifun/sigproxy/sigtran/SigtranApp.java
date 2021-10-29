package com.unifun.sigproxy.sigtran;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author arodin
 */
@SpringBootApplication(scanBasePackages = "com.unifun")
public class  SigtranApp {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SigtranApp.class, args);
        context.start();
    }
}
