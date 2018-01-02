package org.beccaria.ossg.model;

import org.bson.Document;

public class Round implements Comparable<Round>{
    public final static String ROUND_TYPE_TOURNAMENT = "Tournament";
    public final static String ROUND_TYPE_PRACTICE = "Practice";

    private String id;
    private DayOfEvent dayOfEvent;
    private String playerId;
    private String playerName;
    private String type = ROUND_TYPE_PRACTICE;
    private String tournamentId = "";
    private boolean isNineHole = false;
    private Course course;
    private Scorecard scorecard;

    public String getId() {
        return id;
    }

    public Round setId(String id) {
        this.id = id;
        return this;
    }

    public DayOfEvent getDayOfEvent(){
        return this.dayOfEvent;
    }

    public Round setDayOfEvent(DayOfEvent dayOfEvent) {
        this.dayOfEvent = dayOfEvent;
        return this;
    }

    public String getPlayerId() {
        return playerId;
    }

    public Round setPlayerId(String id) {
        this.playerId = id;
        return this;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Round setPlayerName(String name) {
        this.playerName = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public Round setType(String type) {
        this.type = type;
        return this;
    }

    public String getTournamentId() {
        return tournamentId;
    }

    public Round setTournamentId(String tournamentId) {
        this.tournamentId = tournamentId;
        return this;
    }

    public Round setNineHole(){
        this.isNineHole = true;
        return this;
    }

    public boolean isNineHole(){
        return isNineHole;
    }

    public Course getCourse() {
        return this.course;
    }

    public Round setCourse(Course course) {
        this.course = course;
        return this;
    }

    public Scorecard getScorecard() {
        return this.scorecard;
    }

    public Round setScorecard(Scorecard scorecard) {
        this.scorecard = scorecard;
        return this;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("{ ");
        buffer.append("\"id\": \"").append(id).append("\"");
        buffer.append(", ");
        buffer.append("\"dayOfEvent\": ").append(dayOfEvent.toString());
        buffer.append(", ");
        buffer.append("\"playerId\": \"").append(playerId).append("\"");
        buffer.append(", ");
        buffer.append("\"playerName\": \"").append(playerName).append("\"");
        buffer.append(", ");
        buffer.append("\"type\": \"").append(type).append("\"");
        if (type.equals(ROUND_TYPE_TOURNAMENT)){
            buffer.append(", ");
            buffer.append("\"tournamentId\": \"").append(tournamentId).append("\"");
        }
        buffer.append(", ");
        buffer.append("\"isNineHole\": \"").append(isNineHole).append("\"");
        buffer.append(", ");
        buffer.append("\"course\": ").append(course.toString());
        buffer.append(", ");
        buffer.append("\"scorecard\": ").append(scorecard.toString());
        buffer.append(" }");
        return  buffer.toString();
    }

    public String prettyPrint() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("\n{ \n");
        buffer.append("\"id\": \"").append(id).append("\"");
        buffer.append(", \n");
        buffer.append("\"dayOfEvent\": ").append(dayOfEvent.toString());
        buffer.append(", \n");
        buffer.append("\"playerId\": \"").append(playerId).append("\"");
        buffer.append(", \n");
        buffer.append("\"playerName\": \"").append(playerName).append("\"");
        buffer.append(", \n");
        buffer.append("\"type\": \"").append(type).append("\"");
        if (type.equals(ROUND_TYPE_TOURNAMENT)){
            buffer.append(", ");
            buffer.append("\"tournamentId\": \"").append(tournamentId).append("\"");
        }
        buffer.append(", \n");
        buffer.append("\"isNineHole\": \"").append(isNineHole).append("\"");
        buffer.append(", \n");
        buffer.append("\"course\": ").append(course.toString());
        buffer.append(", \n");
        buffer.append("\"scorecard\": ").append(scorecard.toString());
        buffer.append("\n }\n");
        return  buffer.toString();
    }

    public Document getDocument(){
        if (id == null){
            return null;
        }
        return new Document("id", this.id)
                .append("dayOfEvent", this.dayOfEvent.getDocument())
                .append("playerId", this.playerId)
                .append("playerName", this.playerName)
                .append("type", this.type)
                .append("tournamentId", this.tournamentId)
                .append("isNineHole", this.isNineHole)
                .append("course", this.course.getDocument())
                .append("scorecard", this.scorecard.getDocument());
    }

    public Round build(Document roundDocument){
        Document dayOfEventDocument = (Document) roundDocument.get("dayOfEvent");
        Document courseDocument = (Document) roundDocument.get("course");
        Document scorecardDocument = (Document) roundDocument.get("scorecard");

        if ((roundDocument.getBoolean("isNineHole") != null) && roundDocument.getBoolean("isNineHole") ){
            this.setNineHole();
        }

        this.id = roundDocument.getString("id");
        this.dayOfEvent = new DayOfEvent().build(dayOfEventDocument);
        this.playerId = roundDocument.getString("playerId");
        this.playerName = roundDocument.getString("playerName");
        this.type = roundDocument.getString("type");
        this.tournamentId = roundDocument.getString("tournamentId");
        this.course = new Course().build(courseDocument);
        this.scorecard = new Scorecard().build(scorecardDocument);

        return this;
    }

    @Override
    public int compareTo(Round other) {
        int diff = this.getScorecard().getStableford() - other.getScorecard().getStableford();
        if (diff == 0){
            diff = this.getScorecard().getLastNineStableford() - other.getScorecard().getLastNineStableford();
        }
        if (diff == 0){
            diff = this.getScorecard().getLastSixStableford() - other.getScorecard().getLastSixStableford();
        }
        if (diff == 0){
            diff = this.getScorecard().getLastThreeStableford() - other.getScorecard().getLastThreeStableford();
        }
        return diff;
    }
}