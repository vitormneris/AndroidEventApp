package edu.fatecitaquera.eventapp.dao;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
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
import edu.fatecitaquera.eventapp.model.Event;
import edu.fatecitaquera.eventapp.model.User;

public class EventDAO {
    public Event findById(String id) {
        Event event = new Event();

        try {
            FindByIdRequest findByIdRequest = new FindByIdRequest();
            String jsonString = findByIdRequest.execute(id).get();

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> map = objectMapper.readValue(jsonString, Map.class);
            event.setId((String) map.get("id"));
            event.setName((String) map.get("name"));
            event.setStartEvent((String) map.get("startEvent"));
            event.setFinishEvent((String) map.get("finishEvent"));

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return event;
    }

    public List<Event> findAll() {
        List<Event> events = new ArrayList<>();
        try {
            FindAllRequest findAllRequest = new FindAllRequest();
            String jsonString = findAllRequest.execute().get();
            Log.i("JS0000N", jsonString);
            ObjectMapper objectMapper = new ObjectMapper();

            int start = 0, startUser= 0;
            int finish = 0, finishUser = 0;
            int count = 0;
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
                    String json = jsonString.substring(start, finish) + '}';
                    Log.i("JSON", json);
                    Map<String, Object> map = objectMapper.readValue(json, Map.class);
                    Event event = new Event();
                    event.setId((String) map.get("id"));
                    event.setName((String) map.get("name"));
                    event.setStartEvent((String) map.get("startEvent"));
                    event.setFinishEvent((String) map.get("finishEvent"));
                    events.add(event);
                    eventId = event.getId();
                    active = true;
                    start = 0;
                    finish= 0;

                }

                if (active && jsonString.charAt(i) == '[') {
                    startUser = i;
                }

                if (startUser != 0 && active && jsonString.charAt(i) == ']') {
                    finishUser = i;
                }

                if (active && startUser != 0 && finishUser != 0) {
                    String json = jsonString.substring(startUser, finishUser + 1);
                    String finalEventId = eventId;
                    Event e = events.stream().filter((event -> event.getId().equals(finalEventId))).findFirst().get();
                    e.setUsers(findAllUser(json));
                    active = false;
                    startUser = 0;
                    finishUser = 0;
                }
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return events;
    }

    public List<User> findAllUser(String jsonString) {
        List<User> users = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {

            int start = 0;
            int finish = 0;
            for (int i = 0; i < jsonString.length(); i++) {
                if (jsonString.charAt(i) == '{') {
                    start = i;
                }

                if (jsonString.charAt(i) == '}') {
                    finish = i;
                }

                if (start != 0 && finish != 0) {
                    String json = jsonString.substring(start, finish + 1);
                    Map<String, Object> map = objectMapper.readValue(json, Map.class);
                    User user = new User();

                    user.setId((String) map.get("id"));
                    user.setName((String) map.get("name"));
                    user.setUserIn((String) map.get("userIn"));
                    user.setUserOut((String) map.get("userOut"));

                    users.add(user);
                    start = 0;
                    finish = 0;

                }
            }

        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    public String insert(Event event){
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String jsonUser = objectMapper.writeValueAsString(event);

            InsertRequest insertRequest = new InsertRequest();
            String jsonString = insertRequest.execute(jsonUser).get();

            Map<String, Object> map = objectMapper.readValue(jsonString, Map.class);
            event.setId((String) map.get("id"));
            event.setName((String) map.get("name"));
            event.setStartEvent((String) map.get("startEvent"));
            event.setFinishEvent((String) map.get("finishEvent"));

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return event.getId();
    }

    public void update(String id, Event event){
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String jsonUser = objectMapper.writeValueAsString(event);

            UpdateRequest updateRequest = new UpdateRequest();
            updateRequest.execute(id, jsonUser).get();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteById(String id){
        try {
            DeleteRequest deleteRequest = new DeleteRequest();
            deleteRequest.execute(id).get();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
