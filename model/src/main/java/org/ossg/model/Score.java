package org.ossg.model;

import org.bson.Document;

public class Score {
    private int par = 0;
    private int hcp = 0;
    private int strokes = 0;
    private int distance = 0;
    private int putts = 0;
    private boolean fareway = false;
    private int bunkers = 0;
    private int penalties = 0;
    private int points = 0;

    public int getPar() {
        return par;
    }

    public Score setPar(int par) {
        this.par = par;
        return this;
    }

    public int getHcp() {
        return hcp;
    }

    public Score setHcp(int hcp) {
        this.hcp = hcp;
        return this;
    }

    public int getStrokes() {
        return strokes;
    }

    public Score setStrokes(int strokes) {
        this.strokes = strokes;
        return this;
    }

    public int getDistance() {
        return distance;
    }

    public Score setDistance(int distance) {
        this.distance = distance;
        return this;
    }

    public int getPutts() {
        return putts;
    }

    public Score setPutts(int putts) {
        this.putts = putts;
        return this;
    }

    public boolean isFareway() {
        return fareway;
    }

    public Score setFareway(boolean fareway) {
        this.fareway = fareway;
        return this;
    }

    public int getPenalties() {
        return penalties;
    }

    public Score setPenalties(int penalties) {
        this.penalties = penalties;
        return this;
    }

    public int getBunkers() {
        return bunkers;
    }

    public Score setBunkers(int bunkers) {
        this.bunkers = bunkers;
        return this;
    }

    public int getPoints() {
        return points;
    }

    public Score setPoints(int points) {
        this.points = points;
        return this;
    }

    @Override
    public String toString(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("{ ");
        buffer.append("\"par\": ").append(par);
        buffer.append(", ");
        buffer.append("\"hcp\": ").append(hcp);
        buffer.append(", ");
        buffer.append("\"strokes\": ").append(strokes);
        buffer.append(", ");
        buffer.append("\"putts\": ").append(putts);
        buffer.append(", ");
        buffer.append("\"bunkers\": ").append(bunkers);
        buffer.append(", ");
        buffer.append("\"penalties\": ").append(penalties);
        buffer.append(", ");
        buffer.append("\"fareway\": ").append(fareway);
        buffer.append(", ");
        buffer.append("\"points\": ").append(points);
        buffer.append(" }");
        return  buffer.toString();
    }

    public String prettyPrint(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("\n{ \n");
        buffer.append("\"par\": ").append(par);
        buffer.append(", \n");
        buffer.append("\"hcp\": ").append(hcp);
        buffer.append(", \n");
        buffer.append("\"strokes\": ").append(strokes);
        buffer.append(", \n");
        buffer.append("\"putts\": ").append(putts);
        buffer.append(", \n");
        buffer.append("\"bunkers\": ").append(bunkers);
        buffer.append(", \n");
        buffer.append("\"penalties\": ").append(penalties);
        buffer.append(", \n");
        buffer.append("\"fareway\": ").append(fareway);
        buffer.append(", \n");
        buffer.append("\"points\": ").append(points);
        buffer.append("\n }\n");
        return  buffer.toString();
    }

    public Document getDocument(){
        return new Document("par", this.par)
                .append("hcp", this.hcp)
                .append("strokes", this.strokes)
                .append("putts", this.putts)
                .append("fareway", this.fareway)
                .append("penalties", this.penalties)
                .append("bunkers", this.bunkers)
                .append("points", this.points);
    }

    public Score build(Document document){
        this.par = document.getInteger("par");
        this.hcp = document.getInteger("hcp");
        this.strokes = document.getInteger("strokes");
        if (document.getInteger("putts") != null){
            this.putts = document.getInteger("putts");
        }
        if (document.getInteger("bunkers") != null){
            this.bunkers = document.getInteger("bunkers");
        }
        if (document.getInteger("penalties") != null){
            this.penalties = document.getInteger("penalties");
        }
        if (document.getBoolean("fareway") != null){
            this.fareway = document.getBoolean("fareway");
        }
        if (document.getInteger("points") != null){
            this.points = document.getInteger("points");
        }
        return this;
    }

    public Score build(String score){
        // HOLE, strokes, putts, points, bunkers, penalties, fareway
        String data_delimiter = "_";
        String[] data = score.split(data_delimiter);
        this.setStrokes(Integer.parseInt(data[1]));
        this.setPutts(Integer.parseInt(data[2]));
        this.setPoints(Integer.parseInt(data[3]));
        this.setBunkers(Integer.parseInt(data[4]));
        this.setPenalties(Integer.parseInt(data[5]));
        this.setFareway(Boolean.parseBoolean(data[6]));
        return this;
    }

    private int calculateAdditionalStrokes(int phcp){
        if (phcp == 18){
            return 1;
        }
        if (phcp > 18){
            if ((phcp - 18) >= hcp) {
                return 2;
            } else {
                return 1;
            }
        }
        if (phcp >= hcp){
            return 1;
        }
        return 0;
    }

    public void calculatePoints(int phcp){
        int stb = ((par + calculateAdditionalStrokes(phcp)) - strokes) + 2;
        if (stb < 0){
            stb = 0;
        }
        this.points = stb;
    }

}