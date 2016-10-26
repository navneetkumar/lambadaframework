package org.lambadaframework.example.runtime;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Properties;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.lambadaframework.example.controllers.LambdaRunnableImp;


import com.amazonaws.services.lambda.runtime.ClientContext;
import com.amazonaws.services.lambda.runtime.CognitoIdentity;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.fasterxml.jackson.databind.ObjectMapper;


public class Main {

	public static void main( String[] args ) throws Exception
    {        
		System.out.println("Starting Testing");
		Handler handler = new Handler();
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		
		Properties props = new Properties();
		props.load(Thread.currentThread().getContextClassLoader().getResource("lambda.properties").openStream());
		String runnableClass = props.getProperty("deployment.lambdaRunnabe");
		System.out.println("Got the lambda class = " + runnableClass);
		
		
		handler.handleRequest(getInput(), output, getContext());
		System.out.println("Got output from = " + output.toString());
    }
	
	
	private static Context getContext() {
        return new Context() {
            @Override
            public String getAwsRequestId() {
                return "23234234";
            }

            @Override
            public String getLogGroupName() {
                return null;
            }

            @Override
            public String getLogStreamName() {
                return null;
            }

            @Override
            public String getFunctionName() {
                return null;
            }

            @Override
            public String getFunctionVersion() {
                return null;
            }

            @Override
            public String getInvokedFunctionArn() {
                return null;
            }

            @Override
            public CognitoIdentity getIdentity() {
                return null;
            }

            @Override
            public ClientContext getClientContext() {
                return null;
            }

            @Override
            public int getRemainingTimeInMillis() {
                return 5000;
            }

            @Override
            public int getMemoryLimitInMB() {
                return 128;
            }

            @Override
            public LambdaLogger getLogger() {
                return null;
            }
        };
    }
	
	private static InputStream getInput() throws Exception {
		String request = "{\n" +
                "  \"package\": \"org.lambadaframework\",\n" +
                "  \"pathTemplate\": \"/{id}\",\n" +
                "  \"method\": \"GET\",\n" +
                "  \"requestBody\": {},\n" +
                "  \"path\": {\n" +
                "    \"id\": \"123\"\n" +
                "  },\n" +
                "  \"querystring\": {\n" +
                "        \"query1\": \"test3\",\n" +
                "    \"query2\": \"test\"\n" +
                "  },\n" +
                "  \"header\": {}\n" +
                "}";
		return new ByteArrayInputStream( request.getBytes() );
    }
}
