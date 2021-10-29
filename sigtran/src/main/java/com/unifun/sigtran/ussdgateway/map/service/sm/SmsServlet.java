/**
 *
 */
package com.unifun.sigtran.ussdgateway.map.service.sm;

import com.unifun.sigtran.ussdgateway.gw.Channel;
import com.unifun.sigtran.ussdgateway.gw.Gateway;
import com.unifun.sigtran.ussdgateway.gw.QueryPool;
import com.unifun.sigtran.ussdgateway.gw.UnknownProtocolException;
import com.unifun.sigtran.ussdgateway.gw.context.ExecutionContext;
import com.unifun.sigtran.ussdgateway.gw.context.HttpLocalContext;
import com.unifun.sigtran.ussdgateway.map.dto.*;
import lombok.extern.log4j.Log4j2;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;

/**
 * @author rbabin
 *
 */
@WebServlet(name = "SmsServlet", urlPatterns = {"/sms"}, displayName = "SmsServlet", asyncSupported = true)
@Log4j2
public class SmsServlet extends HttpServlet {

    private static final String SMS_SERVICE_URI = "map://sms";

    private static final long serialVersionUID = 4183065359591890797L;

    private static final String RESP_PATTERN = "{\"RES\":\"OK\", \"IMSI\":\"%s\", \"VLR\":\"%s\"}\n";
    private static final String ERR_PATTERN = "{\"RES\":\"NOK\", \"ERROR\":\"%s\"}\n";

    private final Resolver resolver = new Resolver();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (log.isDebugEnabled()) {
            log.debug("SMS service short call");
        }
        //Obtain reference for the map processor from the servlet context
        final Gateway gateway = (Gateway) req.getServletContext().getAttribute("ussd.gateway");
        final AsyncContext context = req.startAsync();

        if (log.isDebugEnabled()) {
            log.debug("Starting async execution");
        }

        ExecutionContext.EXECUTOR.submit(() -> {
            //obtain respective pool of messages
            if (log.isDebugEnabled()) {
                log.debug("Obtaining message pool");
            }

            QueryPool pool = gateway.getSriPool();

            //verify that this pool contains at least one message
            if (pool.isEmpty()) {
                reject("SRI pool is empty, please check that at least one message with prefix 'sri-' has been deployed", context);
                return;
            }

            //take message from the pool. 
            if (log.isDebugEnabled()) {
                log.debug("Preparing message pattern");
            }

            String m = pool.nextQuery();

            if (log.isDebugEnabled()) {
                log.debug("Resolving variables");
            }

            Enumeration<String> names = req.getParameterNames();
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                String value = req.getParameter(name);

                if (log.isDebugEnabled()) {
                    log.debug(String.format("Resolving %s as %s", name, value));
                }

                m = resolver.resolve(m, name, value);
            }

            JsonMessage msg = new JsonMessage(m);

            //select map sms channel
            Channel channel;
            try {
                channel = gateway.channel(SMS_SERVICE_URI);

                if (log.isDebugEnabled()) {
                    log.debug("Channel = " + channel);
                }
            } catch (UnknownProtocolException e) {
                reject("Well known URI " + SMS_SERVICE_URI + " is not working", context);
                //will never happen
                return;
            }

            channel.send(SMS_SERVICE_URI, msg, new ExecutionContext() {
                @Override
                public void ack(JsonMessage msg) {
                }

                @Override
                public void completed(JsonMessage msg) {
                    response(msg, context);
                }

                @Override
                public void failed(Exception e) {
                    log.error("Could not send message: ", e);
                    reject(e.getMessage(), context);
                }

                @Override
                public void cancelled() {
                    reject("Canceled", context);
                }
            });
        });

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Obtain reference for the map processor from the servlet context
        Gateway gateway = (Gateway) req.getServletContext().getAttribute("ussd.gateway");
        //Start the asynchronous execution!
        //Asynchronous execution allows to leave methods Servlet.service() immediately
        //without holding this thread and container will be able to recycle this thread
        //for receiving incoming messages.
        //Final HTTP response will be handled later (when it will actually arrive)
        //by callback object HttpLocalContext
        AsyncContext context = req.startAsync();

        ExecutionContext.EXECUTOR.submit(() -> {
            try {
                //read message as context in json format
                JsonReader reader = Json.createReader(new InputStreamReader(req.getInputStream()));
                JsonObject obj = reader.readObject();

                Channel channel = gateway.channel("map://sms");
                log.info("Channel " + channel);
                channel.send("map://sms", new JsonMessage(obj), new HttpLocalContext(context));
            } catch (Exception e) {
                log.error("Could not process message", e);
                //Worst case. Could not do anything more
                try {
                    resp.getWriter().println("{\"Error\": \"" + e.getMessage() + "\"}");
                } catch (IOException ex) {
                    log.error("Hello from @author rbabin", e);
                }
            }
        });
    }

    private void response(JsonMessage msg, AsyncContext asyncContext) {
        JsonTcap tcap = msg.getTcap();
        JsonComponents components = tcap.getComponents();

        JsonComponent component = components.getComponent(0);
        JsonReturnResultLast result = (JsonReturnResultLast) component.getValue();

        JsonMap map = (JsonMap) result.component();
        JsonMapOperation op = (JsonMapOperation) map.operation();

        String imsi = op.getImsi();
        String vlr = op.getLocationInfoWithLMSI().getNetworkNodeNumber().getAddress();

        ((HttpServletResponse) asyncContext.getResponse()).setStatus(HttpServletResponse.SC_OK);
        ((HttpServletResponse) asyncContext.getResponse()).addHeader("RESULT", "OK");
        ((HttpServletResponse) asyncContext.getResponse()).addHeader("IMSI", imsi);
        ((HttpServletResponse) asyncContext.getResponse()).addHeader("VLR", vlr);
        ((HttpServletResponse) asyncContext.getResponse()).addHeader("Connection", "keep-alive");

        byte[] data = String.format(RESP_PATTERN, imsi, vlr).getBytes();
        asyncContext.getResponse().setContentLength(data.length);

        try {
            asyncContext.getResponse().getOutputStream().write(data);
            asyncContext.getResponse().flushBuffer();
        } catch (IOException e) {
            log.error("Unexpected error while sending HTTP response", e);
        }
    }

    private void reject(String msg, AsyncContext asyncContext) {
        try {
            byte[] data = String.format(ERR_PATTERN, msg).getBytes();

            asyncContext.getResponse().setContentType("application/json");
            asyncContext.getResponse().setCharacterEncoding("UTF-8");

            ((HttpServletResponse) asyncContext.getResponse()).setStatus(HttpServletResponse.SC_OK);
            ((HttpServletResponse) asyncContext.getResponse()).addHeader("RESULT", "NOK");
            ((HttpServletResponse) asyncContext.getResponse()).addHeader("Connection", "keep-alive");
            asyncContext.getResponse().setContentLength(data.length);

            asyncContext.getResponse().getOutputStream().write(data);
            asyncContext.getResponse().flushBuffer();
        } catch (IOException e) {
            log.error("Unexpected error while sending HTTP response", e);
        }
    }

}
