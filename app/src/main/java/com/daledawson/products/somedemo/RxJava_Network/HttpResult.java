package com.daledawson.products.somedemo.RxJava_Network;

/**
 * Created by Administrator on 2017/12/5/005.
 */

public class HttpResult<T> {
    private int code;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
