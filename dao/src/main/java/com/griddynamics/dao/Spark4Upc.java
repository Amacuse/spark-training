package com.griddynamics.dao;

import com.griddynamics.dao.model.Upc;

import java.util.List;

public interface Spark4Upc {

    void addAll(List<Upc> upcList);

    List<Upc> getAll();
}
