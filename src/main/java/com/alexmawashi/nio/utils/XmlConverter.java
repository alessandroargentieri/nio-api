package com.alexmawashi.nio.utils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class XmlConverter<T> {

    private static XmlConverter instance = null;
    public synchronized static XmlConverter getInstance(){
        if(instance == null){
            instance = new XmlConverter();
        }
        return instance;
    }

    //TODO
    public T getDataFromBodyRequest(HttpServletRequest request, Class clazz) throws IOException {
        StringBuilder sb = new StringBuilder();
        String s;
        while ((s = request.getReader().readLine()) != null) {
            sb.append(s);
        }
        return null;
    }

    //TODO
    public String getXmlOf(Object object){
        return null;
    }


}
