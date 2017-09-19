package com.griddynamics.dao.config;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SparkConfiguration {

    @Value("${cassandra.host}")
    private String cassandraHost;
    @Value("${spark.master.host}")
    private String sparkMasterHost;
    @Value("${app.name}")
    private String appName;

    @Bean
    public SparkConf getSparkConf() {
        return new SparkConf()
                .setMaster(sparkMasterHost)
                .setAppName(appName)
                .set("spark.cassandra.connection.host", cassandraHost);
    }

    @Bean
    public JavaSparkContext getJavaSparkContext() {
        return new JavaSparkContext(getSparkConf());
    }
}
