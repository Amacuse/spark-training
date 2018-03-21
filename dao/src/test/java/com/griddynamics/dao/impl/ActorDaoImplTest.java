package com.griddynamics.dao.impl;

import com.griddynamics.ActorDao;
import com.griddynamics.dao.config.Application;
import com.griddynamics.dao.model.Actor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
public class ActorDaoImplTest {

    @Autowired
    private ActorDao actorDao;
    @Value("${cassandra.data.amount}")
    private int dataAmount;

    @Test
    public void getAsJSON() {
        List<Actor> actors = actorDao.getAsJSON();
        assertNotNull(actors);
        assertEquals(dataAmount, actors.size());
    }

    @Test
    public void getAsObject() {
        List<Actor> actors = actorDao.getAsObject();
        assertNotNull(actors);
        assertEquals(dataAmount, actors.size());
    }
}