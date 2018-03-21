package com.griddynamics.dao.config;

import com.datastax.driver.core.*;
import com.griddynamics.dao.codec.ActorCodec;
import com.griddynamics.dao.codec.MovieCodec;
import com.griddynamics.dao.model.Actor;
import com.griddynamics.dao.model.Movie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApacheCassandraConfiguration {

    @Value("${cassandra.host}")
    private String cassandraHost;
    @Value("${cassandra.keyspace}")
    private String cassandraKeyspace;

    @Bean
    public Cluster getCluster() {
        Cluster cluster = Cluster.builder().addContactPoint(cassandraHost).build();
        KeyspaceMetadata keyspace = cluster.getMetadata().getKeyspace(cassandraKeyspace);
        UserType actor = keyspace.getUserType("actor");
        UserType movie = keyspace.getUserType("movie");
        CodecRegistry codecRegistry = cluster.getConfiguration().getCodecRegistry();
        TypeCodec<UDTValue> actorTypeCodec = codecRegistry.codecFor(actor);
        TypeCodec<UDTValue> movieTypeCodec = codecRegistry.codecFor(movie);
        ActorCodec actorCodec = new ActorCodec(actorTypeCodec, Actor.class);
        MovieCodec movieCodec = new MovieCodec(movieTypeCodec, Movie.class);
        codecRegistry.register(actorCodec, movieCodec);
        return cluster;
    }

    @Bean
    public Session getSession() {
        return getCluster().connect(cassandraKeyspace);
    }
}
