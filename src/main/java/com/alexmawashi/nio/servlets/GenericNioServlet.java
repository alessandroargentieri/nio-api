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

public class GenericNioServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String uri = request.getRequestURI();

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        RestHandler.getInstance().getEndpoint(uri).act(request, response);



        String jsonResponse = RestHandler.getInstance().getJsonResponse();
        nioResponse(request, response, jsonResponse);
    }


    private synchronized void nioResponse(HttpServletRequest request, HttpServletResponse response, String jsonResponse) throws IOException {

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

        ByteBuffer finalContent = ByteBuffer.wrap(resp.getBytes());
        AsyncContext async = request.startAsync();
        ServletOutputStream out = response.getOutputStream();
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
