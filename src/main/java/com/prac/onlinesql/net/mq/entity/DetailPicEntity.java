package com.prac.onlinesql.net.mq.entity;

import java.io.Serializable;

/**
 * @author ly
 * @create 2019-06-14 21:55
 **/
public class DetailPicEntity implements Serializable{
    private String subPath;
    private String rootPath;
    private String uri;
    private String rootName;

    public String getRootName() {
        return rootName;
    }

    public void setRootName(String rootName) {
        this.rootName = rootName;
    }

    public String getSubPath() {
        return subPath;
    }

    public void setSubPath(String subPath) {
        this.subPath = subPath;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
