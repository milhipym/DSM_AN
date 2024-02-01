package com.dongbu.dsm.common;

import com.dongbu.dsm.Config;
import com.dongbu.dsm.model.Result;
import com.dongbu.dsm.model.ResultData;
import com.dongbu.dsm.model.ResultDataWithContents;
import com.dongbu.dsm.model.ResultDataWithT;
import com.dongbu.dsm.model.web.BCResultWebItem;
import com.dongbu.dsm.model.web.Contract;
import com.dongbu.dsm.model.web.Info;
import com.dongbu.dsm.model.web.KV;
import com.dongbu.dsm.model.web.OCRItem;
import com.dongbu.dsm.model.web.Parameter;
import com.dongbu.dsm.util.GSONUtils;
import com.dongbu.dsm.util.Log;
import com.minerva.magicbcreaderlib.ui.item.BCResultItem;

import java.util.ArrayList;

import minerva.magicidreaderlib.card.CardDTO;

/**
 * Created by LandonJung on 2017-09-06.
 */

public class DataManager {

    /**
     * 디바이스 및 앱 정보 결과 (JSON) 생성
     * @param resultCode    처리 결과 코드
     * @param message   처리 결과 메세지
     * @param data  디바이스 및 앱 정보 데이터
     * @return  디바이스 및 앱 정보 JSON 데이터
     */
    public static String genJSONResultGetInfo(int resultCode, String message, Info data) {
        ResultDataWithT<Info> result = new ResultDataWithT<>();
        result.setCode(String.valueOf(resultCode));
        result.setMessage(message.isEmpty() ? (resultCode == CommonData.API_SUCCESS_WEB_BRIDGE ? "Success" : "Fail") : message);
        result.setData(data);
        String json = GSONUtils.modelToJson(result);
        String jsonLog = GSONUtils.modelToJsonForLog(result);
        Log.d("------------------ 디바이스 및 앱 정보 결과 (JSON)------------------------");
        Log.d(jsonLog);
        Log.d("------------------------------------------------------------------");

        return json;
    }

    /**
     * 신분증 인식 결과 (JSON) 생성
     * @param resultCode 처리 결과 코드
     * @param message   처리 결과 메세지
     * @param data  신분증 인식 결과 데이터
     * @return 신분증 인식 결과 JSON 데이터
     */
    public static String genJSONResultDoOcrCall(int resultCode, String message, OCRItem data) {
        ResultDataWithT<OCRItem> result = new ResultDataWithT<>();
        result.setCode(String.valueOf(resultCode));
        result.setMessage(message.isEmpty() ? (resultCode == CommonData.API_SUCCESS_WEB_BRIDGE ? "Success" : "Fail") : message);
        result.setData(data);
        String json = GSONUtils.modelToJson(result);
        String jsonLog = GSONUtils.modelToJsonForLog(result);
        Log.d("------------------ 신분증 인식 결과 (JSON)------------------------");
//        {
//            "code": "0",
//            "data": {
//                      "imagePath": "/storage/emulated/0/Download/MinervaSoft/Perspective.jpg",
//                      "licenseNum": "11-01-650738-12",
//                      "name": "홍길동",
//                      "regNum": "123456-1738495",
//                      "title": "운전면허증"
//            },
//            "message": "Success"
//        }
//
//        {
//            "code": "0",
//            "data": {
//                    "imagePath": "/storage/emulated/0/Download/MinervaSoft/20170907_1353/Perspective.jpg",
//                    "name": "홍길동",
//                    "regNum": "123456-1738495",
//                    "title": "주민등록증"
//            },
//            "message": "Success"
//        }
        Log.d(jsonLog);
        Log.d("------------------------------------------------------------------");

        return json;
    }

