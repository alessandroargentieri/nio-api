package com.alexmawashi.nio.utils;

import com.alexmawashi.nio.annotations.Api;
import javax.servlet.AsyncContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class RestHandler {

    private final static Logger log = Logger.getLogger(RestHandler.class.getName());

    /* Singleton Design pattern implementation */
    private static RestHandler instance = null;
    public synchronized static RestHandler getInstance(){
        if(instance == null){
            log.info(LocalTime.now() + ": creation of RestHandler");
            instance = new RestHandler();
        }
        return instance;
    }

    private final List<Endpoint> endpointList = new ArrayList<>();

    @Api(path = "/not-found", method = "", consumes = "", produces = "", description = "Error action")
    private final Action notFoundError = (HttpServletRequest request, HttpServletResponse response) -> {
        toJsonResponse(request, response, new Error(404, request.getRequestURI(), "Page not found"));
    };
    @Api(path = "/internal-server-error", method = "", consumes = "", produces = "", description = "Internal server error")
    private final Action internalServerError = (HttpServletRequest request, HttpServletResponse response) -> {
        toJsonResponse(request, response, new Error(500, request.getRequestURI(), request.getAttribute("internal-server-error").toString()));
    };

    private RestHandler(){
        endpointList.add(new Endpoint("/not-found", notFoundError));
        endpointList.add(new Endpoint("/internal-server-error", internalServerError));
    }



    public final synchronized RestHandler setEndpoint(final String path, final Action action){
        final Endpoint endpoint = new Endpoint(path, action);
        endpointList.add(endpoint);
        log.info(LocalTime.now() + ": added action to path " + path);
        return this;
    }

    public final synchronized Action getEndpointIfMatches(final String path){
        List<Endpoint> endpoints = endpointList.stream().filter(end -> urlMatch(path, end.getPath())).limit(1).collect(Collectors.toList());//.get(0).getAction();
        return (endpoints.size()==1) ? endpoints.get(0).getAction(): getEndpointIfMatches("/not-found") ;
    }

    private synchronized boolean urlMatch(String requestUrl, String endpointUrl){

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


    public synchronized void toJsonResponse(HttpServletRequest request, HttpServletResponse response, Object resp) throws IOException {
        response.setContentType("application/json");
        nioResponse(request, response, JsonConverter.getInstance().getJsonOf(resp));
    }
    public synchronized void toXmlResponse(HttpServletRequest request, HttpServletResponse response, Object resp) throws IOException {
        response.setContentType("application/xml");
        nioResponse(request, response, XmlConverter.getInstance().getXmlOf(resp));
    }
    public synchronized void toTextResponse(HttpServletRequest request, HttpServletResponse response, Object resp) throws IOException {
        response.setContentType("text/plain");
        nioResponse(request, response, resp.toString());
    }

    private synchronized void nioResponse(HttpServletRequest request, HttpServletResponse response, final String resp) throws IOException {

        final ByteBuffer finalContent = ByteBuffer.wrap(resp.getBytes());
        final AsyncContext async = request.startAsync();
        final ServletOutputStream out = response.getOutputStream();
        out.setWriteListener(new WriteListener() {

            @Override
            public void onWritePossible() throws IOException {
                while (out.isReady()) {
                    if (!finalContent.hasRemaining()) {
                        response.setStatus(200);
                        async.complete();
                        return;
                    }
                    out.write(finalContent.get());
                }
            }

            @Override
            public void onError(Throwable t) {
                log.info(LocalDateTime.now().toString()+" | "+this.getClass().getSimpleName()+":"+t.toString());
                async.complete();
            }
        });
    }

}
