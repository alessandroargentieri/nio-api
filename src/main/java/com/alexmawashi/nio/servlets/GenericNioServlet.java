package com.alexmawashi.nio.servlets;

import com.alexmawashi.nio.utils.RestHandler;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.util.logging.Logger;

public class GenericNioServlet extends HttpServlet {

    private final static Logger log = Logger.getLogger(GenericNioServlet.class.getName());

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            RestHandler.getInstance().getEndpointIfMatches(request.getRequestURI()).act(request, response);
        } catch (Exception e){
            log.info(LocalTime.now() + ": Internal Server Error (code 500) " + e.toString());
            request.setAttribute("internal-server-error", e.toString());
            RestHandler.getInstance().getEndpointIfMatches("/internal-server-error").act(request, response);
        }
    }

}
