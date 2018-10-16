package com.quicktutorialz.nio;

import com.alexmawashi.nio.jetty.JaNio;
import com.alexmawashi.nio.jetty.Jetty;
import com.alexmawashi.nio.servlets.GenericNioServlet;
import com.alexmawashi.nio.servlets.StatusBlockingServlet;
import com.alexmawashi.nio.servlets.upload.UploadServlet;
import com.quicktutorialz.nio.custom.CustomFilter;
import com.quicktutorialz.nio.custom.CustomServlet;
import io.reactivex.Flowable;

/**
 * class with the main
 * it initialize the Jetty embedded server and set all the endpoints to it
 */
public class MainApplication {

    public static void main(String[] args) throws Exception {
        new JaNio().port(8786)
                .endpoints(new ExampleEndpoints())
                .addServlet(CustomServlet.class, "/custom/servlet")
                .addFilter(CustomFilter.class, "/*", Jetty.Dispatch.DEFAULT)
                .start();
    }




}
