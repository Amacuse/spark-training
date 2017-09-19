package com.griddynamics.dao.config;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
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
    public Cluster getCluster(){
        return Cluster.builder().addContactPoint(cassandraHost).build();
    }

    @Bean
    public Session getSession(){
        return getCluster().connect(cassandraKeyspace);
    }
}
