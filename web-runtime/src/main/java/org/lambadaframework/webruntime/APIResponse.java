package org.lambadaframework.webruntime;

import org.eclipse.jetty.client.api.ContentResponse;

public class APIResponse {
	private String body;
	private int statusCode;
	
	public APIResponse(ContentResponse response) {
		this.body = response.getContentAsString();
		this.statusCode = response.getStatus();
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
	
	@Override
    public String toString() {
        return "Response[ " +
                "body='" + body + '\'' +
                ", statusCode=" + statusCode +
                ']';
    }

}
