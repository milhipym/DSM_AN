package com.dongbu.dsm.model;

import com.google.gson.annotations.SerializedName;

public class ResultDataWithT<T> {

    @SerializedName("code")
    private String code;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
