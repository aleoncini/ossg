package org.beccaria.ossg.persistence;

import org.beccaria.ossg.model.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;

public class RoundHelper {
    public static String COLLECTION_NAME = "rounds";

    public boolean save(Round round){
        return DBTools.save(COLLECTION_NAME, round.getDocument());
    }

    public boolean deleteRound(String id){
        return DBTools.delete(COLLECTION_NAME, id);
    }

    public Collection<Round> search(String playerId, String tournamentId, String courseId, int year, int month, int day){
        Document filter = new Document();
        if ((playerId != null) && (playerId.length() > 0)){
            filter.append("playerId",playerId);
        }
        if ((tournamentId != null) && (tournamentId.length() > 0)){
            filter.append("tournamentId",tournamentId);
        }
        if ((courseId != null) && (courseId.length() > 0)){
            filter.append("course.id",courseId);
        }
        if (year > 0){
            filter.append("dayOfEvent.year", year);
        }
        if (month > 0){
            filter.append("dayOfEvent.month", month);
        }
        if (day > 0){
            filter.append("dayOfEvent.day", day);
        }
        System.out.println("=====> " + filter.toJson());
        Iterator<Document> docs = DBTools.searchByFilter("rounds", filter);
        Collection<Round> rounds = new ArrayList<Round>();
        while (docs.hasNext()){
            rounds.add(new Round().build(docs.next()));
        }
        return rounds;
    }

    public Round initializeNewRound(String playerId, int phcp, String courseId, String tournamentId){
        DayOfEvent dayOfEvent = new DayOfEvent().today();
        return initializeNewRound(playerId, phcp, courseId, tournamentId, dayOfEvent);
    }

    public Round initializeNewRound(String playerId, int phcp, String courseId, String tournamentId, DayOfEvent dayOfEvent){
        String id = UUID.randomUUID().toString().replace("-", "");
        Player player = new PlayerHelper().getById(playerId);
        Course course = new CourseHelper().getById(courseId);
        Scorecard scorecard = new Scorecard().setPhcp(phcp);
        Round round = new Round()
                .setId(id)
                .setDayOfEvent(dayOfEvent)
                .setPlayerId(playerId)
                .setPlayerName(player.getName())
                .setCourse(course)
                .setScorecard(scorecard);
        if (tournamentId != null){
            round.setType(Round.ROUND_TYPE_TOURNAMENT).setTournamentId(tournamentId);
        }
        if (DBTools.save(COLLECTION_NAME, round.getDocument())){
            return round;
        }
        return null;
    }

    public boolean saveHole(String roundId, String holeId, Score score){
        Document filter = new Document("id",roundId);
        Document values = new Document();
        values.append("scorecard.scores." + holeId + ".strokes",score.getStrokes());
        values.append("scorecard.scores." + holeId + ".putts",score.getPutts());
        values.append("scorecard.scores." + holeId + ".fareway",score.isFareway());
        values.append("scorecard.scores." + holeId + ".bunkers",score.getBunkers());
        values.append("scorecard.scores." + holeId + ".penalties",score.getPenalties());
        Document set = new Document("$set",values);
        return DBTools.update(RoundHelper.COLLECTION_NAME,filter,set);
    }

    public boolean saveScorecard(String roundId, Scorecard scorecard){
        Document filter = new Document("id",roundId);
        Document values = new Document().append("scorecard", scorecard.getDocument());
        Document set = new Document("$set",values);
        return DBTools.update(RoundHelper.COLLECTION_NAME,filter,set);
    }

    public Round getById(String id){
        Document roundDocument = DBTools.getById(COLLECTION_NAME,id);
        if (roundDocument == null){
            return null;
        }
        return new Round().build(roundDocument);
    }

    public Collection<Round> searchByFieldId(String field, String id){
        Collection<Round> rounds = new ArrayList<Round>();
        Iterator<Document> docs = DBTools.search(COLLECTION_NAME,field,id);
        Document doc = null;
        while (docs.hasNext()){
            rounds.add(new Round().build(docs.next()));
        }
        return rounds;
    }

    public boolean roundExists(String playerid, String courseid, DayOfEvent dayOfEvent){
        Document filter = new Document("playerId",playerid)
                .append("course.id",courseid)
                .append("dayOfEvent.day", dayOfEvent.getDay())
                .append("dayOfEvent.month", dayOfEvent.getMonth())
                .append("dayOfEvent.year", dayOfEvent.getYear());
        Iterator<Document> docs = DBTools.searchByFilter("rounds",filter);
        return docs.hasNext();
    }

}