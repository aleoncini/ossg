package org.beccaria.ossg.rest;

import org.beccaria.ossg.model.Player;
import org.beccaria.ossg.model.ResponseInfo;
import org.beccaria.ossg.persistence.PlayerHelper;
import org.bson.Document;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.logging.Logger;

@Path("/player")
public class PlayerResource {
    private static final Logger logger = Logger.getLogger("org.ossg");

    @GET
    public String getInfo() {
        return "OSSG PLAYER REST service";
    }

    @GET
    @Produces("application/json")
    @Path("{id}")
    public Response getPlayerById(@PathParam("id") String id) {
        Response.ResponseBuilder responseBuilder;
        Player player = new PlayerHelper().getById(id);
        if (player == null){
            responseBuilder = Response.status(404);
        } else {
            responseBuilder = Response.ok(player.toString());
        }
        return responseBuilder.header("Access-Control-Allow-Origin", "http://www.zingarotour.org").build();
    }

    @POST
    @Consumes("application/json")
    @Path("/add")
    public Response addPlayer(String jsonString){
        Response.ResponseBuilder responseBuilder;
        Player player = new Player().build(Document.parse(jsonString));
        if (player == null){
            responseBuilder = Response.status(400); // Bad Request
            return responseBuilder.header("Access-Control-Allow-Origin", "http://www.zingarotour.org").build();
        }

        if (new PlayerHelper().save(player)){
            responseBuilder = Response.ok(player.toString());
            return responseBuilder.header("Access-Control-Allow-Origin", "http://www.zingarotour.org").build();
        }

        responseBuilder = Response.ok("{}");
        return responseBuilder.header("Access-Control-Allow-Origin", "http://www.zingarotour.org").build();
    }

    @GET
    @Produces("application/json")
    @Path("/search")
    public Response search(@QueryParam("name") String name) {
        if ((name != null) && (name.length() > 0)){
            Collection<Player> players = new PlayerHelper().searchByName(name);
            Response.ResponseBuilder responseBuilder = Response.ok(formatPlayersList(players));
            return responseBuilder.header("Access-Control-Allow-Origin", "http://www.zingarotour.org").build();
        }
        Response.ResponseBuilder responseBuilder = Response.status(404); // Not Found
        return responseBuilder.header("Access-Control-Allow-Origin", "http://www.zingarotour.org").build();
    }

    @GET
    @Produces("application/json")
    @Path("/list")
    public Response list() {
        Collection<Player> players = new PlayerHelper().list();
        Response.ResponseBuilder responseBuilder = Response.ok(formatPlayersList(players));
        return responseBuilder.header("Access-Control-Allow-Origin", "http://www.zingarotour.org").build();
    }

    private String formatPlayersList(Collection<Player> players) {

        if (players.isEmpty()){
            return "{\"players\": [] }";
        }

        logger.info("[Search Players by name] found " + players.size() + " players.");

        StringBuffer buffer = new StringBuffer("{\"players\": [ ");
        boolean isFirstElement = true;

        for (Player player: players) {
            if (isFirstElement){
                isFirstElement = false;
            } else {
                buffer.append(", ");
            }
            buffer.append(player.toString());
        }

        buffer.append(" ] }");

        return buffer.toString();
    }

}
