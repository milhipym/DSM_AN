package com.dongbu.dsm.model.web;

import com.dongbu.dsm.util.Log;
import com.google.gson.annotations.SerializedName;

/**
 * 신분증 촬영 인식 결과 모델
 * Created by LandonJung on 2017-09-07.
 */

public class OCRItem {

    @SerializedName("title")
    private String title;                       // 주민등록증 / 운전면허증

    @SerializedName("name")
    private String name;                        // 이름

    @SerializedName("regNum")
    private String regNum;                      // 주민등록번호

    @SerializedName("licenseNum")
    private String licenseNum;                  // 운전면허증번호

    @SerializedName("issueDate")
    private String issueDate;                   // 발급일자

    @SerializedName("imagePath")
    private String imagePath;                   // 사진

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegNum() {
        return regNum;
    }

    public void setRegNum(String regNum) {
        this.regNum = regNum;
    }

    public String getLicenseNum() {
        return licenseNum;
    }

    public void setLicenseNum(String licenseNum) {
        this.licenseNum = licenseNum;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

}
