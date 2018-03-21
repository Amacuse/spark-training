package com.griddynamics.controllers;

import com.griddynamics.ActorDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/actor")
public class ActorController {

    @Autowired
    private ActorDao actorDao;

    @RequestMapping(method = RequestMethod.GET, path = "/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getActorAsJson(){
        return ResponseEntity.ok(actorDao.getAsJSON());
    }

    @RequestMapping(method = RequestMethod.GET, path = "/object", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getActorAsObject(){
        return ResponseEntity.ok(actorDao.getAsObject());
    }
}
