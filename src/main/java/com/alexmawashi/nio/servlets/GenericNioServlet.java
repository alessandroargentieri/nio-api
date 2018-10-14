package com.alexmawashi.nio.servlets;

import com.alexmawashi.nio.utils.RestHandler;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;

public class GenericNioServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //TODO: if it not matches manage the error
        RestHandler.getInstance().getEndpointIfMatches(request.getRequestURI()).act(request, response);
        nioResponse(request, response);
    }


    private synchronized void nioResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String resp=null;
        String responseType = RestHandler.getInstance().getResponseType();
        switch (responseType){
            case "text":
                response.setContentType("text/plain");
                resp = RestHandler.getInstance().getTextResponse();
                break;
            case "json":
                response.setContentType("application/json");
                resp = RestHandler.getInstance().getJsonResponse();
                break;
            case "xml":
                response.setContentType("application/xml");
                resp = RestHandler.getInstance().getXmlResponse();
                break;
        }
        Map<String, String> headers = RestHandler.getInstance().getHeaders();
        if( !headers.isEmpty() ){
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                response.setHeader(entry.getKey(), entry.getValue());
            }
        }


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
                getServletContext().log("Async Error", t);
                async.complete();
            }
        });
    }

}
