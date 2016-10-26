package org.lambadaframework.webruntime;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

public class EchoHandler implements RequestStreamHandler {
	
	static final Logger logger = Logger.getLogger(EchoHandler.class);
	
	
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context)
            throws IOException {
    	System.out.println("Initializing Echo Handler"); 
    	int letter;
        while((letter = inputStream.read()) != -1)
        {
            outputStream.write(Character.toUpperCase(letter));
        }
        System.out.println("Context = " + context.toString()); 
        outputStream.write(context.toString().getBytes(Charset.forName("UTF-8")));
        
    }
    
}
