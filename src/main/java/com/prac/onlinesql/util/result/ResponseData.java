package com.prac.onlinesql.util.result;

/**
 * @Auther: Administrator
 * @Date: 2018-11-07 14:09
 * @Description: 响应前端
 */
public class ResponseData {

    private int code;

    private String Message;

    public ResponseData(int code, String message) {
        this.code = code;
        Message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
