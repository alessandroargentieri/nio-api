package com.alexmawashi.nio.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RestHandler {

    private volatile String jsonResponse;
    private volatile String xmlResponse;
    private volatile String textResponse;
    private volatile Object resp;
    private volatile String responseType;

    private static RestHandler instance = null;
    public synchronized static RestHandler getInstance(){
        if(instance == null){
            instance = new RestHandler();
        }
        return instance;
    }

    private List<Endpoint> endpointList = new ArrayList<>();

    public RestHandler setEndpoint(String path, Action action){
        Endpoint endpoint = new Endpoint(path, action);
        endpointList.add(endpoint);
        return this;
    }

    public Action getEndpoint(String path){
        return endpointList.stream().filter(end -> end.getPath().equals(path)).limit(1).collect(Collectors.toList()).get(0).getAction();
    }

    public Action getEndpointIfMatches(String path){
        return endpointList.stream().filter(end -> urlMatch(path, end.getPath())).limit(1).collect(Collectors.toList()).get(0).getAction();

    }

    public String getJsonResponse() {
        return jsonResponse;
    }

    public void setJsonResponse(String jsonResponse) {
        this.jsonResponse = jsonResponse;
    }

    public void setResponse(Object response) {
        this.resp = response;
    }

    public void setResponseToText(Object response) {
        this.responseType = "text";
        this.textResponse = response.toString();
    }

    public void setResponseToJson(Object response) {
        this.responseType = "json";
        this.jsonResponse = JsonConverter.getInstance().getJsonOf(response);
    }

    public void setResponseToXml(Object response) {
        this.responseType = "xml";
        this.xmlResponse = XmlConverter.getInstance().getXmlOf(response);
    }

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public String getXmlResponse() {
        return xmlResponse;
    }

    public String getTextResponse() {
        return textResponse;
    }


    private boolean urlMatch(String requestUrl, String endpointUrl){

        if(!requestUrl.startsWith("/")) requestUrl = "/"+requestUrl;
        if(!endpointUrl.startsWith("/")) endpointUrl = "/"+endpointUrl;

        if(requestUrl.endsWith("/")) requestUrl = requestUrl.substring(0, requestUrl.length()-1);
        if(endpointUrl.endsWith("/")) endpointUrl = endpointUrl.substring(0, endpointUrl.length()-1);

        String[] split1 = requestUrl.split("/");
        String[] split2 = endpointUrl.split("/");
        if(split1.length != split2.length) return false;

        for(int i=0; i<split1.length; i++){
            String chunck1 = split1[i];
            String chunck2 = split2[i];

            if(!chunck1.equals(chunck2)){
                if(!chunck2.startsWith("{") && !chunck2.endsWith("}")) return false;
            }

        }
        return true;
    }



}
