package com.quicktutorialz.nio;

import com.alexmawashi.nio.jetty.ReactiveJ;
import com.alexmawashi.nio.jetty.Jetty;
import com.quicktutorialz.nio.custom.CustomFilter;
import com.quicktutorialz.nio.custom.CustomServlet;

/**
 * class with the main
 * it initialize the Jetty embedded server and set all the endpoints to it
 */
public class MainApplication {

    public static void main(String[] args) throws Exception {
        new ReactiveJ().port(8786)
                       .endpoints(new ExampleEndpoints())
                       .addServlet(CustomServlet.class, "/custom/servlet")
                       .addFilter(CustomFilter.class, "/*", Jetty.Dispatch.DEFAULT)
                       .start();
    }




}
