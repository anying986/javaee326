package com.itheima.web;

public class Result {
    public static final int SUCCESS=1;
    public static final int FAILURE=0;
    public static final int NOLOGIN=2;

    private int code;
    private Object data;
    private String message;

    public Result() {
    }

    public Result(int code, Object data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
