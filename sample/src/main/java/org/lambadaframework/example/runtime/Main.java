package org.lambadaframework.example.runtime;

import org.lambadaframework.example.controllers.LambdaRunnableImp;

public class Main {

	public static void main( String[] args ) throws Exception
    {        
		System.out.println("Starting Testing");
		JettyTestServer.ensureStarted(new LambdaRunnableImp());
    }
}
