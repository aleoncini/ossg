package org.beccaria.ossg.rest;

import org.beccaria.ossg.persistence.RoundHelper;
import org.beccaria.ossg.persistence.TournamentHelper;
import org.ossg.model.Round;
import org.ossg.model.Tournament;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@Path("/leaderboard")
public class LeaderBoard {
    private static final Logger logger = Logger.getLogger("org.ossg");

    @GET
    public String getInfo() {
        return "OSSG - REST service to retrieve Leaderboard of a tournament";
    }

    @GET
    @Produces("application/json")
    @Path("{id}")
    public Response getLeaderboard(@PathParam("id") String id) {
        Collection<Round> rounds = new RoundHelper().searchByFieldId("tournamentId", id);
        if (rounds.size() > 0){
            List<Round> ordered = new RoundHelper().orderByStableford(rounds);
            //to have descending order
            Collections.reverse(ordered);
            Response.ResponseBuilder responseBuilder = Response.ok(listToString(ordered));
            return responseBuilder.header("Access-Control-Allow-Origin", "http://www.zingarotour.org").build();
        }
        return Response.status(404).build();
    }

    private String listToString(Collection<Round> rounds){
        boolean isFirst = true;
        StringBuffer buffer = new StringBuffer("{\"results\": [ ");
        for (Round round : rounds) {
            if (isFirst){
                isFirst = false;
            } else {
                buffer.append(",");
            }
            buffer.append(round.result());
        }
        buffer.append(" ] }");
        return buffer.toString();
    }
}
