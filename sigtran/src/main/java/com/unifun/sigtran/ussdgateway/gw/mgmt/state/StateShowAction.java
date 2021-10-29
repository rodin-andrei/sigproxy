package com.unifun.sigtran.ussdgateway.gw.mgmt.state;

import com.unifun.sigtran.ussdgateway.gw.Gateway;
import com.unifun.sigtran.ussdgateway.gw.MapBaseChannel;
import com.unifun.sigtran.ussdgateway.gw.mgmt.Action;

import java.util.StringTokenizer;

public class StateShowAction extends Action {

    public StateShowAction(String name, Gateway gateway) {
        super(name, gateway);
    }

    @Override
    public String exec(String cmd, StringTokenizer tokenizer) throws Exception {

        String delimiter =
                "-----------------------------------------------------------------------------------------------------------------------------------" +
                        "\r\n\r\n";
        String linkShow = getGateway().getShell().exec("mobile link show");
        String subsystemShow = getGateway().getShell().exec("mobile subsystem show");
        String dialogShow = getGateway().getShell().exec("mobile dialog show");
        String dialogCount = getGateway().getShell().exec("mobile dialog stat");
        String ussdShow = getGateway().getShell().exec("ussd show");
        int connectTimeout = getGateway().getHttpChannel().getAppProperties().getEmbeddedTomcatConnectionTimeout();
        int socketTimeout = getGateway().getHttpChannel().getAppProperties().getEmbeddedTomcatConnectionTimeout();
        int proxyMapSize = MapBaseChannel.proxyIdMatcherMap.size();
        int httpMaxConnections = getGateway().getHttpChannel().getAppProperties().getEmbeddedTomcatMaxConnections();

        return "\r\n" +
                "                                                           SCTP LINKS                                                              " +
                "\r\n" +
                linkShow +
                delimiter +
                "\r\n" +
                "                                                           M3UA LAYER                                                              " +
                "\r\n" +
                subsystemShow +
                delimiter +
                "\r\n" +
                "                                                           USSD ROUTES                                                             " +
                "\r\n" +
                ussdShow +
                delimiter +
                "\r\n" +
                "                                                           DIALOG COUNT                                                            " +
                "\r\n" +
                dialogCount +
                delimiter +
                "\r\n" +
                "                                                           TOMCAT CONFIG                                                           " +
                "\r\n" +
                dialogShow +
                String.format("%25s  %4d\n", "connectTimeout", connectTimeout) +
                String.format("%25s  %4d\n", "socketTimeout", socketTimeout) +
                String.format("%25s  %4d\n", "proxyMapSize", proxyMapSize) +
                String.format("%25s  %4d\n", "httpMaxConnections", httpMaxConnections);
    }
}
