package com.alexmawashi.nio.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author alessandroargentieri
 *
 * This abstract class must be extended by the programmer in order to define {@Endpoint}
 * The programmer must define various {@Action} and then must add each endpoint in the constructor with {@Endpoints#setEndpoint}
 * Each extended class must be set into the {@ReactiveJ#endpoints}
 */
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

    protected synchronized Map<String, String> getPathVariables(HttpServletRequest request){
        return handler.getPathVariables(request.getRequestURI());
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
