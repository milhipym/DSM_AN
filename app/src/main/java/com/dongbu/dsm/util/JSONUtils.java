package com.dongbu.dsm.util;

import android.content.Context;

import com.dongbu.dsm.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by 81700905 on 2017-09-06.
 */

public class JSONUtils {

    public static void testJsonUtils(Context context) {

        Log.d("--------------------------- JSON TEST ---------------------------------------");
        String jsonStr = getJsonFromAsset(context, "newdata");
        JSONUtil jsonUtil = new JSONUtil(jsonStr);
//        List<People> peoples3 =jsonUtil.getList(People.class,"people",Look.class,En.class);
//        Log.e(peoples3.toString());
//        List<People> peoples =jsonUtil.getList(People.class,"people",Look.class);
//        Log.e(peoples.toString());
//        List<People> peoples2 =jsonUtil.getList(People.class,"data.people");
//        Log.e(peoples2.toString());
//        String animal = jsonUtil.getObject(String.class,"data.animal");
//        Log.e(animal);
//        Look look = jsonUtil.getObject(Look.class,"data.look");
//        Log.e(look.toString());
        Log.d("-----------------------------------------------------------------------------");

    }

    public static String getJsonFromAsset(Context context, String fileName){
        StringBuilder builder = new StringBuilder();
        try {
            InputStreamReader isr = new InputStreamReader(context.getAssets().open(fileName + ".json"),"UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line;

            while((line = br.readLine()) != null){
                builder.append(line);
            }
            br.close();
            isr.close();
        } catch (IOException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        }

        return builder.toString();
    }
}
