package org.ossg.model;

import org.bson.Document;

public class Player {
    private String id = "";
    private String name = "";
    private String email = "";

    public String getId() {
        return id;
    }

    public Player setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Player setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Player setEmail(String email) {
        this.email = email;
        return this;
    }

    @Override
    public String toString(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("{ ");
        buffer.append("\"id\": \"").append(id).append("\",");
        buffer.append("\"name\": \"").append(name).append("\",");
        buffer.append("\"email\": \"").append(email).append("\"");
        buffer.append(" }");
        return  buffer.toString();
    }

    public Document getDocument(){
        Document doc = new Document("id", this.id)
                .append("name", this.name)
                .append("email", email);
        return doc;
    }

    public Player build(Document document){
        this.id = document.getString("id");
        this.name = document.getString("name");
        this.email = document.getString("email");
        return this;
    }
}
