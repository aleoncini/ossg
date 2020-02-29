package org.beccaria.ossg.model;

import org.bson.Document;

public class Tournament {
    private String id;
    private String title;
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
        if (id == null || title == null || dayOfEvent == null){
            return null;
        }
        StringBuffer buffer = new StringBuffer();
        buffer.append("{ ");
        buffer.append("\"id\":\"").append(id).append("\", ");
        buffer.append("\"title\": \"").append(title).append("\", ");
        buffer.append("\"dayOfEvent\": ").append(dayOfEvent.toString());
        buffer.append(" }");
        return  buffer.toString();
    }

    public Document getDocument(){
        if (id == null || title == null || dayOfEvent == null){
            return null;
        }
        Document doc = new Document("id", this.id)
                .append("title", this.title)
                .append("dayOfEvent", this.dayOfEvent.getDocument());
        return doc;
    }

    public Tournament build(String jsonString){
        return this.build(Document.parse(jsonString));
    }

    public Tournament build(Document document){
        if (document.get("id") != null){
            this.id = document.getString("id");
        }

        if (document.get("title") != null){
            this.title = document.getString("title");
        }

        if (document.get("dayOfEvent") != null){
            Document dayOfEventDocument = (Document) document.get("dayOfEvent");
            this.dayOfEvent = new DayOfEvent().build(dayOfEventDocument);
        }

        return this;
    }
}
