package org.beccaria.ossg.model;

import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

public class Scorecard {
    private int phcp;
    private Map<String, Score> scores;

    public Scorecard(){
        phcp = 0;
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

    public Scorecard setScores(String scoresString) {
        // HOLE, strokes, putts, points, bunkers, penalties, fareway
        String hole_delimiter = "-";
        String[] holes = scoresString.split(hole_delimiter);
        for (String hole : holes) {
            this.setScore(hole);
        }
        return this;
    }

    public Scorecard setScore(int holeNumber, Score score) {
        scores.put(Integer.toString(holeNumber), score);
        return this;
    }

    public Scorecard setScore(String scoreString) {
        String data_delimiter = "_";
        String[] data = scoreString.split(data_delimiter);
        int holeNumber = Integer.parseInt(data[0]);
        Score score = new Score().build(scoreString);
        this.setScore(holeNumber,score);
        return this;
    }

    @Override
    public String toString(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("{ ");
        buffer.append("\"phcp\": ").append(phcp);
        buffer.append(", ");
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

    public String prettyPrint(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("\n{ \n");
        buffer.append("\"phcp\": ").append(phcp);
        buffer.append(", \n");
        buffer.append("\"scores\": { \n");
        for (int i =1; i<=18; i++) {
            if (i>1){
                buffer.append(", \n");
            }
            buffer.append("\"").append(i).append("\": ").append(scores.get(Integer.toString(i)).toString());
        }
        buffer.append(" }\n }\n");
        return  buffer.toString();
    }

    public Document getDocument(){
        Document document = new Document("phcp", this.phcp);
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
        this.phcp = document.getInteger("phcp");
        Document scoreDocuments = (Document) document.get("scores");
        for (int i=1; i<=18; i++){
            Document scoreDocument = (Document) scoreDocuments.get(Integer.toString(i));
            Score score = new Score().build(scoreDocument);
            this.setScore(i, score);
        }
        return this;
    }

}
