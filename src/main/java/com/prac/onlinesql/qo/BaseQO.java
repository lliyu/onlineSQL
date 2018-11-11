package com.prac.onlinesql.qo;

/**
 * @author ly
 * @create 2018-11-10 23:30
 **/
public class BaseQO {

    private int page;
    private int limit;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
