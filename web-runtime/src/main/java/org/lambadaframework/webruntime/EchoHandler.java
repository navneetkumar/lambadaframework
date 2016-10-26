package org.lambadaframework.webruntime;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

public class EchoHandler implements RequestStreamHandler {
	
	static final Logger logger = Logger.getLogger(EchoHandler.class);
	
	
	 public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
	    	System.out.println("Initializing Light Echo Handler"); 
	    	System.out.println("Got Context = " + context.toString()); 
	    	
	    	ByteArrayOutputStream result = new ByteArrayOutputStream();
	    	byte[] buffer = new byte[1024];
	    	int length;
	    	while ((length = input.read(buffer)) != -1) {
	    	    result.write(buffer, 0, length);
	    	}
	    	String inputString = result.toString("UTF-8");
	    	System.out.println("Received input  = " + inputString); 
	    	output.write(inputString.getBytes(Charset.forName("UTF-8")));   
	        System.out.println("Echo Handler ended"); 
	    }
    
}
