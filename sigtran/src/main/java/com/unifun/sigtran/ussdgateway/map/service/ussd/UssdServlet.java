package com.unifun.sigtran.ussdgateway.map.service.ussd;

import com.unifun.sigtran.ussdgateway.gw.Channel;
import com.unifun.sigtran.ussdgateway.gw.Gateway;
import com.unifun.sigtran.ussdgateway.gw.context.ExecutionContext;
import com.unifun.sigtran.ussdgateway.gw.context.HttpLocalAsyncContext;
import com.unifun.sigtran.ussdgateway.gw.context.HttpLocalContext;
import com.unifun.sigtran.ussdgateway.map.dto.JsonMessage;
import com.unifun.sigtran.ussdgateway.properties.AppProperties;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonParsingException;
import javax.servlet.AsyncContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;

/**
 * @author rbabin
 */
@WebServlet(name = "UssdServlet", urlPatterns = {"/ussd"}, displayName = "UssdServlet", asyncSupported = true)
@Log4j2
public class UssdServlet extends HttpServlet {

    private static final long serialVersionUID = 4183065359591890797L;
    private static final Logger LOGGER_HTTP = LoggerFactory.getLogger("HttpLogger");
    private static final String HTTP_LOGGING_STRING_FORMAT_RECEIVED = "[HTTP-GET ] [URL: %44s] [%12s] [DialogId: %11s] [PC: %5s] [SSN: %4s] [GT: %13s] [Tx: \"%-10s\"]";
    private final AppProperties appProperties;

    public UssdServlet(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");

        try (PrintWriter writer = resp.getWriter()) {
            writer.println("<body>");
            writer.println("<p>");
            writer.println("curl -X POST -H \"Content-Type: application/json\" -d @/home/okulikov/work/unifun/unifun-sigtran-modules/development/UssdGate/pussr2.json http://127.0.0.1:7080/UssdGate/mapapi");
            writer.println("</p>");
            writer.println("</body>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        log.info("<--- Request from " + req.getRemoteAddr() + ":" + req.getRemotePort());
        //Obtain reference for the map processor from the servlet context
        Gateway gateway = (Gateway) req.getServletContext().getAttribute("ussd.gateway");
        /*Start the asynchronous execution!
        Asynchronous execution allows to leave methods Servlet.service() immediately
        without holding this thread and container will be able to recycle this thread
        for receiving incoming messages.
        Final HTTP response will be handled later (when it will actually arrive)
        by callback object HttpLocalContext*/
        AsyncContext context = req.startAsync();


        context.setTimeout(appProperties.getUssdServletTimeout());

        ExecutionContext.EXECUTOR.execute(() -> {
            try {
                //read message as context in json format
                JsonReader reader = Json.createReader(new InputStreamReader(req.getInputStream()));
                JsonObject obj = reader.readObject();
                JsonMessage msg = new JsonMessage(obj);


                logOnHttpPost(req, msg);

                ExecutionContext executionContext;
                if (msg.getCallback() != null) {
                    log.info("msg with callback");
                    executionContext = new HttpLocalAsyncContext(gateway, context, msg.getCallback());
                } else {
                    log.info("msg without callback");
                    executionContext = new HttpLocalContext(context);
                    context.addListener(new HttpAsyncListener(executionContext));
                }

                Channel channel = gateway.channel("map://ussd");
                channel.send("map://ussd", msg, executionContext);
            } catch (Exception e) {
                log.error("Could not process message", e);
                //Worst case. Could not do anything more
                try {
                    resp.getWriter().println("{\"Error\": \"" + e.getMessage() + "\"}");
                } catch (IOException ex) {
                    log.error("error printing msg", e);
                }
            }
        });
    }

    private void logOnHttpPost(HttpServletRequest req, JsonMessage msg) {
        try {
            LOGGER_HTTP.info(String.format(HTTP_LOGGING_STRING_FORMAT_RECEIVED,
                    req.getRequestURL(),
                    getMsisdn(msg),
                    msg.getTcap().getDialog().getDialogId(),
                    msg.getSccp().getCalledPartyAddress().getPc(),
                    msg.getSccp().getCalledPartyAddress().getSsn(),
                    msg.getSccp().getCalledPartyAddress().getGlobalTitle().getDigits(),
                    getUssdString(msg.getTcap().getComponents().getComponent(0).getValue().toString())
            ));
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            LOGGER_HTTP.error(String.format(HTTP_LOGGING_STRING_FORMAT_RECEIVED,
                    req.getRequestURL(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    e.getMessage()
            ));
        }
    }

    private String getUssdString(String value) {
        try {
            if (value == null || "".equals(value)) {
                return "null";
            }
            try (JsonReader jsonReader = Json.createReader(new StringReader(value))) {
                return jsonReader.readObject().getJsonObject("component").getJsonObject("args").getString("ussdString");
            }
        } catch (NullPointerException | JsonParsingException e) {
            return "null";
        }
    }

    private String getMsisdn(JsonMessage msg) {
        try {
            String value = msg.getTcap().getComponents().getComponent(0).getValue().toString();
            if (value == null || "".equals(value)) {
                return "null";
            }
            return this.getMsisdnFromJson(msg, value);
        } catch (NullPointerException | JsonParsingException e) {
            return "null";
        }
    }

    private String getMsisdnFromJson(JsonMessage msg, String value) {
        try (JsonReader jsonReader = Json.createReader(new StringReader(value))) {
            return jsonReader.readObject()
                    .getJsonObject("component")
                    .getJsonObject("args")
                    .getJsonObject("msisdn")
                    .getString("address");
        } catch (NullPointerException | JsonParsingException e) {
            return msg.getTcap().getDialog().getMsisdn().getAddress();
        }
    }
}
