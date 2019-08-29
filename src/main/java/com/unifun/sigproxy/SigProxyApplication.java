package com.unifun.sigproxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.unifun.sigproxy")
public class SigProxyApplication {
    public static void main(String[] args) {
        SpringApplication.run(SigProxyApplication.class, args);
    }

}
