package com.alexmawashi.nio.servlets;

import com.alexmawashi.nio.utils.RestHandler;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GenericNioServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //TODO: if it not matches manage the error
        RestHandler.getInstance().getEndpointIfMatches(request.getRequestURI()).act(request, response);
    }

}
