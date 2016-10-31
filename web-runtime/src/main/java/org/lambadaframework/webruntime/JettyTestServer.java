package org.lambadaframework.webruntime;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class JettyTestServer {
	public static final int PORT = 8070;
	private static Server jetty = new Server(PORT);
	public static final String BASEURL = "http://127.0.0.1:" + Integer.toString(PORT);
	static Boolean isRunning = false;
	static final Logger logger = Logger.getLogger(JettyTestServer.class);
	
	public static void ensureStarted(LambdaRunnable web) throws IOException {
		logger.debug("attempting to start server");
		 if (!JettyTestServer.isRunning) {
			 logger.debug("Jetty was not running. Starting Jetty...");
			 jetty = new Server(PORT);
			 AbstractHandler handler = web.getHandler();
			 
			 jetty.setHandler(handler);
			 try {
				 	logger.debug("Starting Jetty...");
				 	JettyTestServer.isRunning = true;
		            jetty.start();
		            logger.debug("Jetty started!");
		        } catch (final Exception e) {
		            System.out.println("Crumbs!");
		            e.printStackTrace();
		        }
		 }
		 else {
			 logger.debug("Server is already running");
		 }
		 
	}
	
	public static void stop() {
	    try {
	        jetty.stop();
	        
	    } catch (final Exception e) {
	    }
	}
}