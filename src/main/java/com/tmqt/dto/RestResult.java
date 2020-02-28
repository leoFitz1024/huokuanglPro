package com.tmqt.dto;


public class RestResult<T> {

    private T data;

    private String msg;

    private int code;

    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    private RestResult(){}

    protected RestResult( T data, String msg,int code,int count) {
        this.data = data;
        this.msg = msg;
        this.code = code;
        this.count = count;
    }

    public T getData() {
        return data;
    }


    public RestResult<T> setData(T data) {
        this.data = data;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
