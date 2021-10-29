/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.gw;

import com.unifun.sigtran.ussdgateway.gw.config.JsonConfiguration;
import com.unifun.sigtran.ussdgateway.properties.AppProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

/**
 * @author okulikov
 */
@Log4j2
@Component
@RequiredArgsConstructor
@Getter
public class Mobile {

    private final String NAME = "unifun";
    private final AppProperties properties;
    private final JsonConfiguration config;
}
