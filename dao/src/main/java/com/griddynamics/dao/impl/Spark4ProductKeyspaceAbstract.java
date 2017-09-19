package com.griddynamics.dao.impl;

import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@Service
public abstract class Spark4ProductKeyspaceAbstract {

    protected static final String KEYSPACE = "product_test";
    protected static final String PRODUCT_TABLE = "product";
    protected static final String UPC_TABLE = "upc";
    protected static final int ACTIVE_STATUS_CODE = 1;
    protected HashMap<String, String> map = new HashMap<>();

    @Autowired
    protected JavaSparkContext sparkContext;

    @PostConstruct
    public void init() {
        map.put("statusCode", "statuscode");
    }
}
