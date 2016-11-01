package org.lambadaframework.webruntime;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpField;

public class APIResponse {
	private String body;
	private int statusCode;
	private Map<String, String> headers;
	
	public APIResponse(ContentResponse response) {
		this.body = response.getContentAsString();
		this.statusCode = response.getStatus();
		this.headers = new HashMap<String, String>();
		
		Iterator<HttpField> it = response.getHeaders().iterator();
	    while (it.hasNext()) {
	    	HttpField httpField = (HttpField)it.next();
	    	headers.put(httpField.getName() , httpField.getValue());
	    }
				
	}
	

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
	public Map<String, String> getHeaders() {
		return headers;
	}


	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	
	@Override
    public String toString() {
        return "Response[ " +
                "body='" + body + '\'' +
                ", statusCode=" + statusCode +
                ", headers=" + headers +
                ']';
    }


	

}
