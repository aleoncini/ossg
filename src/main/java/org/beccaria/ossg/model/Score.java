package org.beccaria.ossg.model;

import org.bson.Document;

public class Score {
    private int par = 0;
    private int hcp = 0;
    private int strokes = 0;
    private int distance = 0;
    private int putts = -1;
    private boolean fareway = false;
    private int bunkers = -1;
    private int penalties = -1;
    private int points = -1;

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
        buffer.append("\"strokes\": ").append(strokes);
        if (par > 0){
            buffer.append(", ");
            buffer.append("\"par\": ").append(par);
        }
        if (hcp > 0){
            buffer.append(", ");
            buffer.append("\"hcp\": ").append(hcp);
        }
        if (putts >= 0){
            buffer.append(", ");
            buffer.append("\"putts\": ").append(putts);
        }
        if (distance > 0){
            buffer.append(", ");
            buffer.append("\"distance\": ").append(distance);
        }
        if (bunkers > 0){
            buffer.append(", ");
            buffer.append("\"bunkers\": ").append(bunkers);
        }
        if (penalties > 0){
            buffer.append(", ");
            buffer.append("\"penalties\": ").append(penalties);
        }
        if (fareway){
            buffer.append(", ");
            buffer.append("\"fareway\": ").append(fareway);
        }
        if (points >= 0){
            buffer.append(", ");
            buffer.append("\"points\": ").append(points);
        }
        buffer.append(" }");
        return  buffer.toString();
    }

    public Document getDocument(){
        Document d = new Document("strokes", this.strokes);
        if (par > 0){
            d.append("par", this.par);
        }
        if (hcp > 0){
            d.append("hcp", this.hcp);
        }
        if (distance > 0){
            d.append("distance", this.distance);
        }
        if (putts >= 0){
            d.append("putts", this.putts);
        }
        if (fareway){
            d.append("fareway", this.fareway);
        }
        if (penalties > 0){
            d.append("penalties", this.penalties);
        }
        if (bunkers > 0){
            d.append("bunkers", this.bunkers);
        }
        if (points >= 0){
            d.append("points", this.points);
        }
        return d;
    }

    public Score build(Document document){
        this.strokes = document.getInteger("strokes");
        if (document.get("par") != null){
            this.par = document.getInteger("par");
        }
        if (document.get("hcp") != null){
            this.hcp = document.getInteger("hcp");
        }
        if (document.get("distance") != null){
            this.distance = document.getInteger("distance");
        }
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
