package org.lambadaframework.example.runtime;

import java.io.IOException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;


public class JettyTestServer {
	public static final int PORT = 8080;
	private static Server jetty = new Server(PORT);
	
	public synchronized static void ensureStarted(LambdaRunnable web) throws IOException {
		 if (!jetty.isStarted()) {
			 WebAppContext context = web.getContext();
			 System.out.println("Starting Jetty...");
			 jetty.setHandler(context);
			 
			 try {
		            System.out.println("Starting Jetty...");
		            jetty.start();
		            System.out.println("Jetty started!");
		        } catch (final Exception e) {
		            System.out.println("Crumbs!");
		            e.printStackTrace();
		        }
		 }
		 
	}
	
	public static void stop() {
	    try {
	        jetty.stop();
	    } catch (final Exception e) {
	    }
	}
}