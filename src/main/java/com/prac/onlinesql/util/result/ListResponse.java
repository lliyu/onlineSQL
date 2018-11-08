package com.prac.onlinesql.util.result;

import java.util.List;

/**
 * @Auther: Administrator
 * @Date: 2018-11-07 14:11
 * @Description:
 */
public class ListResponse extends ResponseData {

    private List data;

    public ListResponse(int code, String message) {
        super(code, message);
    }

    public ListResponse(int code, String message, List data) {
        super(code, message);
        this.data = data;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }
}
