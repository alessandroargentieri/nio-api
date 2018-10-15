package com.alexmawashi.nio.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class Endpoints {

    protected RestHandler   handler       = RestHandler.getInstance();
    protected JsonConverter jsonConverter = JsonConverter.getInstance();
    protected XmlConverter  xmlConverter  = XmlConverter.getInstance();

    protected synchronized void setEndpoint(final String path, final Action action){
        handler.setEndpoint(path, action);
    }

    protected synchronized Object getDataFromJsonBodyRequest(HttpServletRequest request, Class clazz) throws IOException {
        return jsonConverter.getDataFromBodyRequest(request, clazz);
    }

    protected synchronized Object getDataFromXmlBodyRequest(HttpServletRequest request, Class clazz) throws IOException {
        return xmlConverter.getDataFromBodyRequest(request, clazz);
    }


    protected synchronized void toJsonResponse(HttpServletRequest request, HttpServletResponse response, Object resp) throws IOException {
        handler.toJsonResponse(request, response, resp);
    }

    protected synchronized void toXmlResponse(HttpServletRequest request, HttpServletResponse response, Object resp) throws IOException {
        handler.toXmlResponse(request, response, resp);
    }

    protected synchronized void toTextResponse(HttpServletRequest request, HttpServletResponse response, Object resp) throws IOException {
        handler.toTextResponse(request, response, resp);
    }
}
