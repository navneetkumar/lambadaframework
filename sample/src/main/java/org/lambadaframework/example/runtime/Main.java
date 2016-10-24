package org.lambadaframework.example.runtime;

import java.io.OutputStream;
import java.nio.charset.Charset;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.lambadaframework.example.controllers.LambdaRunnableImp;

public class Main {

	public static void main( String[] args ) throws Exception
    {        
		System.out.println("Starting Testing");
		JettyTestServer.ensureStarted(new LambdaRunnableImp());
		System.out.println("Jetty listening on = " + JettyTestServer.BASEURL);
		sendRequest();
    }
	
	public static void sendRequest() throws Exception {
    	HttpClient httpClient = new HttpClient();
    	httpClient.setFollowRedirects(false);
    	httpClient.start();
    	System.out.println("sending request to server = " + JettyTestServer.BASEURL);
    	ContentResponse r = httpClient.GET(JettyTestServer.BASEURL);
    	String response =  r.getContentAsString();
    	System.out.println("http response = " + response);
    }
}
