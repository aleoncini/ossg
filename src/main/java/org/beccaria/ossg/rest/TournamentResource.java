package org.beccaria.ossg.rest;

import org.beccaria.ossg.model.ResponseInfo;
import org.beccaria.ossg.model.Tournament;
import org.beccaria.ossg.persistence.TournamentHelper;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.logging.Logger;

@Path("/tournament")
public class TournamentResource {
    private static final Logger logger = Logger.getLogger("org.ossg");

    @GET
    public String getInfo() {
        return "OSSG New Tournament REST service";
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
    @Produces("application/json")
    @Path("/add")
    public Response addTournament(String jsonString){
        ResponseInfo responseInfo = new ResponseInfo();
        Tournament tournament = new Tournament().build(jsonString);
        if (new TournamentHelper().save(tournament)){
            responseInfo.setId(tournament.getId()).setStatus(ResponseInfo.SUCCESS_STATUS);
        } else {
            responseInfo.setStatus(ResponseInfo.ERROR_STATUS).setMessage("Unable to save tournament.");
        }
        return Response.ok(responseInfo.toString()).build();
    }

    @GET
    @Produces("application/json")
    @Path("/search")
    public Response search(@QueryParam("day") int day,
                           @QueryParam("month") int month,
                           @QueryParam("year") int year) {
        Collection<Tournament> tournaments = null;

        tournaments = new TournamentHelper().search(day,month,year);

        Response.ResponseBuilder responseBuilder = Response.ok(formatTournaments(tournaments));
        return responseBuilder.header("Access-Control-Allow-Origin", "http://www.zingarotour.org").build();
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