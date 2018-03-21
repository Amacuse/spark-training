package com.griddynamics.dao.util;

import com.datastax.driver.core.*;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.collect.Lists;
import com.griddynamics.dao.codec.ActorCodec;
import com.griddynamics.dao.codec.MovieCodec;
import com.griddynamics.dao.model.Actor;
import com.griddynamics.dao.model.Movie;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomUtils.*;

public class PopulateDB {
    private static Properties properties;
    private static Cluster cluster;
    private static Session session;
    private static int testEntities;
    private static ObjectMapper objectMapper;

    public static void main(String[] args) {

        init();

        List<Actor> testActors = createTestActors();

        if (Boolean.valueOf(properties.getProperty("cassandra.populate.test.actors.json"))) {
            Lists.partition(testActors, 10)
                    .stream()
                    .map(actors -> actors
                            .stream()
                            .map(actor -> {
                                String actorJSON = "";
                                try {
                                    actorJSON = objectMapper.writeValueAsString(actor);
                                } catch (JsonProcessingException e) {
                                    e.printStackTrace();
                                }

                                return actorJSON;
                            })
                            .map(actorAsString -> QueryBuilder
                                    .insertInto(properties.getProperty("cassandra.table.actors.json"))
                                    .value("id", UUID.randomUUID())
                                    .value("actor", actorAsString))
                            .collect(Collectors.toList()))
                    .forEach(inserts -> session.execute(new BatchStatement(BatchStatement.Type.LOGGED).addAll(inserts)));
        }

        if (Boolean.valueOf(properties.getProperty("cassandra.populate.test.actors.object"))) {
            Lists.partition(testActors, 10)
                    .stream()
                    .map(actors -> actors
                            .stream()
                            .map(actor -> QueryBuilder
                                    .insertInto(properties.getProperty("cassandra.table.actors.object"))
                                    .value("id", UUID.randomUUID())
                                    .value("actor", actor))
                            .collect(Collectors.toList()))
                    .forEach(inserts -> session.execute(new BatchStatement(BatchStatement.Type.LOGGED).addAll(inserts)));
        }

        cluster.close();
    }

    private static void init() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try (InputStream resource = classLoader.getResourceAsStream("application.properties")) {
            properties = new Properties();
            properties.load(resource);
            cluster = Cluster.builder()
                    .addContactPoint(properties.getProperty("cassandra.host"))
                    .build();
            testEntities = Integer.valueOf(properties.getProperty("cassandra.data.amount"));
            KeyspaceMetadata keyspace = cluster.getMetadata().getKeyspace(properties.getProperty("cassandra.keyspace"));
            UserType actor = keyspace.getUserType("actor");
            UserType movie = keyspace.getUserType("movie");
            CodecRegistry codecRegistry = cluster.getConfiguration().getCodecRegistry();
            TypeCodec<UDTValue> actorTypeCodec = codecRegistry.codecFor(actor);
            TypeCodec<UDTValue> movieTypeCodec = codecRegistry.codecFor(movie);
            ActorCodec actorCodec = new ActorCodec(actorTypeCodec, Actor.class);
            MovieCodec movieCodec = new MovieCodec(movieTypeCodec, Movie.class);
            codecRegistry.register(actorCodec, movieCodec);
            session = cluster.connect(properties.getProperty("cassandra.keyspace"));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static List<Actor> createTestActors() {
        return IntStream.rangeClosed(1, testEntities)
                .mapToObj(index -> new Actor.ActorBuilder(randomAlphabetic(15), randomAlphabetic(15))
                        .withBirthday(LocalDate.ofEpochDay(nextLong(1L, 1_000_000L)))
                        .withFirstMovieDate(LocalDate.ofEpochDay(nextLong(1L, 1_000_000L)))
                        .withRate(nextDouble(0.01D, 10.00D))
                        .withListOfMovies(createTestMovies(nextInt(1, 16)))
                        .build())
                .collect(Collectors.toList());
    }

    private static List<Movie> createTestMovies(int number) {
        return IntStream.rangeClosed(1, number)
                .mapToObj(index -> new Movie.MovieBuilder(randomAlphabetic(15), LocalDate.ofEpochDay(nextLong(1L, 1_000_000L)))
                        .withRate(nextDouble(0.01D, 10.00D))
                        .withBoxOfficeRevenue(nextLong(1L, 1_000_000_000L))
                        .withDescription(randomAlphabetic(1000))
                        .build())
                .collect(Collectors.toList());
    }
}
