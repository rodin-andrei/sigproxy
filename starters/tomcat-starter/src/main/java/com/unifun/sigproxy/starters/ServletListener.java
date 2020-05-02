package com.unifun.sigproxy.starters;

import lombok.extern.slf4j.Slf4j;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@Slf4j
public class ServletListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("Lookup for SigtranObjectFactory in JNDI");
        Object lookup;
        Context initContext = null;
        while (true) {
            try {
                initContext = new InitialContext();
                lookup = initContext.lookup("java:comp/env/bean/SigtranObjectFactory");
                if (lookup != null) {
                    break;
                }
                log.warn("Not found SigtranObjectFactory, waiting");
                Thread.sleep(1000);
            } catch (NamingException e) {
                log.error(e.getMessage(), e);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
                return;
            }
        }
        //TODO set lookup to all sigtran services
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //TODO realise remove all listeners

    }
}
