/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unifun.sigtran.ussdgateway.gw;

import com.unifun.sigtran.ussdgateway.gw.config.JsonConfiguration;
import com.unifun.sigtran.ussdgateway.gw.mgmt.ShellServlet;
import com.unifun.sigtran.ussdgateway.map.service.callhandling.CallHandlingServlet;
import com.unifun.sigtran.ussdgateway.map.service.mobility.MobilityServlet;
import com.unifun.sigtran.ussdgateway.map.service.sm.SmsServlet;
import com.unifun.sigtran.ussdgateway.map.service.ussd.UssdServlet;
import com.unifun.sigtran.ussdgateway.properties.AppProperties;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.net.InetAddress;

/**
 * Wrapper around embedded tomcat instance.
 *
 * @author okulikov
 */
@Component
@Data
@RequiredArgsConstructor
public class TomcatLocal {
    private final Gateway gateway;
    private final AppProperties appProperties;
    private final JsonConfiguration config;
    private Tomcat tomcat;

    @PostConstruct
    public void start() throws LifecycleException {
        tomcat = new Tomcat();

        final Connector nioConnector = new Connector(Http11NioProtocol.class.getName());
        nioConnector.setPort(appProperties.getEmbeddedTomcatPort());
        nioConnector.setSecure(false);
        nioConnector.setScheme("http");
        nioConnector.setAsyncTimeout(config.getDialogConfig().getInvokeTimeout());

        try {
            nioConnector.setProperty("address", InetAddress.getByName("0.0.0.0").getHostAddress());
        } catch (Exception e) {
            throw new RuntimeException("unexpected", e);
        }

        tomcat.getService().removeConnector(tomcat.getConnector());
        tomcat.getService().addConnector(nioConnector);
        tomcat.setConnector(nioConnector);

        String contextPath = "/";
        String docBase = new File(".").getAbsolutePath();

        Context context = tomcat.addContext(contextPath, docBase);
        context.getServletContext().setAttribute("ussd.gateway", gateway);

        //Deploy USSD servlet
        String servletName = "UssdServlet";
        String urlPattern = "/gateway/ussd";

        tomcat.addServlet(contextPath, servletName, new UssdServlet(appProperties));
        context.addServletMappingDecoded(urlPattern, servletName);
        context.addServletMappingDecoded("/UssdGate/mapapi", servletName);

        //Deploy CallHandling servlet
        tomcat.addServlet(contextPath, "CallHandlingServlet", new CallHandlingServlet());
        context.addServletMappingDecoded("/gateway/callhandling", "CallHandlingServlet");

        //Deploy Mobility servlet
        tomcat.addServlet(contextPath, "MobilityServlet", new MobilityServlet());
        context.addServletMappingDecoded("/gateway/mobility", "MobilityServlet");

        //Deploy SM servlet
        tomcat.addServlet(contextPath, "SmsServlet", new SmsServlet());
        context.addServletMappingDecoded("/gateway/sms", "SmsServlet");

        //Deploy Shell servlet
        tomcat.addServlet(contextPath, "ShellServlet", new ShellServlet());
        context.addServletMappingDecoded("/gateway/shell", "ShellServlet");


        tomcat.start();
    }

    public void stop() throws LifecycleException {
        tomcat.stop();
    }

    public String getState() {
        return tomcat.getServer().getStateName();
    }
}
