package com.griddynamics.dao;

import com.griddynamics.dao.model.Product;
import com.griddynamics.dao.model.Upc;

import java.util.List;

public interface Spark4Product {

    void addAll(List<Product> productList);

    List<Integer> getPidOfActiveProducts();

    List<Integer> getUidOfActiveUpcOfActiveProducts();
}
