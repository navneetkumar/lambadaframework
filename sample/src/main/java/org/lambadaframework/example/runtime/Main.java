package org.lambadaframework.example.runtime;

public class Main {

	public static void main( String[] args ) throws Exception
    {        
		System.out.println("Starting Testing");
		JettyTestServer.ensureStarted(new LambdaRunnableImp());
    }
}
