package org.beccaria.ossg.model;

import org.bson.Document;

public class Hole {
    private int distance=0;
    private int hcp=0;
    private int par=0;

    public int getDistance() {
        return distance;
    }

    public Hole setDistance(int distance) {
        this.distance = distance;
        return this;
    }

    public int getHcp() {
        return hcp;
    }

    public Hole setHcp(int hcp) {
        this.hcp = hcp;
        return this;
    }

    public int getPar() {
        return par;
    }

    public Hole setPar(int par) {
        this.par = par;
        return this;
    }

    @Override
    public String toString(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("{ ");
        buffer.append("\"par\": ").append(par);
        buffer.append(", ");
        buffer.append("\"hcp\": ").append(hcp);
        if (distance > 0){
            buffer.append(", ");
            buffer.append("\"distance\": ").append(distance);
        }
        buffer.append(" }");
        return  buffer.toString();
    }

    public Document getDocument(){
        Document document = new Document("hcp", this.hcp).append("par", this.par);
        if (distance > 0){
            document.append("distance", this.distance);
        }
        return document;
    }

    public Hole build(Document document){
        if (document.get("distance") != null){
            this.distance = document.getInteger("distance");
        }
        this.hcp = document.getInteger("hcp");
        this.par = document.getInteger("par");
        return this;
    }
}
