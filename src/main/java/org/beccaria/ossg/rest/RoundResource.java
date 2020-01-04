package org.beccaria.ossg.rest;

import org.beccaria.ossg.persistence.RoundHelper;
import org.ossg.model.Round;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@Path("/round")
public class RoundResource {
    private static final Logger logger = Logger.getLogger("org.ossg");

    @GET
    public String getInfo() {
        return "OSSG - Single ROUND REST service     ";
    }

    @GET
    @Produces("application/json")
    @Path("{id}")
    public Response getRound(@PathParam("id") String id) {
        Round round = new RoundHelper().getById(id);
        if (round == null){
            return Response.status(404).build();
        } else {
            Response.ResponseBuilder responseBuilder = Response.ok(round.toString());
            return responseBuilder.header("Access-Control-Allow-Origin", "http://www.zingarotour.org").build();
        }
    }
}
