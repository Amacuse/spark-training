package com.griddynamics.dao.dto;

import java.io.Serializable;
import java.util.List;

public class ProductDto implements Serializable {

    private int statusCode;
    private List<Integer> uids;

    public ProductDto() {
    }

    public ProductDto(int statusCode, List<Integer> uids) {
        this.statusCode = statusCode;
        this.uids = uids;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public List<Integer> getUids() {
        return uids;
    }

    public void setUids(List<Integer> uids) {
        this.uids = uids;
    }
}
