package com.dongbu.dsm.model.web;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 81700905 on 2017-11-24.
 */

public class KV {
    @SerializedName("key")
    private String key;                  // key

    @SerializedName("value")
    private String value;                // value

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
