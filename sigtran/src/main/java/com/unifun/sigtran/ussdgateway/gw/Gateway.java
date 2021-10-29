/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.gw;

import com.unifun.sigproxy.sigtran.service.m3ua.M3uaService;
import com.unifun.sigproxy.sigtran.service.map.MapService;
import com.unifun.sigproxy.sigtran.service.sccp.SccpService;
import com.unifun.sigproxy.sigtran.service.tcap.TcapService;
import com.unifun.sigtran.ussdgateway.gw.config.JsonConfiguration;
import com.unifun.sigtran.ussdgateway.gw.mgmt.GatewayShell;
import com.unifun.sigtran.ussdgateway.gw.stats.UsageStats;
import com.unifun.sigtran.ussdgateway.properties.AppProperties;
import com.unifun.sigtran.ussdgateway.util.URL;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author okulikov
 */
@Component
@Getter
@Setter
@Log4j2
@RequiredArgsConstructor
public class Gateway {

    private static final Logger LOG_DIALOG_COUNT = LoggerFactory.getLogger("DialogCount");
    private final JsonConfiguration config;
    private final Mobile mobile;
    private final UsageStats usageStats;
    private final AppProperties appProperties;
    private final SccpService sccpService;
    private final MapService mapService;
    private final TcapService tcapService;
    private final M3uaService m3uaService;
    //HTTP channel for HTTP endpoints
    private final HttpChannel httpChannel;
    private QueryPool ocsPool;
    private QueryPool sriPool;
    private GatewayShell shell;
    private MapChannel mapChannel;
    //Local channel between HTTP or MAP Endpoints
    @Autowired
    private LocalChannel localChannel;


    /**
     * Starts gateway instance.
     *
     * @throws Exception
     */
    @PostConstruct
    public void start() throws Exception {
        ocsPool = new QueryPool(appProperties.getOcsJsonFileNamePrefix(), this, appProperties.getOcsJsonPath());
        sriPool = null;
        shell = new GatewayShell(this);

        log.info("Loading OCS pool");
        ocsPool.reload();
        log.info("Gateway Initializing is complete");
        this.startDialogsCount();
    }

    /**
     * Gets channel related to the URL protocol.
     *
     * @param url
     * @return
     * @throws UnknownProtocolException
     */
    public Channel channel(String url) throws UnknownProtocolException {
        switch (protocol(url)) {
            case "http":
                return httpChannel;
            case "map":
                return mapChannel.channel(new URL(url));
            case "proxy":
                return localChannel;
            default:
                throw new UnknownProtocolException("Protocol not supported: " + url);
        }
    }

    private String protocol(String url) {
        return url.substring(0, url.indexOf("://"));
    }

    public void start(Channel channel) {
        try {
            channel.start();
            log.info("Started channel " + channel);
        } catch (Exception e) {
            log.error("Couldn't start channel " + channel + ", Caused by ", e);
        }
    }

    public void restart(Channel channel) {
        channel.stop();
        start(channel);
    }

    public void startDialogsCount() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> LOG_DIALOG_COUNT.info(String.valueOf(mapService.getMapStack(mobile.getNAME()).getMAPProvider().getCurrentDialogsCount())), 10, 1, TimeUnit.SECONDS);
    }
}
