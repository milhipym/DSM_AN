package com.dongbu.dsm.model.web;

import com.google.gson.annotations.SerializedName;

/**
 * 기타 웹 연동 파라메터 모델
 * Created by LandonJung on 2017-09-06.
 */

public class Contract {
    @SerializedName("name")
    private String name;            // 이름

    @SerializedName("tel")
    private String tel;             // 전화번호

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
