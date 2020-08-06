package org.ossg.store.model;

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

}