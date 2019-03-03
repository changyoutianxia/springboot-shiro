package com.ch.springbootshiro.po;

import java.io.Serializable;

public class Permission implements Serializable {
    private static final long serialVersionUID = 1527448973538928240L;

    private Integer id;
    private String permission;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "id=" + id +
                ", permission='" + permission + '\'' +
                '}';
    }
}
