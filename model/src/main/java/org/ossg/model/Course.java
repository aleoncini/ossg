package org.ossg.model;

import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

public class Course {
    private String id;
    private String name;
    private String address;
    private String city;
    private String country;
    private String cap;
    private String website;
    private Position position;
    private Map<String, Hole> holes = new HashMap<String, Hole>();

    public String getId() {
        return id;
    }

    public Course setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Course setName(String name) {
        this.name = name;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public Course setAddress(String address) {
        this.address = address;
        return this;
    }

    public Position getPosition() {
        return position;
    }

    public Course setPosition(Position position) {
        this.position = position;
        return this;
    }

    public String getCity() {
        return city;
    }

    public Course setCity(String city) {
        this.city = city;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public Course setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getCap() {
        return cap;
    }

    public Course setCap(String cap) {
        this.cap = cap;
        return this;
    }

    public String getWebsite() {
        return website;
    }

    public Course setWebsite(String website) {
        this.website = website;
        return this;
    }

    public Map<String, Hole> getHoles() {
        return holes;
    }

    public Hole getHole(int holeNumber) {
        String holeName = String.valueOf(holeNumber);
        return holes.get(holeName);
    }

    public Course setHoles(Hole[] holes) {
        for (int i=1; i<=18; i++){
            String holeName = String.valueOf(i);
            this.holes.put(holeName, holes[i]);
        }
        return this;
    }

    public Course setHole(int holeNumber, Hole hole) {
        String holeName = String.valueOf(holeNumber);
        holes.put(holeName, hole);
        return this;
    }

    @Override
    public String toString(){
        boolean notFirst = false;
        StringBuffer buffer = new StringBuffer();
        buffer.append("{ ");

        if (id != null){
            buffer.append("\"id\": \"").append(id).append("\"");
            notFirst = true;
        }

        if (name != null){
            if (notFirst) buffer.append(", ");
            buffer.append("\"name\": \"").append(name).append("\"");
            notFirst = true;
        }

        if (address != null){
            if (notFirst) buffer.append(", ");
            buffer.append("\"address\": \"").append(address).append("\"");
            notFirst = true;
        }

        if (city != null){
            if (notFirst) buffer.append(", ");
            buffer.append("\"city\": \"").append(city).append("\"");
            notFirst = true;
        }

        if (country != null){
            if (notFirst) buffer.append(", ");
            buffer.append("\"country\": \"").append(country).append("\"");
            notFirst = true;
        }

        if (cap != null){
            if (notFirst) buffer.append(", ");
            buffer.append("\"cap\": \"").append(cap).append("\"");
            notFirst = true;
        }

        if (website != null){
            if (notFirst) buffer.append(", ");
            buffer.append("\"website\": \"").append(website).append("\"");
            notFirst = true;
        }

        if (position != null){
            if (notFirst) buffer.append(", ");
            buffer.append("\"position\": \"").append(position.toString()).append("\"");
            notFirst = true;
        }

        if (holes != null){
            if (notFirst) buffer.append(", ");
            buffer.append("\"holes\": { ");
            for (int i=1; i<=18; i++) {
                String holeName = String.valueOf(i);
                if (i>1){
                    buffer.append(", ");
                }
                buffer.append("\"").append(holeName).append("\": ").append(holes.get(holeName).toString());
            }
            buffer.append(" }");
        }
        buffer.append(" }");
        return  buffer.toString();
    }

    public Document getDocument(){
        Document document = new Document("id", this.id).append("name", this.name);

        if (address != null){
            document.append("address", address);
        }

        if (city != null){
            document.append("city", city);
        }

        if (country != null){
            document.append("country", country);
        }

        if (cap != null){
            document.append("cap", cap);
        }

        if (website != null){
            document.append("website", website);
        }

        if (position != null){
            document.append("website", position.getDocument());
        }

        Document holeCollection = new Document();
        for (int i=1; i<=18; i++){
            String holeName = String.valueOf(i);
            holeCollection.append(holeName, holes.get(holeName).getDocument());
        }
        document.append("holes",holeCollection);
        return document;
    }

    public Course build(String jsonString){
        return this.build(Document.parse(jsonString));
    }

    public Course build(Document courseDocument){

        id = courseDocument.getString("id");
        name = courseDocument.getString("name");

        if (courseDocument.containsKey("address")){
            address = courseDocument.getString("address");
        }
        if (courseDocument.containsKey("city")){
            city = courseDocument.getString("city");
        }
        if (courseDocument.containsKey("country")){
            country = courseDocument.getString("country");
        }
        if (courseDocument.containsKey("cap")){
            cap = courseDocument.getString("cap");
        }
        if (courseDocument.containsKey("website")){
            website = courseDocument.getString("website");
        }
        if (courseDocument.containsKey("position")){
            Document positionDocument = (Document) courseDocument.get("position");
            position = new Position().build(positionDocument);
        }

        Document holeDocuments = (Document) courseDocument.get("holes");
        for (int i=1; i<=18; i++){
            String holeName = String.valueOf(i);
            Document holeDocument = (Document) holeDocuments.get(holeName);
            Hole hole = new Hole().build(holeDocument);
            this.setHole(i,hole);
        }

        return this;
    }

}
