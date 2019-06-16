package com.prac.onlinesql.net.mq.entity;

import java.io.Serializable;

/**
 * @Auther: Administrator
 * @Date: 2019-06-14 17:59
 * @Description:
 */
public class RootPageEntity implements Serializable{
    private String name;
    private String path;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
