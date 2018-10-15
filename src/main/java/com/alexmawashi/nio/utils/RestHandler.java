package com.alexmawashi.nio.utils;

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

    //private volatile Map<String, String> headers = new HashMap<>();

    private static RestHandler instance = null;
    public synchronized static RestHandler getInstance(){
        if(instance == null){
            log.info(LocalTime.now() + ": creation of RestHandler");
            instance = new RestHandler();
        }
        return instance;
    }

    private final List<Endpoint> endpointList = new ArrayList<>();

    public final synchronized RestHandler setEndpoint(String path, Action action){
        final Endpoint endpoint = new Endpoint(path, action);
        endpointList.add(endpoint);
        log.info(LocalTime.now() + ": added action to path " + path);
        return this;
    }

    public final synchronized Action getEndpoint(final String path){
        return endpointList.stream().filter(end -> end.getPath().equals(path)).limit(1).collect(Collectors.toList()).get(0).getAction();
    }

    public final synchronized Action getEndpointIfMatches(final String path){
        return endpointList.stream().filter(end -> urlMatch(path, end.getPath())).limit(1).collect(Collectors.toList()).get(0).getAction();

    }



    /*public final synchronized RestHandler setHeaders(final Map<String, String> headers){
        this.headers = headers;
        return this;
    }

    public final synchronized RestHandler addHeader(final String key, final String value){
        headers.put(key, value);
        return this;
    }

    public final synchronized Map getHeaders(){
        return headers;
    }

    public final synchronized RestHandler setCookie(final Cookie cookie){
        StringBuilder builder = new StringBuilder();
        if(StringUtil.isBlank(cookie.getName()) || StringUtil.isBlank(cookie.getValue())) return this;

        builder.append(cookie.getName()).append("=").append(cookie.getValue()+"; ");
        builder.append( StringUtil.isNotBlank(cookie.getPath()) ? "Path="+cookie.getPath()+"; " : "" );
        builder.append( StringUtil.isNotBlank(cookie.getComment()) ? "Comment="+cookie.getComment()+"; " : "" );
        builder.append( StringUtil.isNotBlank(cookie.getDomain()) ? "Domain="+cookie.getDomain()+"; " : "" );

        builder.append( cookie.getMaxAge()!=0 ? "MaxAge="+cookie.getMaxAge()+"; " : "" );
        builder.append( cookie.getSecure() ? "Secure; " : "" );
        builder.append( cookie.getVersion()!=0 ? "Version="+cookie.getVersion()+"; " : "" );
        builder.append( cookie.isHttpOnly() ? "HttpOnly; " : "" );

        this.addHeader("Set-Cookie", builder.toString());
        //resp.addHeader("Set-Cookie","SID=31d4d96e407aad42; Path=/; Secure; HttpOnly");
        return this;
    }

    public final synchronized RestHandler setCookies(final Cookie[] cookies){
        for(Cookie cookie : cookies){
            this.setCookie(cookie);
        }
        return this;
    }*/

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
        /* TODO: lasciar incollare gli header e i cookie dalle action nella response in base al risultato della elaborazione reattiva
        final Map<String, String> headers = getHeaders();
        if( !headers.isEmpty() ){
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                response.setHeader(entry.getKey(), entry.getValue());
            }
        }
        */

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
                log.info(LocalDateTime.now().toString()+" | "+this.getClass().getName()+":"+t.toString());
                async.complete();
            }
        });
    }




}
