package org.lambadaframework.example.controllers;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Path("/api")
@Consumes({ MediaType.TEXT_PLAIN })
@Produces({ MediaType.TEXT_PLAIN })
@Component
public class RestController {
	static final Logger logger = Logger.getLogger(RestController.class);
	@GET
    public Response indexEndpoint(
    ) {
        logger.debug("Request got");
        return Response.status(200)
                .entity("Hello Rest API")
                .build();
    }

}
