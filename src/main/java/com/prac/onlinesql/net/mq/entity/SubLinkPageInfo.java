package com.prac.onlinesql.net.mq.entity;

import java.io.Serializable;

/**
 * @Auther: Administrator
 * @Date: 2019-06-13 17:34
 * @Description:
 */
public class SubLinkPageInfo implements Serializable {

    private String name;
    private String rootName;
    private String rootPath;
    private String uri;
    private int totalPage;

    public String getRootName() {
        return rootName;
    }

    public void setRootName(String rootName) {
        this.rootName = rootName;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
                ", uri='" + uri + '\'' +
                ", totalPage=" + totalPage +
                '}';
    }
}
