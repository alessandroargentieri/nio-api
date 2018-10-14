package com.alexmawashi.nio.utils;

import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Util class which encapsulate the use of Gson class to parse Json messages from the request to a Java class
 * and from a Java class to a Json string message
 * @param <T>
 */
public class JsonConverter<T> {

    private static JsonConverter instance = null;
    public synchronized static JsonConverter getInstance(){
        if(instance == null){
            instance = new JsonConverter();
        }
        return instance;
    }

    Gson gson = new Gson();


    public synchronized T getDataFromBodyRequest(final HttpServletRequest request, final Class clazz) throws IOException {
        StringBuilder sb = new StringBuilder();
        String s;
        while ((s = request.getReader().readLine()) != null) {
            sb.append(s);
        }
        return (T) gson.fromJson(sb.toString(), clazz);
    }

    public synchronized String getJsonOf(final Object object){
        return gson.toJson(object);
    }


}
