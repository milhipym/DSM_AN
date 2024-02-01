package com.dongbu.dsm.model.web;

import com.google.gson.annotations.SerializedName;

/**
 * 디바이스 및 앱 정보 모델
 * Created by LandonJung on 2017-09-07.
 */

public class Info {

    @SerializedName("deviceId")
    private String deviceId;                    // 단말기 ID

    @SerializedName("phoneNum")
    private String phoneNum;                    // 단말기 휴대폰 번호

    @SerializedName("macAddr")
    private String macAddr;                     // MAC 주소

    @SerializedName("ip")
    private String ip;                          // IP

    @SerializedName("osName")
    private String osName;                      // OS 이름

    @SerializedName("osVer")
    private String osVer;                       // OS 버전

    @SerializedName("osVerName")
    private String osVerName;                   // OS 버전 이름

    @SerializedName("phoneModel")
    private String phoneModel;                  // 휴대폰 모델명

    @SerializedName("appVer")
    private String appVer;                      // 앱 버전

    @SerializedName("isPad")
    private boolean isPad;                      // 태블릿인지 여부

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getOsVerName() {
        return osVerName;
    }

    public void setOsVerName(String osVerName) {
        this.osVerName = osVerName;
    }

    public String getOsVer() {
        return osVer;
    }

    public void setOsVer(String osVer) {
        this.osVer = osVer;
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }

    public String getAppVer() {
        return appVer;
    }

    public void setAppVer(String appVer) {
        this.appVer = appVer;
    }

    public boolean isPad() {
        return isPad;
    }

    public void setPad(boolean pad) {
        isPad = pad;
    }
}
