package org.beccaria.ossg.model;

import org.bson.Document;

public class ResponseInfo {
    private String status = "error";
    private String message = "";
    private String exception = "";
    private String id = "";

    public final static String ERROR_STATUS = "ERROR";
    public final static String SUCCESS_STATUS = "success";

    public String getStatus() {
        return status;
    }

    public ResponseInfo setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ResponseInfo setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getException() {
        return exception;
    }

    public ResponseInfo setException(String exception) {
        this.exception = exception;
        return this;
    }

    public String getId() {
        return id;
    }

    public ResponseInfo setId(String id) {
        this.id = id;
        return this;
    }

    @Override
    public String toString(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("{ ");
        buffer.append("\"status\": \"").append(status).append("\",");
        buffer.append("\"message\": \"").append(message).append("\",");
        buffer.append("\"exception\": \"").append(exception).append("\",");
        buffer.append("\"id\": \"").append(id).append("\"");
        buffer.append(" }");
        return  buffer.toString();
    }

    public Document getDocument(){
        Document doc = new Document("status", this.status)
                .append("message", this.message)
                .append("exception", this.exception)
                .append("id", this.id);
        return doc;
    }

    public ResponseInfo build(Document document){
        this.status = document.getString("status");
        this.message = document.getString("message");
        this.exception = document.getString("exception");
        this.id = document.getString("id");
        return this;
    }
}
