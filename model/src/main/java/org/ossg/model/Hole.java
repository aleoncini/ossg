package org.ossg.model;

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
        buffer.append("\"distance\": ").append(distance);
        buffer.append(", ");
        buffer.append("\"par\": ").append(par);
        buffer.append(", ");
        buffer.append("\"hcp\": ").append(hcp);
        buffer.append(" }");
        return  buffer.toString();
    }

    public Document getDocument(){
        return new Document("distance", this.distance).append("hcp", this.hcp).append("par", this.par);
    }

    public Hole build(Document document){
        this.distance = document.getInteger("distance");
        this.hcp = document.getInteger("hcp");
        this.par = document.getInteger("par");
        return this;
    }
}