    /**
     * 명함 인식 결과 (JSON) 생성
     * @param resultCode    처리 결과 코드
     * @param message   처리 결과 메세지
     * @param data  명함 인식 결과 데이터
     * @return  명함 인식 결과 JSON 데이터
     */
    public static String genJSONResultDoIdCall(int resultCode, String message, BCResultWebItem data) {
        ResultDataWithT<BCResultWebItem> result = new ResultDataWithT<>();
        result.setCode(String.valueOf(resultCode));
        result.setMessage(message.isEmpty() ? (resultCode == CommonData.API_SUCCESS_WEB_BRIDGE ? "Success" : "Fail") : message);
        result.setData(data);
        String json = GSONUtils.modelToJson(result);
        String jsonLog = GSONUtils.modelToJsonForLog(result);
        Log.d("------------------ 명함 인식 결과 (JSON)------------------------");
//        "code": "0",
//                "data": {
//                    "bcAddress": "06194 서울시 김남구 테쎄란로 432동부금융센터",
//                    "bcComRegCode": "",
//                    "bcComTel": "0230114853",
//                    "bcComTel_1": "02",
//                    "bcComTel_2": "3011",
//                    "bcComTel_3": "4853",
//                    "bcCompany": "동부화재해상보험 (주)",
//                    "bcDepartment": "",
//                    "bcEmail": "ux@dbinsnet",
//                    "bcEmail_1": "ux",
//                    "bcEmail_2": "dbinsnet",
//                    "bcFax": "05051816424",
//                    "bcJobTitle": "Qa Aga\\IW",
//                    "bcMobile": "01090714365",
//                    "bcMobile_1": "010",
//                    "bcMobile_2": "9071",
//                    "bcMobile_3": "4365",
//                    "bcName": "허성국",
//                    "bcPostCode": "",
//                    "bcSNS": "",
//                    "bcWebpage": "www.idongbu.com"
//        },
//        "message": "Success"
//    }
        Log.d(jsonLog);
        Log.d("------------------------------------------------------------------");

        return json;
    }

    /**
     * 문서 인식 결과 (JSON) 생성
     * @param resultCode    처리 결과 코드
     * @param message   처리 결과 메세지
     * @param data  문서 인식 결과 데이터 ( 문서 촬영 파일 목록 )
     * @return  문서 인식 결과 JSON 데이터
     */
    public static String genJSONResultDoPaperCall(int resultCode, String message, String data) {

        String json, jsonLog;

        if(resultCode == CommonData.API_FAIL_WEB_BRIDGE) {
            ResultDataWithT<ArrayList<String>> result = new ResultDataWithT<>();
            result.setCode(String.valueOf(resultCode));
            result.setMessage(message.isEmpty() ? (resultCode == CommonData.API_SUCCESS_WEB_BRIDGE ? "Success" : "Fail") : message);
            result.setData(null);
            json = GSONUtils.modelToJson(result);
            jsonLog = GSONUtils.modelToJsonForLog(result);
        } else {
            json = data;
            jsonLog = data;
        }
        Log.d("------------------ 문서 인식 결과 (JSON)------------------------");
//        {
//            "code": "0",
//            "data": [
//                    "/storage/emulated/0/MagicIDR/20170907135519.jpg",
//                    "/storage/emulated/0/MagicIDR/20170907135524.jpg",
//                    "/storage/emulated/0/MagicIDR/20170907135526.jpg",
//                    "/storage/emulated/0/MagicIDR/20170907135528.jpg",
//                    "/storage/emulated/0/MagicIDR/20170907135530.jpg",
//                    "/storage/emulated/0/MagicIDR/20170907135532.jpg"
//            ],
//            "message": "Success"
//        }
        Log.d(jsonLog);
        Log.d("------------------------------------------------------------------");

        return json;
    }

    public static String genJSONResultDoCameraGalleryCall(int resultCode, String message, String data) {
        ResultDataWithT<String> result = new ResultDataWithT<>();
        result.setCode(String.valueOf(resultCode));
        result.setMessage(message.isEmpty() ? (resultCode == CommonData.API_SUCCESS_WEB_BRIDGE ? "Success" : "Fail") : message);
        result.setData(data);
        String json = GSONUtils.modelToJson(result);
        String jsonLog = GSONUtils.modelToJsonForLog(result);
        Log.d("------------------ 이미지 가져오기 결과 (JSON)------------------------");
        Log.d(jsonLog);
        Log.d("------------------------------------------------------------------");

        return json;
    }

