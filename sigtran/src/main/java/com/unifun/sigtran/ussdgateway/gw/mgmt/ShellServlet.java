/**
 *
 */
package com.unifun.sigtran.ussdgateway.gw.mgmt;

import com.unifun.sigtran.ussdgateway.gw.Gateway;
import com.unifun.sigtran.ussdgateway.gw.context.ExecutionContext;
import lombok.extern.log4j.Log4j2;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * @author rbabin
 *
 */
@Log4j2
@WebServlet(name = "ShellServlet", urlPatterns = {"/shell"}, displayName = "ShellServlet", asyncSupported = true)
public class ShellServlet extends HttpServlet {

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
                BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream()));
                String line;

                while ((line = reader.readLine()) != null) {
                    line = line.trim();

                    if (line.length() == 0) {
                        continue;
                    }

                    if (line.equals("state show") || line.equals("mobile link show") || line.equals("mobile subsystem show")) {
                        log.debug(line);
                    } else {
                        log.info(line);
                    }

                    if (line.startsWith("#")) {
                        continue;
                    }

                    final String res = gateway.getShell().exec(line);
                    resp.getWriter().println(res);
                }

                resp.getWriter().flush();
                resp.getWriter().close();
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
