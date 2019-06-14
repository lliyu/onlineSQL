package com.prac.onlinesql.net.youguo.entity;

import java.io.Serializable;

/**
 * @Auther: Administrator
 * @Date: 2019-06-13 17:34
 * @Description:
 */
public class PageInfo implements Serializable {

    private String name;
    private int size;
    private String uri;
    private int totalPage;

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

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    @Override
    public String toString() {
        return "PageInfo{" +
                "name='" + name + '\'' +
                ", size=" + size +
                ", uri='" + uri + '\'' +
                ", totalPage=" + totalPage +
                '}';
    }
}
