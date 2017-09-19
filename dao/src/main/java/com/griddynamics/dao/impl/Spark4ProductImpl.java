package com.griddynamics.dao.impl;

import com.griddynamics.dao.Spark4Product;
import com.griddynamics.dao.dto.ProductDto;
import com.griddynamics.dao.dto.UpcDto;
import com.griddynamics.dao.model.Product;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.types.DataTypes;
import org.springframework.stereotype.Service;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;

import static com.datastax.spark.connector.japi.CassandraJavaUtil.*;

@Service
public class Spark4ProductImpl extends Spark4ProductKeyspaceAbstract implements Spark4Product {

    @Override
    public void addAll(List<Product> productList) {
        JavaRDD<Product> javaRDD = sparkContext.parallelize(productList);
        javaFunctions(javaRDD)
                .writerBuilder(KEYSPACE, PRODUCT_TABLE, mapToRow(Product.class, map))
                .saveToCassandra();
    }

    @Override
    public List<Integer> getPidOfActiveProducts() {
        return javaFunctions(sparkContext)
                .cassandraTable(KEYSPACE, PRODUCT_TABLE, mapRowToTuple(Integer.class, Integer.class))
                .select(
                        column("pid"),
                        column("statuscode"))
                .filter(tuple2 -> tuple2._2 == ACTIVE_STATUS_CODE)
                .map(tuple2 -> tuple2._1)
                .collect();
    }

    @Override
    public List<Integer> getUidOfActiveUpcOfActiveProducts() {
        JavaRDD<UpcDto> upcsOfActiveProducts = javaFunctions(sparkContext)
                .cassandraTable(KEYSPACE, PRODUCT_TABLE, mapRowTo(ProductDto.class))
                .select(column("statuscode").as("statusCode"),
                        column("uids"))
                .filter(productDto -> productDto.getStatusCode() == ACTIVE_STATUS_CODE)
                .flatMap(productDto -> {

                    List<UpcDto> result = new ArrayList<>();

                    for (Integer uid : productDto.getUids()) {
                        UpcDto upcDto = new UpcDto();
                        upcDto.setUid(uid);
                        result.add(upcDto);
                    }

                    return result.iterator();
                });

        JavaRDD<UpcDto> repartitionedRdd = javaFunctions(upcsOfActiveProducts)
                .repartitionByCassandraReplica(
                        KEYSPACE,
                        UPC_TABLE,
                        10,
                        someColumns("uid"),
                        mapToRow(UpcDto.class));

        return javaFunctions(repartitionedRdd)
                .joinWithCassandraTable(
                        KEYSPACE,
                        UPC_TABLE,
                        someColumns("uid", "statuscode"),
                        someColumns("uid"),
                        mapRowTo(UpcDto.class, Pair.of("statusCode", "statuscode")),
                        mapToRow(UpcDto.class))
                .filter(upcDto -> upcDto._2.getStatusCode() == ACTIVE_STATUS_CODE)
                .map(upcDto -> upcDto._1.getUid())
                .collect();
    }
}
