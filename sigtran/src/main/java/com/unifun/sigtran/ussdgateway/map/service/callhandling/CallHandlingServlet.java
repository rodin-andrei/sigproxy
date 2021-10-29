/**
 *
 */
package com.unifun.sigtran.ussdgateway.map.service.callhandling;

import com.unifun.sigtran.ussdgateway.gw.Channel;
import com.unifun.sigtran.ussdgateway.gw.Gateway;
import com.unifun.sigtran.ussdgateway.gw.context.ExecutionContext;
import com.unifun.sigtran.ussdgateway.gw.context.HttpLocalContext;
import com.unifun.sigtran.ussdgateway.map.dto.JsonMessage;
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
import java.io.PrintWriter;

/**
 * @author rbabin
 *
 */
@WebServlet(name = "CallHandlingServlet", urlPatterns = {"/callhandling"}, displayName = "CallHandlingServlet", asyncSupported = true)
@Log4j2
public class CallHandlingServlet extends HttpServlet {

    private static final long serialVersionUID = 4183065359591890797L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

                Channel channel = gateway.channel("map://callhandling");
                log.info("Channel " + channel);
                channel.send("map://callhandling", new JsonMessage(obj), new HttpLocalContext(context));
            } catch (Exception e) {
                log.error("Could not process message", e);
                //Worst case. Could not do anything more
                try {
                    resp.getWriter().println("{\"Error\": \"" + e.getMessage() + "\"}");
                } catch (IOException ex) {
                }
            }
        });
    }

}
