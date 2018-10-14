package com.alexmawashi.nio.servlets.upload;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "UploadServlet", urlPatterns = {"/upload"}, asyncSupported=true)
public class UploadServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("We're inside");

        AsyncContext context = request.startAsync();
        // set up async listener
        /*context.addListener(new AsyncListener() {
            public void onComplete(AsyncEvent event) throws IOException {
                System.out.println("Inside onComplete of AsyncListener");
                event.getSuppliedResponse().getOutputStream().print("Complete");
            }

            public void onError(AsyncEvent event) {
                System.out.println(event.getThrowable());
            }

            public void onStartAsync(AsyncEvent event) {
                System.out.println("Inside onStartAsync of AsyncListener");
            }

            public void onTimeout(AsyncEvent event) {
                System.out.println("my asyncListener.onTimeout");
            }
        });*/
        ServletInputStream input = request.getInputStream();
        ReadListener readListener = new ReadListenerImpl(input, response, context);
        input.setReadListener(readListener);
    }

}
