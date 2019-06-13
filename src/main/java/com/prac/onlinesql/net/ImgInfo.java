package com.prac.onlinesql.net;

import java.io.Serializable;

/**
 * @Auther: Administrator
 * @Date: 2019-06-13 17:43
 * @Description:
 */
public class ImgInfo implements Serializable {

    private String name;
    private int size;
    private String uri;
    private String directName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getDirectName() {
        return directName;
    }

    public void setDirectName(String directName) {
        this.directName = directName;
    }
}
