package com.quicktutorialz.nio;

import com.alexmawashi.nio.jetty.Jetty;
import com.alexmawashi.nio.servlets.GenericNioServlet;

/**
 * class with the main
 * it initialize the Jetty embedded server and set all the endpoints to it
 */
public class MainApplication {

    public static void main(String[] args) throws Exception {
        Endpoints endpoints = new Endpoints();
        new Jetty().port(8786)
                   .endpoint(GenericNioServlet.class, "/*")
                   .start();
    }


}
