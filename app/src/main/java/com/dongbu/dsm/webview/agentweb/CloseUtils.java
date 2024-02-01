package com.dongbu.dsm.webview.agentweb;

import com.dongbu.dsm.Config;

import java.io.Closeable;

/**
 * Created by cenxiaozhong on 2017/5/24.
 */

class CloseUtils {


    public static void closeIO(Closeable closeable){
        try {

            if(closeable!=null)
                closeable.close();
        }catch (Exception e){

            if(Config.DISPLAY_LOG) e.printStackTrace();
        }

    }
}
