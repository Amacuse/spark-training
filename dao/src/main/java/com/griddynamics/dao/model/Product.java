package com.griddynamics.dao.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

public class Product implements Serializable {

    private int pid;
    private String name;
    private List<Integer> uids;
    private int statusCode;

    public Product() {
    }

    public Product(int pid, String name, List<Integer> uids) {
        this(pid, name, uids, 0);
    }

    public Product(int pid, String name, List<Integer> uids, int statusCode) {
        this.pid = pid;
        this.name = name;
        this.uids = uids;
        this.statusCode = statusCode;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getUids() {
        return uids;
    }

    public void setUids(List<Integer> uids) {
        this.uids = uids;
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
                .append("pid", pid)
                .append("name", name)
                .append("uids", uids)
                .append("statusCode", statusCode)
                .toString();
    }
}
