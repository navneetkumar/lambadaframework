package org.lambadaframework.example.runtime;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.lambadaframework.example.controllers.LambdaRunnableImp;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;



public class Handler implements RequestStreamHandler {
	
	static final Logger logger = Logger.getLogger(Handler.class);
	
	
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context)
            throws IOException {
    	System.out.println("Initializing New Handler"); 
        try {
        	
        	Properties props = new Properties();
    		props.load(Thread.currentThread().getContextClassLoader().getResource("lambda.properties").openStream());
    		String runnableClass = props.getProperty("deployment.lambdaRunnabe");
    		LambdaRunnable runnableInstance = (LambdaRunnable)Class.forName(runnableClass).getConstructor().newInstance();
    		
    		System.out.println("Got the lambdarunnable class = " + runnableClass);
    		System.out.println("Got the lambda runnable instance = " + runnableInstance);
        	logger.debug("starting server with runnable class");
        	JettyTestServer.ensureStarted(runnableInstance);
        	logger.debug("sending request to local server ");
			sendRequest(outputStream);
		} catch (Exception e) {
			logger.debug("Handling request failed = " + e.getLocalizedMessage());
			e.printStackTrace();
		}
    }
    
    
    public void sendRequest(OutputStream outputStream) throws Exception {
    	HttpClient httpClient = new HttpClient();
    	httpClient.setFollowRedirects(false);
    	httpClient.start();
    	logger.debug("sending request to server = " + JettyTestServer.BASEURL);
    	ContentResponse r = httpClient.GET(JettyTestServer.BASEURL);
    	String response =  r.getContentAsString();
    	logger.debug("http response = " + response);
    	outputStream.write(response.getBytes(Charset.forName("UTF-8")));
  
    }

}