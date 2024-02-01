package com.dongbu.dsm.model.web;

/**
 * Created by 81700905 on 2017-09-27.
 */
import com.blankj.utilcode.util.StringUtils;
import com.dongbu.dsm.Config;
import com.dongbu.dsm.util.DSMUtil;
import com.minerva.magicbcreaderlib.ui.item.BCResultItem;
import com.google.gson.annotations.SerializedName;

public class BCResultWebItem {
    @SerializedName("bcName")
    private String bcName;                  // 이름
    @SerializedName("bcCompany")
    private String bcCompany;               // 회사명
    @SerializedName("bcDepartment")
    private String bcDepartment;            // 부서명
    @SerializedName("bcJobTitle")
    private String bcJobTitle;              // 직책
    @SerializedName("bcEmail")
    private String bcEmail;                 // 이메일
    @SerializedName("bcEmail_1")
    private String bcEmail_1;               // 이메일 앞자리
    @SerializedName("bcEmail_2")
    private String bcEmail_2;               // 이메일 뒷자리
    @SerializedName("bcMobile")
    private String bcMobile;                // 모바일 번호
    @SerializedName("bcMobile_1")
    private String bcMobile_1;              // 모바일 번호 앞자리
    @SerializedName("bcMobile_2")
    private String bcMobile_2;              // 모바일 번호 중간 자리
    @SerializedName("bcMobile_3")
    private String bcMobile_3;              // 모바일 번호 끝자리
    @SerializedName("bcComTel")
    private String bcComTel;                // 회사 전화번호
    @SerializedName("bcComTel_1")
    private String bcComTel_1;              // 회사 전화번호 앞자리
    @SerializedName("bcComTel_2")
    private String bcComTel_2;              // 회사 전화번호 중간 자리
    @SerializedName("bcComTel_3")
    private String bcComTel_3;              // 회사 전화번호 끝자리
    @SerializedName("bcWebpage")
    private String bcWebpage;               // 회사 홈페이지 주소
    @SerializedName("bcAddress")
    private String bcAddress;               // 회사 주소
    @SerializedName("bcFax")
    private String bcFax;                   // 팩스번호
    @SerializedName("bcFax_1")
    private String bcFax_1;                 // 팩스번호 앞자리
    @SerializedName("bcFax_2")
    private String bcFax_2;                 // 팩스번호 중간 자리
    @SerializedName("bcFax_3")
    private String bcFax_3;                 // 팩스번호 끝자리
    @SerializedName("bcSNS")
    private String bcSNS;                   // SNS 정보
    @SerializedName("bcPostCode")
    private String bcPostCode;              // 우편번호
    @SerializedName("bcComRegCode")
    private String bcComRegCode;            // 회사 코드

    public BCResultWebItem() {
    }

    public void InitResultItem() {
        this.bcName = "";
        this.bcCompany = "";
        this.bcDepartment = "";
        this.bcEmail = "";
        this.bcMobile = "";
        this.bcComTel = "";
        this.bcWebpage = "";
        this.bcJobTitle = "";
        this.bcAddress = "";
        this.bcFax = "";
        this.bcSNS = "";
        this.bcPostCode = "";
        this.bcComRegCode = "";
    }

    public void set(BCResultItem bcResultItem) {
        if(bcResultItem == null) return;

        this.bcName = bcResultItem.getBcName();
        this.bcCompany = bcResultItem.getBcCompany();
        this.bcDepartment = bcResultItem.getBcDepartment();
        this.bcEmail = bcResultItem.getBcEmail();
        if(!StringUtils.isEmpty(this.bcEmail)) {
            try{
                this.bcEmail_1 = DSMUtil.getSplitEmail(this.bcEmail)[0];
                this.bcEmail_2 = DSMUtil.getSplitEmail(this.bcEmail)[1];
            }catch(Exception e) {
                if(Config.DISPLAY_LOG) e.printStackTrace();
            }
        }

        this.bcMobile = bcResultItem.getBcMobile();
        if(!StringUtils.isEmpty(this.bcMobile)) {
            try{
                this.bcMobile_1 = DSMUtil.getSplitPhoneNum(this.bcMobile)[0];
                this.bcMobile_2 = DSMUtil.getSplitPhoneNum(this.bcMobile)[1];
                this.bcMobile_3 = DSMUtil.getSplitPhoneNum(this.bcMobile)[2];
            }catch(Exception e) {
                if(Config.DISPLAY_LOG) e.printStackTrace();
            }
        }
        this.bcComTel = bcResultItem.getBcComTel();
        if(!StringUtils.isEmpty(this.bcComTel)) {
            try{
                this.bcComTel_1 = DSMUtil.getSplitPhoneNum(this.bcComTel)[0];
                this.bcComTel_2 = DSMUtil.getSplitPhoneNum(this.bcComTel)[1];
                this.bcComTel_3 = DSMUtil.getSplitPhoneNum(this.bcComTel)[2];
            }catch(Exception e) {
                if(Config.DISPLAY_LOG) e.printStackTrace();
            }
        }

        this.bcWebpage = bcResultItem.getBcWebpage();
        this.bcJobTitle = bcResultItem.getBcJobTitle();
        this.bcAddress = bcResultItem.getBcAddress();
        this.bcFax = bcResultItem.getBcFax();
        if(!StringUtils.isEmpty(this.bcFax)) {
            try{
                this.bcFax_1 = DSMUtil.getSplitPhoneNum(this.bcFax)[0];
                this.bcFax_2 = DSMUtil.getSplitPhoneNum(this.bcFax)[1];
                this.bcFax_3 = DSMUtil.getSplitPhoneNum(this.bcFax)[2];
            }catch(Exception e) {
                if(Config.DISPLAY_LOG) e.printStackTrace();
            }
        }
        this.bcSNS = bcResultItem.getBcSNS();
        this.bcPostCode = bcResultItem.getBcPostCode();
        this.bcComRegCode = bcResultItem.getBcComRegCode();

    }

