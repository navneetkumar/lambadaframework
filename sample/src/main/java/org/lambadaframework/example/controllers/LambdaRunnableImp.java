package org.lambadaframework.example.controllers;

import java.io.File;
import java.io.IOException;

import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.annotations.ClassInheritanceHandler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebXmlConfiguration;
import org.glassfish.jersey.server.spring.SpringWebApplicationInitializer;
import org.lambadaframework.example.runtime.LambdaRunnable;
import org.lambadaframework.example.controllers.JerseyConfig;
import org.springframework.web.WebApplicationInitializer;

public class LambdaRunnableImp implements LambdaRunnable {
	
	private static final String CONTEXT_PATH = "/";


	@Override
	public AbstractHandler getHandler() {
//		return new HelloHandler();
//		WebAppContext webapp = new WebAppContext();
		
		final WebAppContext webapp = new WebAppContext() {
            @Override
            public Resource getWebInf() throws IOException {
                return newResource("target");
            }
        };
	
        webapp.setThrowUnavailableOnStartupException(true);
        webapp.setContextPath(CONTEXT_PATH);
        webapp.setParentLoaderPriority(true);

        
        webapp.setConfigurations(new Configuration[] {
                new WebXmlConfiguration(),
                new Servlet3AnnotationConfiguration(SpringConfig.class,
                        SpringWebApplicationInitializer.class) });
        webapp.setClassLoader(Thread.currentThread().getContextClassLoader());
        		
		return webapp;
	}
	
	private static final class Servlet3AnnotationConfiguration extends AnnotationConfiguration {
        private final Class<? extends WebApplicationInitializer>[] initializerClasses;

        @SafeVarargs
        public Servlet3AnnotationConfiguration(final Class<? extends WebApplicationInitializer>... initializerClasses) {
            this.initializerClasses = initializerClasses;
        }

        @Override
        public void preConfigure(final WebAppContext context) throws Exception {
            final String[] classNames = new String[initializerClasses.length];
            for (int classNameIdx = 0; classNameIdx < initializerClasses.length; classNameIdx++) {
                classNames[classNameIdx] = initializerClasses[classNameIdx].getName();
            }

            final MultiMap<Object> sciMap = new MultiMap<>();
            sciMap.addValues(WebApplicationInitializer.class.getName(), classNames);
            context.setAttribute(AnnotationConfiguration.CLASS_INHERITANCE_MAP, sciMap);
            _classInheritanceHandler = new ClassInheritanceHandler(sciMap);
            super.preConfigure(context);
        }
    }
}