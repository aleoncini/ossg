package org.beccaria.ossg.model;

import org.bson.Document;

public class Position {
    private double latitude;
    private double longitude;

    public double getLatitude() {
        return latitude;
    }

    public Position setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public Position setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    @Override
    public String toString(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("{ ");
        buffer.append("\"latitude\": ").append(latitude);
        buffer.append(", ");
        buffer.append("\"longitude\": ").append(longitude);
        buffer.append(" }");
        return  buffer.toString();
    }

    public Document getDocument(){
        return new Document("latitude", this.latitude).append("longitude", this.longitude);
    }

    public Position build(Document document){
        this.latitude = document.getDouble("latitude");
        this.longitude = document.getDouble("longitude");
        return this;
    }

}
