package org.lambadaframework.webruntime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class APIRequest {
	
	private String resource;
	private String path;
	private String httpMethod;
	private Map<String, String> headers;

	private Map<String, String> queryStringParameters;
	private String body;
	
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
		Request updatedRequest = request
				.method(httpMethod)
				.path(path)
				.content(new StringContentProvider(body));

		for (Map.Entry<String, String> header : headers.entrySet())
		{
		    updatedRequest.header(header.getKey(), header.getValue());
		}
		
		for (Map.Entry<String, String> queryParam : queryStringParameters.entrySet())
		{
			updatedRequest.param(queryParam.getKey(), queryParam.getValue());
		}
		
		return updatedRequest;
	}
	
	@Override
    public String toString() {
        return "Request[ " +
                "path='" +  path + '\'' +
                ", resource=" + resource +
                ", httpMethod=" + httpMethod +
                 ", headers=" + headers +
                 ", queryStringParameters=" + queryStringParameters +
                 ", body=" + body +       
                ']';
    }

}
