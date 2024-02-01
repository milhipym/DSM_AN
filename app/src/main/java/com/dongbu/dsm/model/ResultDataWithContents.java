package com.dongbu.dsm.model;

import com.google.gson.annotations.SerializedName;

public class ResultDataWithContents {

    @SerializedName("code")
    private String code;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private Result data;

    public Result getData() {
        return data;
    }

    public void setData(Result data) {
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