    /**
     * 연락처 가져오기 결과 (JSON) 생성
     * @param resultCode    처리 결과 코드
     * @param message   처리 결과 메세지
     * @param data  문서 인식 결과 데이터 ( 문서 촬영 파일 목록 )
     * @return  문서 인식 결과 JSON 데이터
     */
    public static String genJSONResultGetContractCall(int resultCode, String message, Contract data) {
        ResultDataWithT<Contract> result = new ResultDataWithT<>();
        result.setCode(String.valueOf(resultCode));
        result.setMessage(message.isEmpty() ? (resultCode == CommonData.API_SUCCESS_WEB_BRIDGE ? "Success" : "Fail") : message);
        result.setData(data);
        String json = GSONUtils.modelToJson(result);
        String jsonLog = GSONUtils.modelToJsonForLog(result);
        Log.d("------------------ 연락처 가져오기 결과 (JSON)------------------------");
//        {
//            "code": "0",
//            "data": [
//                    "/storage/emulated/0/MagicIDR/20170907135519.jpg",
//                    "/storage/emulated/0/MagicIDR/20170907135524.jpg",
//                    "/storage/emulated/0/MagicIDR/20170907135526.jpg",
//                    "/storage/emulated/0/MagicIDR/20170907135528.jpg",
//                    "/storage/emulated/0/MagicIDR/20170907135530.jpg",
//                    "/storage/emulated/0/MagicIDR/20170907135532.jpg"
//            ],
//            "message": "Success"
//        }
        Log.d(jsonLog);
        Log.d("------------------------------------------------------------------");

        return json;
    }

    /**
     * 기타 웹 연동 처리 결과 (JSON) 생성
     * @param resultCode    처리 결과 코드
     * @param message   처리 결과 메세지
     * @param data  처리 결과 데이터
     * @return  처리 결과 JSON 데이터
     */
    public static String genJSONResultParameter(int resultCode, String message, Parameter data) {
        ResultDataWithT<Parameter> result = new ResultDataWithT<>();
        result.setCode(String.valueOf(resultCode));
        result.setMessage(message.isEmpty() ? (resultCode == CommonData.API_SUCCESS_WEB_BRIDGE ? "Success" : "Fail") : message);
        result.setData(data);
        String json = GSONUtils.modelToJson(result);
        String jsonLog = GSONUtils.modelToJsonForLog(result);
        Log.d("------------------ 처리 결과 (JSON)------------------------");
        Log.d(jsonLog);
        Log.d("------------------------------------------------------------------");

        return json;
    }

    /**
     * 기본 웹 연동 처리 결과 (JSON) 생성
     * @param resultCode    처리 결과 코드
     * @param message   처리 결과 메세지
     * @return  처리 결과 JSON 데이터
     */
    public static String genJSONResultDefault(int resultCode, String message) {
        ResultData result = new ResultData();
        result.setCode(String.valueOf(resultCode));
        result.setMessage(message.isEmpty() ? (resultCode == CommonData.API_SUCCESS_WEB_BRIDGE ? "Success" : "Fail") : message);
        String json = GSONUtils.modelToJson(result);
        String jsonLog = GSONUtils.modelToJsonForLog(result);
        Log.d("------------------ 처리 결과 (JSON)------------------------");
        Log.d(jsonLog);
        Log.d("------------------------------------------------------------------");

        return json;
    }

    /**
     * 설정값 가져오기 연동 처리 결과 (JSON) 생성
     * @param resultCode    처리 결과 코드
     * @param message   처리 결과 메세지
     * @return  처리 결과 JSON 데이터
     */
    public static String genJSONResultGetValueCall(int resultCode, String message, String key, String value) {
        ResultDataWithT<String> result = new ResultDataWithT();
        result.setCode(String.valueOf(resultCode));
        result.setMessage(message.isEmpty() ? (resultCode == CommonData.API_SUCCESS_WEB_BRIDGE ? "Success" : "Fail") : message);
        result.setData(value);
        String json = GSONUtils.modelToJson(result);
        String jsonLog = GSONUtils.modelToJsonForLog(result);
        Log.d("------------------ 이미지 가져오기 결과 (JSON)------------------------");
        Log.d(jsonLog);
        Log.d("------------------------------------------------------------------");

        return json;
    }

}
