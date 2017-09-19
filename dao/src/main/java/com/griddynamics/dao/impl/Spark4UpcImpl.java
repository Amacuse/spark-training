package com.griddynamics.dao.impl;

import com.griddynamics.dao.Spark4Upc;
import com.griddynamics.dao.model.Upc;
import org.apache.spark.api.java.JavaRDD;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.datastax.spark.connector.japi.CassandraJavaUtil.javaFunctions;
import static com.datastax.spark.connector.japi.CassandraJavaUtil.mapRowTo;
import static com.datastax.spark.connector.japi.CassandraJavaUtil.mapToRow;

@Service
public class Spark4UpcImpl extends Spark4ProductKeyspaceAbstract implements Spark4Upc {

    @Override
    public void addAll(List<Upc> upcList) {
        JavaRDD<Upc> javaRDD = sparkContext.parallelize(upcList);
        javaFunctions(javaRDD)
                .writerBuilder(KEYSPACE, UPC_TABLE, mapToRow(Upc.class, map))
                .saveToCassandra();
    }

    @Override
    public List<Upc> getAll() {
        return javaFunctions(sparkContext)
                .cassandraTable(KEYSPACE, UPC_TABLE, mapRowTo(Upc.class, map))
                .collect();
    }
}
