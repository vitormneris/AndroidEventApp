package edu.fatecitaquera.eventapp.model;

public class User {
    private String id;
    private String name;
    private String userIn;
    private String userOut;

    public User(String id, String name, String userIn, String userOut) {
        this.id = id;
        this.name = name;
        this.userIn = userIn;
        this.userOut = userOut;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserIn() {
        return userIn;
    }

    public void setUserIn(String userIn) {
        this.userIn = userIn;
    }

    public String getUserOut() {
        return userOut;
    }

    public void setUserOut(String userOut) {
        this.userOut = userOut;
    }
}
