package org.beccaria.ossg.model;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Tournament {
    public final static int STABLEFORD_TYPE = 0;
    public final static int MEDAL_TYPE = 1;

    private String id;
    private String title;
    private String ownerId;
    private DayOfEvent dayOfEvent;
    private boolean isOpen = true;
    private int type = STABLEFORD_TYPE;

    public String getId() {
        return this.id;
    }

    public Tournament setId(String id) {
        this.id = id;
        return this;
    }

    public String getOwnerId() {
        return this.ownerId;
    }

    public Tournament setOwnerId(String ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public Tournament setTitle(String title) {
        this.title = title;
        return this;
    }

    public DayOfEvent getDayOfEvent() {
        return this.dayOfEvent;
    }

    public Tournament setDayOfEvent(DayOfEvent dayOfEvent) {
        this.dayOfEvent = dayOfEvent;
        return this;
    }

    public Tournament open(){
        this.isOpen = true;
        return this;
    }

    public Tournament close(){
        this.isOpen = false;
        return this;
    }

    public int getType(){
        return this.type;
    }

    public Tournament setType(int type){
        this.type = type;
        return this;
    }

    @Override
    public String toString(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("{ ");
        buffer.append("\"id\": \"").append(id).append("\",");
        buffer.append("\"ownerId\": \"").append(ownerId).append("\",");
        buffer.append("\"title\": \"").append(title).append("\",");
        buffer.append("\"dayOfEvent\": ").append(dayOfEvent.toString()).append(",");
        buffer.append("\"isOpen\": ").append(this.isOpen).append(",");
        buffer.append("\"type\": ").append(this.type).append(",");
        buffer.append(" }");
        return  buffer.toString();
    }

    public String prettyPrint() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("\n{ \n");
        buffer.append("\"  id\": \"").append(id).append("\",");
        buffer.append(", \n");
        buffer.append("\"  ownerId\": \"").append(ownerId).append("\",");
        buffer.append(", \n");
        buffer.append("\"  title\": \"").append(title).append("\",");
        buffer.append(", \n");
        buffer.append("\"  dayOfEvent\": ").append(dayOfEvent.toString());
        buffer.append(", \n");
        buffer.append("\"  isOpen\": ").append(this.isOpen).append(",");
        buffer.append(", \n");
        buffer.append("\"  type\": ").append(this.type).append(",");
        buffer.append(", \n");
        buffer.append("\n}\n");
        return  buffer.toString();
    }

    public Document getDocument(){
        Document doc = new Document("id", this.id)
                .append("ownerId", this.ownerId)
                .append("title", this.title)
                .append("dayOfEvent", this.dayOfEvent.getDocument())
                .append("isOpen", this.isOpen)
                .append("type", this.type);
        return doc;
    }

    public Tournament build(Document document){
        this.id = document.getString("id");
        this.ownerId = document.getString("ownerId");
        this.title = document.getString("title");
        Document dayOfEventDocument = (Document) document.get("dayOfEvent");
        this.dayOfEvent = new DayOfEvent().build(dayOfEventDocument);
        this.isOpen = document.getBoolean("isOpen", true);
        this.type = document.getInteger("type", STABLEFORD_TYPE);
        return this;
    }
}