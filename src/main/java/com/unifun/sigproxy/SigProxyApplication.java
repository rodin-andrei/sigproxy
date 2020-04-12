package com.unifun.sigproxy;

import javolution.util.FastList;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication(scanBasePackages = "com.unifun.sigproxy")
public class SigProxyApplication {
    public static void main(String[] args) {
        SpringApplication.run(SigProxyApplication.class, args);
    }

}
