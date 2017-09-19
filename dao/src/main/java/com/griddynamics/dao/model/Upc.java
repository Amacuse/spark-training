package com.griddynamics.dao.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class Upc implements Serializable {

    private int uid;
    private String name;
    private int price;
    private int statusCode;

    public Upc() {
    }

    public Upc(int uid, String name, int price) {
        this(uid, name, price, 0);
    }

    public Upc(int uid, String name, int price, int statusCode) {
        this.uid = uid;
        this.name = name;
        this.price = price;
        this.statusCode = statusCode;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("uid", uid)
                .append("name", name)
                .append("price", price)
                .append("statusCode", statusCode)
                .toString();
    }
}
