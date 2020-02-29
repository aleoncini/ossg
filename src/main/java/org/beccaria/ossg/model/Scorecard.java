package org.beccaria.ossg.model;

import org.bson.Document;
import java.util.HashMap;
import java.util.Map;

public class Scorecard {
    private int phcp;
    private Map<String, Score> scores;

    public Scorecard(){
        phcp = -1;
        scores = new HashMap<String, Score>();
        for (int i=1; i<=18; i++){
            scores.put(Integer.toString(i), new Score());
        }
    }

    public int getPhcp(){
        return this.phcp;
    }

    public Scorecard setPhcp(int phcp){
        this.phcp = phcp;
        return this;
    }

    public Map<String, Score> getScores() {
        return scores;
    }

    public Scorecard setScores(Map<String, Score> scores) {
        this.scores = scores;
        return this;
    }

    public Score getScore(int holeNumber) {
        return scores.get(Integer.toString(holeNumber));
    }

    public Scorecard setScore(int holeNumber, Score score) {
        if (this.phcp >= 0){
            score.calculatePoints(phcp);
        }
        scores.put(Integer.toString(holeNumber), score);
        return this;
    }

    @Override
    public String toString(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("{ ");
        if (phcp >= 0){
            buffer.append("\"phcp\": ").append(phcp).append(", ");
        }
        buffer.append("\"scores\": { ");
        for (int i=1; i<=18; i++) {
            if (i>1){
                buffer.append(", ");
            }
            buffer.append("\"").append(i).append("\": ").append(scores.get(Integer.toString(i)).toString());
        }
        buffer.append(" } }");
        return  buffer.toString();
    }

    public Document getDocument(){
        Document document = new Document();
        if (phcp >= 0){
            document.append("phcp", this.phcp);
        }
        Document scoreCollection = new Document();
        String holename = null;
        for (int i=1; i<=18; i++){
            holename = Integer.toString(i);
            scoreCollection.append(holename, scores.get(Integer.toString(i)).getDocument());
        }
        document.append("scores",scoreCollection);
        return document;
    }

    public Scorecard build(Document document){
        if (document.get("phcp") != null){
            this.phcp = document.getInteger("phcp");
        }
        Document scoresDocument = (Document) document.get("scores");
        for (int i=1; i<=18; i++){
            Document scoreDocument = (Document) scoresDocument.get(Integer.toString(i));
            Score score = new Score().build(scoreDocument);
            this.setScore(i, score);
        }
        return this;
    }

    public int getMedal(){
        int medal = 0;
        for (int i =1; i<=18; i++) {
            medal += scores.get(Integer.toString(i)).getStrokes();
        }
        return medal;
    }

    public int getNetMedal(){
        int medal = getMedal();
        if (phcp > 0){
            return medal - phcp;
        }
        return medal;
    }

    public int getStableford(){
        if (phcp < 0){
            return 0;
        }
        int stb = 0;
        for (int i =1; i<=18; i++) {
            stb += scores.get(Integer.toString(i)).getPoints();
        }
        return stb;
    }

    public int getLastNineStableford(){
        int stb = 0;
        for (int i=10; i<=18; i++) {
            stb += scores.get(Integer.toString(i)).getPoints();
        }
        return stb;
    }

    public int getLastSixStableford(){
        int stb = 0;
        for (int i=13; i<=18; i++) {
            stb += scores.get(Integer.toString(i)).getPoints();
        }
        return stb;
    }

    public int getLastThreeStableford(){
        int stb = 0;
        for (int i=16; i<=18; i++) {
            stb += scores.get(Integer.toString(i)).getPoints();
        }
        return stb;
    }

    public int getPutts(){
        int putts = 0;
        for (int i=1; i<=18; i++) {
            putts += scores.get(Integer.toString(i)).getPutts();
        }
        return putts;
    }

}
