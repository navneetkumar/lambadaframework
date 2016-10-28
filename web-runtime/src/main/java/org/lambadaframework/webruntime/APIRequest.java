package org.lambadaframework.webruntime;

import java.util.Map;
import org.eclipse.jetty.client.HttpRequest;
import org.eclipse.jetty.client.api.Request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class APIRequest {
	
	private String resource;
	private String path;
	private String httpMethod;
	private Map<String, String> headers;
	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public Map<String, String> getQueryStringParameters() {
		return queryStringParameters;
	}

	public void setQueryStringParameters(Map<String, String> queryStringParameters) {
		this.queryStringParameters = queryStringParameters;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	private Map<String, String> queryStringParameters;
	private String body;
	
	

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}
	
	public Request copyRequest(Request request) {
		return request.method(httpMethod).path(path);
	}
	
	@Override
    public String toString() {
        return "Request[ " +
                "path='" + path + '\'' +
                ", resource=" + resource +
                ", httpMethod=" + httpMethod +
                 ", headers=" + headers +
                 ", queryStringParameters=" + queryStringParameters +
                 ", body=" + body +       
                ']';
    }

}
