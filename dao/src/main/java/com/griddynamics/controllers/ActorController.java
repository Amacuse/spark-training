package com.griddynamics.controllers;

import com.griddynamics.ActorDao;
import com.griddynamics.dao.model.Actor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("/actor")
public class ActorController {

    @Autowired
    private ActorDao actorDao;

    @RequestMapping(method = RequestMethod.GET, path = "/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getActorAsJson() {
        List<Actor> asJSON = actorDao.getAsJSON();
        return ResponseEntity.ok(String.format("Read %d actors from cassandra stored as JSON objects", asJSON.size()));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/object", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getActorAsObject() {
        List<Actor> asObjects = actorDao.getAsObject();
        return ResponseEntity.ok(String.format("Read %d actors from cassandra stored as objects", asObjects.size()));
    }
}
