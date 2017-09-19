package com.griddynamics.dao.dto;

import java.io.Serializable;

public class UpcDto implements Serializable {

    private int uid;
    private int statusCode;

    public UpcDto() {
    }

    public UpcDto(int uid, int statusCode) {
        this.uid = uid;
        this.statusCode = statusCode;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
