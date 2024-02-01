package com.dongbu.dsm.webview.agentweb;

import androidx.collection.ArrayMap;

/**
 * Created by cenxiaozhong on 2017/5/13.
 */

public interface JsInterfaceHolder {

    JsInterfaceHolder addJavaObjects(ArrayMap<String, Object> maps);

    JsInterfaceHolder addJavaObject(String k, Object v);

    boolean checkObject(Object v) ;

}
