/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.gw.config;

import com.unifun.sigtran.ussdgateway.properties.AppProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.json.*;
import javax.json.stream.JsonGenerator;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author okulikov
 */
@Component
@RequiredArgsConstructor
@Log4j2
@Getter
public class JsonConfiguration {
    private final AppProperties properties;
    private UssdConfig ussdConfig;
    private DialogConfig dialogConfig;

    @PostConstruct
    public void read() throws FileNotFoundException {
        try (JsonReader reader = Json.createReader(new FileInputStream(properties.getMasterConfFile()))) {
            JsonObject cfg = reader.readObject();


            ussdConfig = cfg.getJsonObject("ussd") != null
                    ? new UssdConfig(cfg.getJsonObject("ussd"))
                    : new UssdConfig();
            dialogConfig = cfg.getJsonObject("dialog-config") != null
                    ? new DialogConfig(cfg.getJsonObject("dialog-config"))
                    : new DialogConfig();
        }
    }

    public void write() throws IOException {
        JsonObjectBuilder builder = Json.createObjectBuilder();

        HashMap<String, Boolean> props = new HashMap<>();
        props.put(JsonGenerator.PRETTY_PRINTING, true);

        JsonWriterFactory factory = Json.createWriterFactory(props);

        FileOutputStream fileOut = new FileOutputStream(properties.getMasterConfFile());
        try (JsonWriter writer = factory.createWriter(fileOut)) {
            builder.add("ussd", ussdConfig.toJson());
            builder.add("dialog-config", dialogConfig.toJson());
            writer.writeObject(builder.build());
        }
    }
}
