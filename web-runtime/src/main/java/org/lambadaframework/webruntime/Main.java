package org.lambadaframework.webruntime;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import com.amazonaws.services.lambda.runtime.ClientContext;
import com.amazonaws.services.lambda.runtime.CognitoIdentity;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;


public class Main {

	public static void main( String[] args ) throws Exception
    {        
		System.out.println("Starting Testing from WebRuntime library");
		Handler handler = new Handler();
		ByteArrayOutputStream output = new ByteArrayOutputStream();
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
	
	private static String getRequestFile() {
		return "sample-get.json";
	}
	
	private static InputStream getInput() throws Exception {
		return Thread.currentThread().getContextClassLoader().getResourceAsStream(getRequestFile());
    }
}
