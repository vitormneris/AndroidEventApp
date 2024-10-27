package edu.fatecitaquera.eventapp.model;

import java.util.ArrayList;
import java.util.List;

public class Event {
    private String id;
    private String name;
    private String startEvent;
    private String finishEvent;

    private List<User> users = new ArrayList<>();

    public Event(String id, String name, String startEvent, String finishEvent) {
        this.id = id;
        this.name = name;
        this.startEvent = startEvent;
        this.finishEvent = finishEvent;
    }

    public Event() {
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

    public String getStartEvent() {
        return startEvent;
    }

    public void setStartEvent(String startEvent) {
        this.startEvent = startEvent;
    }

    public String getFinishEvent() {
        return finishEvent;
    }

    public void setFinishEvent(String finishEvent) {
        this.finishEvent = finishEvent;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
