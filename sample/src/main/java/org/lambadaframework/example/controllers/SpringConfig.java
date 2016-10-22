package org.lambadaframework.example.controllers;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

@Order(0)
//run before the default Jersey SpringWebApplicationInitializer
public class SpringConfig implements WebApplicationInitializer {

    static final Logger LOGGER = Logger.getLogger(ExampleController.class);

    static final String PACKAGE_SCAN = "org.lambadaframework.example";
    
    private static AnnotationConfigWebApplicationContext context;

    @Override
    public void onStartup(final ServletContext servletContext) throws ServletException {

        servletContext.setInitParameter("contextConfigLocation", ""); //prevent Jersey from also initializing Spring

        context = new AnnotationConfigWebApplicationContext();        
        context.scan(PACKAGE_SCAN);
        servletContext.addListener(new ContextLoaderListener(context));
        LOGGER.trace("Set up Spring context.");
    }

    public static ApplicationContext getApplicationContext() {
        return context;
    }

}
