package com.dongbu.dsm.model.api;

import com.dongbu.dsm.util.Log;
import com.google.gson.annotations.SerializedName;
/**
 * MDM AppInfo 클래스
 * Created by 81700905 on 2017-10-30.
 */

public class AppInfos {
    @SerializedName("appNm")
    String appNm;                           // 앱 이름
    @SerializedName("appVer")
    String appVer;                          // 앱 버전
    @SerializedName("appPackage")
    String appPackage;                      // 앱 패키지 이름
    @SerializedName("osType")
    String osType;                          // OS 타입
    @SerializedName("deviceType")
    String deviceType;                      // 단말기 타입
    @SerializedName("appInstallUrl")
    String appInstallUrl;                   // 앱 설치 URL
    @SerializedName("iconFileUrl")
    String iconFileUrl;                     // 앱 아이콘 URL
    boolean isEmpty = false;                // 앱 정보 유무 여부
    boolean isInstalled = false;            // 설치 여부
    boolean isNeedUpdate = false;           // 업데이트 여부

    public AppInfos(String appNm, String appVer, String appPackage, String osType, String deviceType, String appInstallUrl, String iconFileUrl) {
        this.appNm = appNm;
        this.appVer = appVer;
        this.appPackage = appPackage;
        this.osType = osType;
        this.deviceType = deviceType;
        this.appInstallUrl = appInstallUrl;
        this.iconFileUrl = iconFileUrl;
    }

    public AppInfos(AppInfos item) {
        this.appNm = item.getAppNm();
        this.appVer = item.getAppVer();
        this.appPackage = item.getAppPackage();
        this.osType = item.getOsType();
        this.deviceType = item.getDeviceType();
        this.appInstallUrl = item.getAppInstallUrl();
        this.iconFileUrl = item.getIconFileUrl();
    }

    public AppInfos(boolean isEmpty) {
        this.appNm = "";
        this.appVer = "";
        this.appPackage = "";
        this.osType = "";
        this.deviceType = "";
        this.appInstallUrl = "";
        this.iconFileUrl = "";
        this.isEmpty = isEmpty;
    }

    public String getAppNm() {
        return appNm;
    }

    public void setAppNm(String appNm) {
        this.appNm = appNm;
    }

    public String getAppVer() {
        return appVer;
    }

    public void setAppVer(String appVer) {
        this.appVer = appVer;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getAppInstallUrl() {
        return appInstallUrl;
    }

    public void setAppInstallUrl(String appInstallUrl) {
        this.appInstallUrl = appInstallUrl;
    }

    public String getIconFileUrl() {
        return iconFileUrl;
    }

    public void setIconFileUrl(String iconFileUrl) {
        this.iconFileUrl = iconFileUrl;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    public boolean isInstalled() {
        return isInstalled;
    }

    public void setInstalled(boolean installed) {
        isInstalled = installed;
    }

    public boolean isNeedUpdate() {
        return isNeedUpdate;
    }

    public void setNeedUpdate(boolean needUpdate) {
        isNeedUpdate = needUpdate;
    }

    public String toString(int index) {
        String log = "[" + index + "] appNm=" + appNm + " appVer=" + appVer + " appPackage=" + appPackage + " osType=" + osType + " deviceType=" + deviceType + " appInstallUrl=" + appInstallUrl + " iconFileUrl" + " isEmpty=" + (isEmpty?"true":"false");
        Log.d(log);
        return log;
    }
}
