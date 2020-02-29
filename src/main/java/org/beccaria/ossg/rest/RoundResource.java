package org.beccaria.ossg.rest;

import org.beccaria.ossg.model.*;
import org.beccaria.ossg.persistence.CourseHelper;
import org.beccaria.ossg.persistence.PlayerHelper;
import org.beccaria.ossg.persistence.RoundHelper;
import org.beccaria.ossg.persistence.TournamentHelper;
import org.bson.Document;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Collection;
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

    /*
        Required by CORS, the call to /create is not a simple request
        so the browser will perform a pre-flight request to know the headers and method accepted
     */
    @OPTIONS
    @Path("/create")
    public Response preCreateRound(String jsonString){
        logger.info("========> requested OPTIONS");
        Response.ResponseBuilder responseBuilder = Response.ok();
        return responseBuilder.header("Access-Control-Allow-Origin", "http://www.zingarotour.org")
                .header("Access-Control-Allow-Methods", "OPTIONS, POST, GET")
                .build();
    }

    @POST
    //@Consumes("application/json")
    @Path("/create")
    public Response createRound(String jsonString){
        logger.info("========> requested POST");
        Round round = create(Document.parse(jsonString));
        if (round != null){
            new RoundHelper().save(round);
            Response.ResponseBuilder responseBuilder = Response.ok("{\"result\":\"success\", \"roundId\":\"" + round.getId() + "\", \"message\":\"OK\"}");
            return responseBuilder.header("Access-Control-Allow-Origin", "http://www.zingarotour.org").build();
        } else {
            Response.ResponseBuilder responseBuilder = Response.ok("{\"result\":\"ERROR\", \"roundId\":\"" + round.getId() + "\", \"message\":\"Unable to parse round\"}");
            return responseBuilder.header("Access-Control-Allow-Origin", "http://www.zingarotour.org").build();
        }
    }

    private Round create(Document roundInfo) {
        Course course = new CourseHelper().getById(roundInfo.getString("courseId"));
        if (course == null){
            return null;
        }
        Player player = new PlayerHelper().getById(roundInfo.getString("playerId"));
        if (player == null){
            return null;
        }
        Tournament tournament = new TournamentHelper().get(roundInfo.getString("tournamentId"));
        DayOfEvent dayOfEvent = new DayOfEvent();
        if (tournament == null){
            dayOfEvent.setDay(roundInfo.getInteger("day"))
                    .setMonth(roundInfo.getInteger("month"))
                    .setYear(roundInfo.getInteger("year"));
        }else{
            dayOfEvent = tournament.getDayOfEvent();
        }
        String[] tokens = roundInfo.getString("scorecard").split(" ");
        Round round = new Round().setDayOfEvent(dayOfEvent)
                .setCourseId(course.getId())
                .setCourseName(course.getName())
                .setPlayerId(player.getId())
                .setPlayerName(player.getName());
        if (tournament != null){
            round.setTournamentId(tournament.getId()).setTournamentName(tournament.getTitle());
        }
        Scorecard scorecard = new Scorecard().setPhcp(Integer.parseInt(tokens[0]));
        for (int i = 1; i <= 18; i++){
            Score score = new Score().setHcp(course.getHole(i).getHcp())
                    .setPar(course.getHole(i).getPar())
                    .setStrokes(Integer.parseInt(tokens[i]));
            scorecard.setScore(i, score);
        }
        round.setScorecard(scorecard);
        return round;
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
            Response.ResponseBuilder responseBuilder = Response.ok(collectionToJson(rounds));
            return responseBuilder.header("Access-Control-Allow-Origin", "http://www.zingarotour.org").build();
        }
        Response.ResponseBuilder responseBuilder = Response.status(404);
        return responseBuilder.header("Access-Control-Allow-Origin", "http://www.zingarotour.org").build();
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
