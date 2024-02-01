package com.dongbu.dsm.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by 81700905 on 2017-09-06.
 */

public class GSONUtils {

    public static<T> String modelToJsonForLog(T model){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder = gsonBuilder.setPrettyPrinting();   //Sets pretty formatting
        Gson gson = gsonBuilder.create();

        return gson.toJson(model);
    }

    public static<T> String modelToJson(T model){
        Gson gson = new Gson();
        return gson.toJson(model);
    }

    public static <T> T changeGsonToBean(String gsonString, Class<T> cls) {
        Gson gson = new Gson();
        T t = gson.fromJson(gsonString, cls);
        return t;
    }

    public static <T> List<T> changeGsonToList(String gsonString, Class<T> cls) {
        Gson gson = new Gson();
        List<T> list = gson.fromJson(gsonString, new TypeToken<List<T>>() {
        }.getType());
        return list;
    }
}

