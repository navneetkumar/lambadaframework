package org.lambadaframework.webruntime;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.webapp.WebAppContext;

public interface LambdaRunnable {
	
	public AbstractHandler getHandler();

}
