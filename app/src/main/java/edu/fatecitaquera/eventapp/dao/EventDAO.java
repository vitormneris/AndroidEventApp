package edu.fatecitaquera.eventapp.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import edu.fatecitaquera.eventapp.background.DeleteRequest;
import edu.fatecitaquera.eventapp.background.FindAllRequest;
import edu.fatecitaquera.eventapp.background.FindByIdRequest;
import edu.fatecitaquera.eventapp.background.InsertRequest;
import edu.fatecitaquera.eventapp.background.UpdateRequest;
import edu.fatecitaquera.eventapp.background.UserAddEventRequest;
import edu.fatecitaquera.eventapp.model.Event;
import edu.fatecitaquera.eventapp.model.User;

public class EventDAO {
    public Event findById(String id) {

        try {
            FindByIdRequest findByIdRequest = new FindByIdRequest();
            String jsonString = findByIdRequest.execute(id).get();

            if (jsonString == null) return null;

            return convertToEvent(jsonString);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Event> findAll() {
        List<Event> events = new ArrayList<>();
        try {
            FindAllRequest findAllRequest = new FindAllRequest();
            String jsonString = findAllRequest.execute().get();

            if (jsonString == null) return  null;

            int start = 0, startUser= 0, finish = 0, finishUser = 0, count = 0;
            boolean active = false;
            String eventId = null;
            for (int i = 0; i < jsonString.length(); i++) {
                if (!active && jsonString.charAt(i) == '{') {
                    start = i;
                    count = 0;
                }

                if (!active && jsonString.charAt(i) == ',') {
                    count++;
                    if (count == 4) {
                        finish = i;
                        count = 0;
                    }
                }

                if (!active && start != 0 && finish != 0) {
                    Event event = convertToEvent(jsonString.substring(start, finish) + '}');
                    events.add(event);
                    eventId = event.getId();
                    active = true;
                    start = 0;
                    finish= 0;
                }

                if (active && jsonString.charAt(i) == '[') startUser = i;

                if (startUser != 0 && active && jsonString.charAt(i) == ']') finishUser = i;

                if (active && startUser != 0 && finishUser != 0) {
                    String finalEventId = eventId;
                    Event event = events.stream().filter((e -> e.getId().equals(finalEventId))).findFirst().get();
                    event.setUsers(findAllUser(jsonString.substring(startUser, finishUser + 1)));
                    active = false;
                    startUser = 0;
                    finishUser = 0;
                }
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return events;
    }

    public List<User> findAllUser(String jsonString) {
        List<User> users = new ArrayList<>();

        int start = 0, finish = 0;
        for (int i = 0; i < jsonString.length(); i++) {
            if (jsonString.charAt(i) == '{') start = i;

            if (jsonString.charAt(i) == '}') finish = i;

            if (start != 0 && finish != 0) {
                users.add(convertToUser(jsonString, start, finish));
                start = 0;
                finish = 0;
            }
        }
        return users;
    }

    public boolean insert(Event event){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            InsertRequest insertRequest = new InsertRequest();
            return insertRequest.execute(objectMapper.writeValueAsString(event)).get();
        } catch (ExecutionException | InterruptedException |JsonProcessingException  e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(String id, Event event) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            UpdateRequest updateRequest = new UpdateRequest();
            return updateRequest.execute(id, objectMapper.writeValueAsString(event)).get();
        } catch (ExecutionException | InterruptedException |JsonProcessingException  e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteById(String id) {
        try {
            DeleteRequest deleteRequest = new DeleteRequest();
            return deleteRequest.execute(id).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean eventAddUser(String userId, String eventId) {
        try {
            UserAddEventRequest userAddEventRequest = new UserAddEventRequest();
            return userAddEventRequest.execute(userId, eventId).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
    private Event convertToEvent(String jsonString) {
        Event event = new Event();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            Map<String, Object> map = objectMapper.readValue(jsonString, Map.class);
            event.setId((String) map.get("id"));
            event.setName((String) map.get("name"));
            event.setStartEvent((String) map.get("startEvent"));
            event.setFinishEvent((String) map.get("finishEvent"));
            return event;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private User convertToUser(String jsonString, int start, int finish) {
        User user = new User();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            Map<String, Object> map = objectMapper.readValue(jsonString.substring(start, finish + 1), Map.class);
            user.setId((String) map.get("id"));
            user.setName((String) map.get("name"));
            user.setUserIn((String) map.get("userIn"));
            user.setUserOut((String) map.get("userOut"));
            return user;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
