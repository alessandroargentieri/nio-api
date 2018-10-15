package com.alexmawashi.nio.jetty;

import com.alexmawashi.nio.servlets.GenericNioServlet;
import com.alexmawashi.nio.servlets.StatusBlockingServlet;
import com.alexmawashi.nio.servlets.upload.UploadServlet;
import com.alexmawashi.nio.utils.Endpoints;

import java.util.ArrayList;
import java.util.List;

public class JaNio {

    private int port;
    private List<Endpoints> endpointsList= new ArrayList<>();

    public JaNio port(int port){
        this.port = port;
        return this;
    }

    public JaNio endpoints(Endpoints endpoints){
        endpointsList.add(endpoints);
        return this;
    }

    public void start() throws Exception {

        //TODO: vedere come discriminare il path per l'upload
        new Jetty().port(port)
                .endpoint(UploadServlet.class, "/upload/*")
                .endpoint(GenericNioServlet.class, "/*")
                .endpoint(StatusBlockingServlet.class, "/status")
                .start();

    }

}
