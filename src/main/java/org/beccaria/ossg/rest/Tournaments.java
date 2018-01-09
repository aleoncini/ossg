package org.beccaria.ossg.rest;

import org.beccaria.ossg.model.Tournament;
import org.beccaria.ossg.persistence.TournamentHelper;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.logging.Logger;

@Path("/rs/tournaments")
public class Tournaments {
    private static final Logger logger = Logger.getLogger("org.ossg");

    @GET
    public String getInfo() {
        return "OSSG Tournament REST service";
    }

    @GET
    @Produces("application/json")
    @Path("/tournament/{id}")
    public Response getTournament(@PathParam("id") String id) {
        Tournament tournament = new TournamentHelper().get(id);
        if (tournament == null){
            return Response.status(404).build();
        } else {
            return Response.status(200).entity(tournament.toString()).build();
        }
    }

    @POST
    @Consumes("application/json")
    @Path("/add")
    public Response addTournament(Tournament tournament){
        if (new TournamentHelper().save(tournament)){
            return Response.status(200).entity("{\"result\":\"success\", \"tournamentId\":\"" + tournament.getId() + "\", \"message\":\"OK\"}").build();
        } else {
            return Response.status(200).entity("{\"result\":\"ERROR\", \"tournamentId\":\"" + tournament.getId() + "\", \"message\":\"Unable to save Tournament\"}").build();
        }
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