package com.unifun.sigproxy.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unifun.sigproxy.model.config.*;
import com.unifun.sigproxy.model.config.m3ua.M3uaConfig;
import com.unifun.sigproxy.model.config.sccp.SccpConfig;
import com.unifun.sigproxy.model.config.sctp.SctpConfig;
import com.unifun.sigproxy.model.config.tcap.TcapConfig;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Repository
@PropertySource("classpath:application.properties")
@RequiredArgsConstructor
public class SigtranRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(SigtranRepository.class);

    @Value("${sigtran.file.path}")
    private String jsonConfigPath;

    @Getter
    private SigtranConfig sigtranConfig;
    @Getter
    private SctpConfig sctpConfig;
    @Getter
    private M3uaConfig m3uaConfig;

    public void init() {
        try {
            Path path = Paths.get(jsonConfigPath);
            String resultString = String.join("", Files.readAllLines(path, StandardCharsets.UTF_8));
            ObjectMapper objectMapper = new ObjectMapper();
            sigtranConfig = objectMapper.readValue(resultString, SigtranConfig.class);
        } catch (IOException e) {
            LOGGER.error("IOException on getting sigtran_config. ", e);
            sigtranConfig = new SigtranConfig();
            sigtranConfig.setSctpConfig(new SctpConfig());
            sigtranConfig.setM3uaConfig(new M3uaConfig());
            sigtranConfig.setSccpConfig(new SccpConfig());
            sigtranConfig.setTcapConfig(new TcapConfig());
        }
        this.sctpConfig = this.sigtranConfig.getSctpConfig();
        this.m3uaConfig = this.sigtranConfig.getM3uaConfig();
    }
}
