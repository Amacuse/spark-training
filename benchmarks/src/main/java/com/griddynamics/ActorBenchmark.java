package com.griddynamics;

import com.datastax.driver.core.*;
import com.griddynamics.dao.codec.ActorCodec;
import com.griddynamics.dao.codec.MovieCodec;
import com.griddynamics.dao.impl.ActorDaoImpl;
import com.griddynamics.dao.model.Actor;
import com.griddynamics.dao.model.Movie;
import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.List;

public class ActorBenchmark {

    public static void main(String[] args) throws Exception {
        Main.main(args);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void measureActorsAsJson(ActorTestPlan actorTestPlan, Blackhole blackhole) {
        List<Actor> asJSON = actorTestPlan.actorDao.getAsJSON();
        blackhole.consume(asJSON);

    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void measureActorsAsObject(ActorTestPlan actorTestPlan, Blackhole blackhole) {
        List<Actor> asObject = actorTestPlan.actorDao.getAsObject();
        blackhole.consume(asObject);
    }

    @State(Scope.Benchmark)
    public static class ActorTestPlan {
        public static final String ACTORS_BY_ID_JSON = "actors_by_id_json";
        public static final String ACTORS_BY_ID_OBJECT = "actors_by_id_object";
        public Cluster cluster;
        public ActorDaoImpl actorDao;

        public ActorTestPlan() {
        }

        @Setup(Level.Trial)
        public void setUp() {
            actorDao = new ActorDaoImpl();
            actorDao.init();
            actorDao.setActorsByIdJson(ACTORS_BY_ID_JSON);
            actorDao.setActorsByIdObject(ACTORS_BY_ID_OBJECT);
            cluster = Cluster.builder()
                    .addContactPoint("localhost")
                    .build();
            KeyspaceMetadata keyspace = cluster.getMetadata().getKeyspace("cassandra_learning");
            UserType actor = keyspace.getUserType("actor");
            UserType movie = keyspace.getUserType("movie");
            CodecRegistry codecRegistry = cluster.getConfiguration().getCodecRegistry();
            TypeCodec<UDTValue> actorTypeCodec = codecRegistry.codecFor(actor);
            TypeCodec<UDTValue> movieTypeCodec = codecRegistry.codecFor(movie);
            ActorCodec actorCodec = new ActorCodec(actorTypeCodec, Actor.class);
            MovieCodec movieCodec = new MovieCodec(movieTypeCodec, Movie.class);
            codecRegistry.register(actorCodec, movieCodec);
            Session session = cluster.connect("cassandra_learning");
            actorDao.setSession(session);
        }

        @TearDown
        public void tearDown() {
            cluster.close();
        }
    }
}
