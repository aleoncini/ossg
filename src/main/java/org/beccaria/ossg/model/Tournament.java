package org.beccaria.ossg.model;

import org.bson.Document;

import java.util.UUID;

public class Tournament {
    private String id = UUID.randomUUID().toString().replace("-", "");
    private String title = "";
    private DayOfEvent dayOfEvent;

    public String getId() {
        return this.id;
    }

    public Tournament setId(String id) {
        this.id = id;
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

    @Override
    public String toString(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("{ ");
        buffer.append("\"id\": \"").append(id).append("\",");
        buffer.append("\"title\": \"").append(title).append("\",");
        buffer.append("\"dayOfEvent\": ").append(dayOfEvent.toString());
        buffer.append(" }");
        return  buffer.toString();
    }

    public String prettyPrint() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("\n{ \n");
        buffer.append("\"id\": \"").append(id).append("\"");
        buffer.append(", \n");
        buffer.append("\"title\": \"").append(title).append("\"");
        buffer.append(", \n");
        buffer.append("\"dayOfEvent\": ").append(dayOfEvent.prettyPrint());
        buffer.append("\n }\n");
        return  buffer.toString();
    }

    public Document getDocument(){
        Document doc = new Document("id", this.id)
                .append("title", this.title)
                .append("dayOfEvent", this.dayOfEvent.getDocument());
        return doc;
    }

    public Tournament build(Document document){
        this.id = document.getString("id");
        this.title = document.getString("title");
        Document dayOfEventDocument = (Document) document.get("dayOfEvent");
        this.dayOfEvent = new DayOfEvent().build(dayOfEventDocument);
        return this;
    }
}