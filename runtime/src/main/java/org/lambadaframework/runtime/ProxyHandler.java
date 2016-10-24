package org.lambadaframework.runtime;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

public class ProxyHandler implements RequestStreamHandler {
	
	static final Logger logger = Logger.getLogger(ProxyHandler.class);
	
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context)
            throws IOException {
        logger.debug("Handling request started = " + context);
        String response =  "Hello proxy";
        outputStream.write(response.getBytes(Charset.forName("UTF-8")));
    }

}