    public String getBcEmail_1() {
        return bcEmail_1;
    }

    public void setBcEmail_1(String bcEmail_1) {
        this.bcEmail_1 = bcEmail_1;
    }

    public String getBcEmail_2() {
        return bcEmail_2;
    }

    public void setBcEmail_2(String bcEmail_2) {
        this.bcEmail_2 = bcEmail_2;
    }

    public String getBcMobile_1() {
        return bcMobile_1;
    }

    public void setBcMobile_1(String bcMobile_1) {
        this.bcMobile_1 = bcMobile_1;
    }

    public String getBcMobile_2() {
        return bcMobile_2;
    }

    public void setBcMobile_2(String bcMobile_2) {
        this.bcMobile_2 = bcMobile_2;
    }

    public String getBcMobile_3() {
        return bcMobile_3;
    }

    public void setBcMobile_3(String bcMobile_3) {
        this.bcMobile_3 = bcMobile_3;
    }

    public String getBcComTel_1() {
        return bcComTel_1;
    }

    public void setBcComTel_1(String bcComTel_1) {
        this.bcComTel_1 = bcComTel_1;
    }

    public String getBcComTel_2() {
        return bcComTel_2;
    }

    public void setBcComTel_2(String bcComTel_2) {
        this.bcComTel_2 = bcComTel_2;
    }

    public String getBcComTel_3() {
        return bcComTel_3;
    }

    public void setBcComTel_3(String bcComTel_3) {
        this.bcComTel_3 = bcComTel_3;
    }

    public void setBcName(String var1) {
        this.bcName = var1;
    }

    public String getBcName() {
        return this.bcName;
    }

    public void setBcCompany(String var1) {
        this.bcCompany = var1;
    }

    public String getBcCompany() {
        return this.bcCompany;
    }

    public void setBcDepartment(String var1) {
        this.bcDepartment = var1;
    }

    public String getBcDepartment() {
        return this.bcDepartment;
    }

    public void setBcEmail(String var1) {
        this.bcEmail = var1;
    }

    public String getBcEmail() {
        return this.bcEmail;
    }

    public void setBcMobile(String var1) {
        this.bcMobile = var1;
    }

    public String getBcMobile() {
        return this.bcMobile;
    }

    public void setBcComTel(String var1) {
        this.bcComTel = var1;
    }

    public String getBcComTel() {
        return this.bcComTel;
    }

    public void setBcWebpage(String var1) {
        this.bcWebpage = var1;
    }

    public String getBcWebpage() {
        return this.bcWebpage;
    }

    public void setBcJobTitle(String var1) {
        this.bcJobTitle = var1;
    }

    public String getBcJobTitle() {
        return this.bcJobTitle;
    }

    public void setBcAddress(String var1) {
        this.bcAddress = var1;
    }

    public String getBcAddress() {
        return this.bcAddress;
    }

    public void setBcPostCode(String var1) {
        this.bcPostCode = var1;
    }

    public String getBcPostCode() {
        return this.bcPostCode;
    }

    public void setBcFax(String var1) {
        this.bcFax = var1;
    }

    public String getBcFax() {
        return this.bcFax;
    }

    public String getBcFax_1() {
        return bcFax_1;
    }

    public void setBcFax_1(String bcFax_1) {
        this.bcFax_1 = bcFax_1;
    }

    public String getBcFax_2() {
        return bcFax_2;
    }

    public void setBcFax_2(String bcFax_2) {
        this.bcFax_2 = bcFax_2;
    }

    public String getBcFax_3() {
        return bcFax_3;
    }

    public void setBcFax_3(String bcFax_3) {
        this.bcFax_3 = bcFax_3;
    }

    public void setBcComRegCode(String var1) {
        this.bcComRegCode = var1;
    }

    public String getBcComRegCode() {
        return this.bcComRegCode;
    }

    public void setBcSNS(String var1) {
        this.bcSNS = var1;
    }

    public String getBcSNS() {
        return this.bcSNS;
    }
}