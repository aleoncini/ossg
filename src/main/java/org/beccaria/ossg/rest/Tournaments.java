package org.beccaria.ossg.rest;

import org.beccaria.ossg.model.ResponseInfo;
import org.beccaria.ossg.persistence.TournamentHelper;
import org.ossg.model.Tournament;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.logging.Logger;

@Path("/tournament")
public class Tournaments {
    private static final Logger logger = Logger.getLogger("org.ossg");

    @GET
    public String getInfo() {
        return "OSSG Tournament REST service";
    }

    @GET
    @Produces("application/json")
    @Path("{id}")
    public Response getTournament(@PathParam("id") String id) {
        Tournament tournament = new TournamentHelper().get(id);
        if (tournament == null){
            return Response.status(404).build();
        } else {
            Response.ResponseBuilder responseBuilder = Response.ok(tournament.toString());
            return responseBuilder.header("Access-Control-Allow-Origin", "http://www.zingarotour.org").build();
        }
    }

    @POST
    @Consumes("application/json")
    @Path("/ads")
    public Response addTournamentString(String jsonString){
        Tournament tournament = new Tournament().build(jsonString);
        return this.addTournament(tournament);
    }

    @POST
    @Consumes("application/json")
    @Path("/add")
    public Response addTournament(Tournament tournament){
        ResponseInfo responseInfo = new ResponseInfo();
        if (new TournamentHelper().save(tournament)){
            responseInfo.setId(tournament.getId()).setStatus(ResponseInfo.SUCCESS_STATUS);
        } else {
            responseInfo.setStatus(ResponseInfo.ERROR_STATUS).setMessage("Unable to save tournament.");
        }
        return Response.status(200).entity(responseInfo.toString()).build();
    }

    @GET
    @Produces("application/json")
    @Path("/search")
    public Response search(@QueryParam("day") int day,
                           @QueryParam("month") int month,
                           @QueryParam("year") int year) {
        Collection<Tournament> tournaments = null;

        tournaments = new TournamentHelper().search(day,month,year);

        return Response.status(200).entity(formatTournaments(tournaments)).build();
    }

    private String formatTournaments(Collection<Tournament> tournaments) {
        if (tournaments.isEmpty()){
            return "{\"tournaments\": [] }";
        }
        StringBuffer buffer = new StringBuffer("{\"tournaments\": [ ");
        boolean isFirstElement = true;
        for (Tournament t: tournaments) {
            if (isFirstElement){
                isFirstElement = false;
            } else {
                buffer.append(", ");
            }
            buffer.append(t.toString());
        }
        buffer.append(" ] }");
        return buffer.toString();
    }

}