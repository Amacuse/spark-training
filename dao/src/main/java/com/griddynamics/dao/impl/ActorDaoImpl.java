package com.griddynamics.dao.impl;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.griddynamics.ActorDao;
import com.griddynamics.dao.model.Actor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.Spliterator;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@Service
public class ActorDaoImpl implements ActorDao {

    @Autowired
    private Session session;
    @Value("${cassandra.table.actors.json}")
    private String actorsByIdJson;
    @Value("${cassandra.table.actors.object}")
    private String actorsByIdObject;
    @Value("${cassandra.data.amount}")
    private int dataAmount;

    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public List<Actor> getAsJSON() {
        return StreamSupport.stream(getRows(actorsByIdJson), false)
                .map(row -> {
                    try {
                        return objectMapper.readValue(row.getString("actor"), Actor.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .collect(toList());
    }

    @Override
    public List<Actor> getAsObject() {
        return StreamSupport.stream(getRows(actorsByIdObject), false)
                .map(row -> row.get("actor", Actor.class))
                .collect(toList());
    }

    private Spliterator<Row> getRows(String table) {
        return session.execute(QueryBuilder
                .select()
                .from(table)
                .setFetchSize(dataAmount)).spliterator();
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public String getActorsByIdJson() {
        return actorsByIdJson;
    }

    public void setActorsByIdJson(String actorsByIdJson) {
        this.actorsByIdJson = actorsByIdJson;
    }

    public String getActorsByIdObject() {
        return actorsByIdObject;
    }

    public void setActorsByIdObject(String actorsByIdObject) {
        this.actorsByIdObject = actorsByIdObject;
    }
}
