package org.beccaria.ossg.rest;

import org.beccaria.ossg.model.Player;
import org.beccaria.ossg.persistence.PlayerHelper;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@Path("/rs/players")
public class Players {
    private static final Logger logger = Logger.getLogger("org.ossg");

    @GET
    public String getInfo() {
        return "OSSG PLAYERS REST service";
    }

    @GET
    @Produces("application/json")
    @Path("/player/{id}")
    public Response checkEmail(@PathParam("id") String id) {
        Player player = new PlayerHelper().getById(id);
        if (player == null){
            return Response.status(404).build();
        } else {
            return Response.status(200).entity(player.toString()).build();
        }
    }
}
