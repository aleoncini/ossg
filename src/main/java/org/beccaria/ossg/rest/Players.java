package org.beccaria.ossg.rest;

import org.beccaria.ossg.model.Player;
import org.beccaria.ossg.model.ResponseInfo;
import org.beccaria.ossg.persistence.PlayerHelper;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import java.util.Base64;

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

    @GET
    @Produces("application/json")
    @Path("/user")
    public Response checkUser(@Context HttpHeaders headers) {
        String encoded = headers.getHeaderString("authorization").substring("Basic ".length());
        byte[] decoded = Base64.getDecoder().decode(encoded);
        String authorizationHeader = null;
        try {
            authorizationHeader = new String(decoded, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (authorizationHeader == null) {
            ResponseInfo responseInfo = new ResponseInfo().setStatus("ERROR")
                    .setMessage("Unable to read user email.");
            return Response.status(200).entity(responseInfo.toString()).build();
        }
        String[] tokens = authorizationHeader.split(":");
        Player player = new PlayerHelper().getByEmail(tokens[0]);
        if (player == null){
            ResponseInfo responseInfo = new ResponseInfo().setStatus("ERROR")
                    .setMessage("User not found.");
            return Response.status(200).entity(responseInfo.toString()).build();
        }
        return Response.status(200).entity(player.toString()).build();
    }

    @GET
    @Produces("application/json")
    @Path("/check/email")
    public Response getRound(@QueryParam("email") String email) {
        if ((email != null) && (email.length() > 0)){
            Player player = new PlayerHelper().getByEmail(email);
            if (player != null){
                return Response.status(200).entity(player.toString()).build();
            }
        }
        return Response.status(200).entity("{\"id\":\"none\"}").build();
    }

    @POST
    @Consumes("application/json")
    @Path("/add")
    public Response addPlayer(Player player){
        if (new PlayerHelper().save(player)){
            ResponseInfo responseInfo = new ResponseInfo().setStatus("success")
                    .setId(player.getId())
                    .setMessage("OK");
            return Response.status(200).entity(responseInfo.toString()).build();
        } else {
            ResponseInfo responseInfo = new ResponseInfo().setStatus("ERROR")
                    .setMessage("Unable to save round.");
            return Response.status(200).entity(responseInfo.toString()).build();
        }
    }

    @GET
    @Produces("application/json")
    @Path("/search")
    public Response search(@QueryParam("name") String name) {
        if ((name != null) && (name.length() > 0)){
            Collection<Player> players = new PlayerHelper().searchByName(name);
            return Response.status(200).entity(formatPlayersList(players)).build();
        }

        logger.info("======================> if you see this check that player actually doesn't exist");
        return Response.status(404).build();
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
