package org.beccaria.ossg.rest;

import org.beccaria.ossg.model.ResponseInfo;
import org.beccaria.ossg.persistence.RoundHelper;
import org.bson.Document;
import org.ossg.model.DayOfEvent;
import org.ossg.model.Round;
import org.ossg.model.Score;
import org.ossg.model.Scorecard;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
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
        if (new RoundHelper().saveScorecard(roundid, scorecard)){
            ResponseInfo responseInfo = new ResponseInfo().setStatus(ResponseInfo.SUCCESS_STATUS);
            return Response.status(200).entity(responseInfo.toString()).build();
        }
        ResponseInfo responseInfo = new ResponseInfo().setStatus(ResponseInfo.ERROR_STATUS);
        return Response.status(400).entity(responseInfo.toString()).build();
    }

    @POST
    @Produces("application/json")
    @Path("/updinfo/{roundid}")
    public Response updatePlayingHandicap(@PathParam("roundid") String roundid,
                                          @QueryParam("phcp") int phcp,
                                          @QueryParam("day") int dd,
                                          @QueryParam("month") int mm,
                                          @QueryParam("year") int yy) {
        ResponseInfo responseInfo = new ResponseInfo().setStatus(ResponseInfo.ERROR_STATUS);
        System.out.println("=====> " + roundid + " - " + phcp + " - " + dd + "." + mm + "." + yy);
        Round round = new RoundHelper().getById(roundid);
        if (round != null){
            DayOfEvent dayOfEvent = round.getDayOfEvent().setDay(dd).setMonth(mm).setYear(yy);
            RoundHelper helper = new RoundHelper();
            if (helper.savePlayingHandicap(roundid,phcp) && helper.saveDayOfEvent(roundid, dayOfEvent)){
                responseInfo = new ResponseInfo().setStatus(ResponseInfo.SUCCESS_STATUS);
            }
        }
        return Response.status(200).entity(responseInfo.toString()).build();
    }

    @POST
    @Produces("application/json")
    @Path("/remove/{id}")
    public Response deleteRound(@PathParam("id") String id) {
        ResponseInfo responseInfo = new ResponseInfo().setStatus(ResponseInfo.ERROR_STATUS);
        if (new RoundHelper().deleteRound(id)){
            responseInfo = new ResponseInfo().setStatus(ResponseInfo.SUCCESS_STATUS);
        }
        return Response.status(200).entity(responseInfo.toString()).build();
    }

    @POST
    @Produces("application/json")
    @Path("/set/{id}")
    public Response setTournament(@PathParam("id") String id, @QueryParam("tournamentid") String tournamentid) {
        ResponseInfo responseInfo = new ResponseInfo().setStatus(ResponseInfo.ERROR_STATUS);
        if (new RoundHelper().updateTournament(id, tournamentid)){
            responseInfo = new ResponseInfo().setStatus(ResponseInfo.SUCCESS_STATUS);
        }
        return Response.status(200).entity(responseInfo.toString()).build();
    }

    @POST
    @Produces("application/json")
    @Path("/init")
    public Response initNewRound(
            @QueryParam("playerid") String playerid,
            @QueryParam("phcp") int phcp,
            @QueryParam("courseid") String courseid,
            @QueryParam("tournamentid") String tournamentid,
            @QueryParam("day") int theDay,
            @QueryParam("month") int theMonth,
            @QueryParam("year") int theYear){
        System.out.println("===> " + playerid);
        System.out.println("===> " + phcp);
        System.out.println("===> " + courseid);
        System.out.println("===> " + tournamentid);
        System.out.println("===> " + theDay);
        System.out.println("===> " + theMonth);
        if (playerid == null || playerid.length() == 0){
            ResponseInfo responseInfo = new ResponseInfo()
                    .setStatus(ResponseInfo.ERROR_STATUS)
                    .setMessage("Player Id not specified.");
            return Response.status(200).entity(responseInfo.toString()).build();
        }
        if (phcp == 0){
            ResponseInfo responseInfo = new ResponseInfo()
                    .setStatus(ResponseInfo.ERROR_STATUS)
                    .setMessage("Playing handicap not specified.");
            return Response.status(200).entity(responseInfo.toString()).build();
        }
        if (courseid == null || courseid.length() == 0){
            ResponseInfo responseInfo = new ResponseInfo()
                    .setStatus(ResponseInfo.ERROR_STATUS)
                    .setMessage("Course Id not specified.");
            return Response.status(200).entity(responseInfo.toString()).build();
        }
        Round round = null;
        DayOfEvent dayOfEvent = null;
        if ((theDay > 0) && (theMonth > 0) & (theYear > 0)){
            dayOfEvent = new DayOfEvent().setDay(theDay).setMonth(theMonth).setYear(theYear);
        } else {
            dayOfEvent = new DayOfEvent().today();
        }
        if (new RoundHelper().roundExists(playerid,courseid,dayOfEvent)){
            ResponseInfo responseInfo = new ResponseInfo()
                    .setStatus(ResponseInfo.ERROR_STATUS)
                    .setMessage("Found another round in the same day and the same course.");
            return Response.status(200).entity(responseInfo.toString()).build();
        }
        round = new RoundHelper().initializeNewRound(playerid, phcp, courseid, tournamentid, dayOfEvent);
        if (round == null){
            ResponseInfo responseInfo = new ResponseInfo()
                    .setStatus(ResponseInfo.ERROR_STATUS)
                    .setMessage("Unable to initialize a new round.");
            return Response.status(200).entity(responseInfo.toString()).build();
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
    @Path("/ads")
    public Response addRoundString(String jsonString){
        Round round = new Round().build(Document.parse(jsonString));
        return this.addRound(round);
    }

    @POST
    @Consumes("application/json")
    @Path("/add")
    public Response addRound(Round round){
        if (new RoundHelper().save(round)){
            return Response.status(200).entity("{\"result\":\"success\", \"roundId\":\"" + round.getId() + "\", \"message\":\"OK\"}").build();
        } else {
            return Response.status(200).entity("{\"result\":\"ERROR\", \"roundId\":\"" + round.getId() + "\", \"message\":\"Unable to save round\"}").build();
        }
    }

    @GET
    @Produces("application/json")
    @Path("/byplayer/{id}")
    public Response listByPlayerId(@PathParam("id") String id) {
        Collection<Round> rounds = new RoundHelper().searchByFieldId("playerId",id);
        if (rounds.size() > 0){
            return Response.status(200).entity(rounds).build();
        }
        return Response.status(404).build();
    }

    @GET
    @Produces("application/json")
    @Path("/leaderboard/{id}")
    public Response listByTournamentId(@PathParam("id") String tournamentId) {
        Collection<Round> rounds = new RoundHelper().searchByFieldId("tournamentId", tournamentId);
        if (rounds.size() > 0){
            List<Round> ordered = new RoundHelper().orderByStableford(rounds);
            //to have descending order
            Collections.reverse(ordered);
            return Response.status(200).entity(leaderboardToJson(ordered)).build();
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

    private String leaderboardToJson(Collection<Round> rounds){
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
