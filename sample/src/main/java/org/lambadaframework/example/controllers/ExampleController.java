package org.lambadaframework.example.controllers;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
@Consumes({ MediaType.TEXT_PLAIN })
@Produces({ MediaType.TEXT_PLAIN })
@Component
public class ExampleController {


    static final Logger logger = Logger.getLogger(ExampleController.class);

    @GET
    public Response indexEndpoint(
    ) {
        logger.debug("Request got");
        return Response.status(200)
                .entity("Hello world")
                .build();
    }

    @GET
    @Path("/{name}")
    public Response exampleEndpoint(
            @PathParam("name") String name
    ) {

        logger.debug("Request got");
        return Response.status(201)
                .entity("Hello "+ name)
                .build();
    }

}
