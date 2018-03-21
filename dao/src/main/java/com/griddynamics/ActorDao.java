package com.griddynamics;

import com.griddynamics.dao.model.Actor;

import java.util.List;

public interface ActorDao {

    List<Actor> getAsObject();

    List<Actor> getAsJSON();
}
