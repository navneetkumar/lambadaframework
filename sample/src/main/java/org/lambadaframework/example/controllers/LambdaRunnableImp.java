package org.lambadaframework.example.controllers;

import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.lambadaframework.example.runtime.LambdaRunnable;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;


public class LambdaRunnableImp implements LambdaRunnable {

	@Override
	public AbstractHandler getHandler() {
 		final ServletContextHandler servletHandler = new ServletContextHandler();
 		// Spring
 		final AnnotationConfigWebApplicationContext springContext = new AnnotationConfigWebApplicationContext();
 		springContext.register(SpringConfig.class);
 		servletHandler.addEventListener(new ContextLoaderListener(springContext));

 		// Jersey
 		final ServletHolder jerseyServlet = servletHandler.addServlet(ServletContainer.class, "/*");
 		jerseyServlet.setInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS, JerseyConfig.class.getName());
 		
 		return servletHandler;
	}
}