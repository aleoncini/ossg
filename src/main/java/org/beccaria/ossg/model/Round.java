package org.beccaria.ossg.model;

import org.bson.Document;

public class Round implements Comparable<Round>{
    public final static String ROUND_TYPE_TOURNAMENT = "Tournament";
    public final static String ROUND_TYPE_PRACTICE = "Practice";

    private String id;
    private DayOfEvent dayOfEvent;
    private String playerId;
    private String playerName;
    private String tournamentId;
    private String tournamentName;
    private boolean isNineHole = false;
    private String courseId;
    private String courseName;
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

    public String getCourseId() {
        return courseId;
    }

    public Round setCourseId(String id) {
        this.courseId = id;
        return this;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Round setPlayerName(String name) {
        this.playerName = name;
        return this;
    }

    public String getCourseName() {
        return courseName;
    }

    public Round setCourseName(String name) {
        this.courseName = name;
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

    public String getTournamentName() {
        return tournamentName;
    }

    public Round setTournamentName(String name) {
        this.tournamentName = name;
        return this;
    }

    public boolean isNineHole(){
        return isNineHole;
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
        if (tournamentId != null){
            buffer.append(", ");
            buffer.append("\"tournamentId\": \"").append(tournamentId).append("\"");
            buffer.append(", \"tournamentName\": \"").append(tournamentName).append("\"");
        }
        if (isNineHole){
            buffer.append(", ");
            buffer.append("\"isNineHole\": ").append(isNineHole);
        }
        buffer.append(", ");
        buffer.append("\"courseId\": \"").append(courseId).append("\"");
        buffer.append(", ");
        buffer.append("\"courseName\": \"").append(courseName).append("\"");
        buffer.append(", ");
        buffer.append("\"scorecard\": ").append(scorecard.toString());
        buffer.append(" }");
        return  buffer.toString();
    }

    public Round build(Document roundDocument){
        this.id = roundDocument.getString("id");

        Document dayOfEventDocument = (Document) roundDocument.get("dayOfEvent");
        this.dayOfEvent = new DayOfEvent().build(dayOfEventDocument);

        this.playerId = roundDocument.getString("playerId");
        this.playerName = roundDocument.getString("playerName");
        this.courseId = roundDocument.getString("courseId");
        this.courseName = roundDocument.getString("courseName");

        if (roundDocument.get("tournamentId") != null){
            String tid = roundDocument.getString("tournamentId");
            if (tid.length() > 0){
                this.tournamentId = tid;
                this.tournamentName = roundDocument.getString("tournamentName");
            }
        }

        if ((roundDocument.getBoolean("isNineHole") != null) && roundDocument.getBoolean("isNineHole") ){
            this.setNineHole();
        }

        Document scorecardDocument = (Document) roundDocument.get("scorecard");
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

    public Document getDocument(){
        if (id == null){
            return null;
        }
        Document document = new Document("id", this.id)
                .append("dayOfEvent", this.dayOfEvent.getDocument())
                .append("playerId", this.playerId)
                .append("playerName", this.playerName);
        if (tournamentId != null) {
            document.append("tournamentId", this.tournamentId)
                    .append("tournamentName", this.tournamentName);
        }
        if (isNineHole) {
            document.append("isNineHole", this.isNineHole);
        }
        document.append("courseId", this.courseId)
                .append("courseName", this.courseName)
                .append("scorecard", this.scorecard.getDocument());
        return document;
    }

    public String result() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("{ ");
        buffer.append("\"roundid\": \"").append(this.id).append("\"");
        buffer.append(", ");
        buffer.append("\"playerName\": \"").append(playerName).append("\"");
        buffer.append(", ");
        buffer.append("\"course\": \"").append(courseName).append("\"");
        buffer.append(", ");
        buffer.append("\"mdl\": ").append(scorecard.getMedal());
        buffer.append(", ");
        buffer.append("\"phcp\": ").append(scorecard.getPhcp());
        if (scorecard.getPhcp() >= 0){
            buffer.append(", ");
            buffer.append("\"net\": ").append(scorecard.getNetMedal());
            buffer.append(", ");
            buffer.append("\"stb\": ").append(scorecard.getStableford());
        }
        buffer.append(" }");
        return  buffer.toString();
    }

}
