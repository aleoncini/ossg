package org.beccaria.ossg.rest;

import org.beccaria.ossg.model.ResponseInfo;
import org.beccaria.ossg.model.Round;
import org.beccaria.ossg.model.Score;
import org.beccaria.ossg.model.Scorecard;
import org.beccaria.ossg.persistence.RoundHelper;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.logging.Logger;

@Path("/rs/rounds")
public class Rounds {
    private static final Logger logger = Logger.getLogger("org.ossg");

    @GET
    public String getInfo() {
        return "OSSG ROUNDS REST service     ";
    }

    @GET
    @Produces("application/json")
    @Path("/round/{id}")
    public Response getRound(@PathParam("id") String id) {
        Round round = new RoundHelper().getById(id);
        if (round == null){
            return Response.status(404).build();
        } else {
            return Response.status(200).entity(round.toString()).build();
        }
    }

    @POST
    @Produces("application/json")
    @Path("/scorecard/{roundid}")
    public Response updateScorecard(@PathParam("roundid") String roundid, @QueryParam("phcp") int phcp, @QueryParam("scorecard") String sc) {
        Scorecard scorecard = new Scorecard().setPhcp(phcp).setScores(sc);
        logger.info("=== scorecard: " + scorecard.prettyPrint());
        if (new RoundHelper().saveScorecard(roundid, scorecard)){
            ResponseInfo responseInfo = new ResponseInfo().setStatus(ResponseInfo.SUCCESS_STATUS);
            return Response.status(200).entity(responseInfo.toString()).build();
        }
        ResponseInfo responseInfo = new ResponseInfo().setStatus(ResponseInfo.ERROR_STATUS);
        return Response.status(400).entity(responseInfo.toString()).build();
    }

    @DELETE
    @Produces("application/json")
    @Path("/remove/{id}")
    public Response deleteRound(@PathParam("id") String id) {
        if (new RoundHelper().deleteRound(id)){
            ResponseInfo responseInfo = new ResponseInfo().setStatus(ResponseInfo.SUCCESS_STATUS);
            return Response.status(200).entity(responseInfo.toString()).build();
        }
        ResponseInfo responseInfo = new ResponseInfo().setStatus(ResponseInfo.ERROR_STATUS);
        return Response.status(400).entity(responseInfo.toString()).build();
    }

    @POST
    @Path("/init")
    public Response initNewRound(
            @QueryParam("playerid") String playerid,
            @QueryParam("phcp") int phcp,
            @QueryParam("courseid") String courseid){
        if (playerid == null || playerid.length() == 0){
            ResponseInfo responseInfo = new ResponseInfo().setMessage("Player Id not specified.");
            return Response.status(400).entity(responseInfo.toString()).build();
        }
        if (courseid == null || courseid.length() == 0){
            ResponseInfo responseInfo = new ResponseInfo().setMessage("Course Id not specified.");
            return Response.status(400).entity(responseInfo.toString()).build();
        }
        Round round = new RoundHelper().initializeNewRound(playerid, phcp, courseid);
        if (round == null){
            ResponseInfo responseInfo = new ResponseInfo().setStatus(ResponseInfo.SUCCESS_STATUS)
                    .setMessage("Unable to initialize a new round.");
            return Response.status(400).entity(responseInfo.toString()).build();
        }
        ResponseInfo responseInfo = new ResponseInfo().setStatus(ResponseInfo.SUCCESS_STATUS)
                .setMessage("New Round successfully initialized.")
                .setId(round.getId());
        return Response.status(200).entity(responseInfo.toString()).build();
    }

    @POST
    @Path("/score/{roundid}/{holeid}")
    public Response saveHole(@PathParam("roundid") String roundId,
                             @PathParam("holeid") String holeId,
                             @QueryParam("strokes") int strokes,
                             @QueryParam("putts") int putts,
                             @QueryParam("fareway") boolean fareway,
                             @QueryParam("bunkers") int bunkers,
                             @QueryParam("points") int points,
                             @QueryParam("penalties") int penalties){
        if (strokes == 0){
            ResponseInfo responseInfo = new ResponseInfo().setMessage("Number of strokes cannot be 0 or not specified.");
            return Response.status(400).entity(responseInfo.toString()).build();
        }

        Score score = new Score().setStrokes(strokes);

        if (putts > 0){
            score.setPutts(putts);
        }

        if (fareway){
            score.setFareway(fareway);
        }

        if (bunkers > 0){
            score.setBunkers(bunkers);
        }

        if (penalties > 0){
            score.setPenalties(penalties);
        }

        if (points > 0){
            score.setPoints(points);
        }

        logger.info("======= SCORE: " + score.toString());

        if (new RoundHelper().saveHole(roundId, holeId, score)){
            ResponseInfo responseInfo = new ResponseInfo().setStatus(ResponseInfo.SUCCESS_STATUS);
            return Response.status(200).entity(responseInfo.toString()).build();
        }
        ResponseInfo responseInfo = new ResponseInfo().setStatus(ResponseInfo.ERROR_STATUS);
        return Response.status(400).entity(responseInfo.toString()).build();
    }

    @POST
    @Consumes("application/json")
    @Path("/add")
    public Response addRound(Round round){
        logger.info("===============================================================================" + round.prettyPrint());
        if (new RoundHelper().save(round)){
            return Response.status(200).entity("{\"result\":\"success\", \"roundId\":\"" + round.getId() + "\", \"message\":\"OK\"}").build();
        } else {
            return Response.status(200).entity("{\"result\":\"ERROR\", \"courseId\":\"" + round.getId() + "\", \"message\":\"Unable to save round\"}").build();
        }
    }

    @GET
    @Produces("application/json")
    @Path("/byplayer/{id}")
    public Response listByPlayerId(@PathParam("id") String id) {
        Collection<Round> rounds = new RoundHelper().searchByPlayerId(id);
        if (rounds.size() > 0){
            return Response.status(200).entity(rounds).build();
        }
        return Response.status(404).build();
    }

    @GET
    @Produces("application/json")
    @Path("/search")
    public Response search(@QueryParam("playerid") String playerid,
                           @QueryParam("tournamentid") String tournamentid,
                           @QueryParam("courseid") String courseid,
                           @QueryParam("year") int year,
                           @QueryParam("month") int month,
                           @QueryParam("day") int day) {
        Collection<Round> rounds = new RoundHelper().search(playerid, tournamentid, courseid, year, month, day);
        if (rounds.size() > 0){
            return Response.status(200).entity(collectionToJson(rounds)).build();
        }
        return Response.status(404).build();
    }

    private String collectionToJson(Collection<Round> rounds){
        boolean isElementAfterFirst = false;
        StringBuffer buffer = new StringBuffer("{\"rounds\": [ ");
        for (Round round : rounds) {
            if (isElementAfterFirst){
                buffer.append(",");
            } else {
                isElementAfterFirst = true;
            }
            buffer.append(round.toString());
        }
        buffer.append(" ] }");
        return buffer.toString();
    }
}
