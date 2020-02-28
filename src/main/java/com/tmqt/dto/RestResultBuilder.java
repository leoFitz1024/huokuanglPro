package com.tmqt.dto;



public class RestResultBuilder<T> {

    private T data;

    private String msg = "";

    private int code = 0;

    private int count = 0;


    public RestResultBuilder<T> setData(T data){
        this.data = data;
        return this;
    }

    public RestResultBuilder<T> setMsg(String msg){
        this.msg = msg;
        return this;
    }

    public RestResultBuilder<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public RestResultBuilder<T> setCount(int count) {
        this.count = count;
        return this;
    }

    public RestResult<T> build(){
        return new RestResult<T>(data,msg,code,count);
    }

}
