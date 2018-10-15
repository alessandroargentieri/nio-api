package com.alexmawashi.nio.jetty;

import com.alexmawashi.nio.servlets.GenericNioServlet;
import com.alexmawashi.nio.servlets.StatusBlockingServlet;
import com.alexmawashi.nio.servlets.upload.UploadServlet;
import com.alexmawashi.nio.utils.Endpoints;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class JaNio {

    private final static Logger log = Logger.getLogger(JaNio.class.getName());

    private int port;
    private List<Endpoints> endpointsList= new ArrayList<>();
    private Map<Class, String> servletsMap= new HashMap<>();

    public JaNio port(int port){
        this.port = port;
        return this;
    }

    public JaNio endpoints(Endpoints endpoints){
        endpointsList.add(endpoints);
        return this;
    }

    /** if you want to add some other custom Servlets */
    public JaNio addServlet(Class servletClass, String path){
        this.servletsMap.put(servletClass, path);
        return this;
    }

    public void start() throws Exception {

        //TODO: vedere come discriminare il path per l'upload
        Jetty jetty = new Jetty().port(port)
                                 .endpoint(UploadServlet.class, "/upload/*")
                                 .endpoint(GenericNioServlet.class, "/*")
                                 .endpoint(StatusBlockingServlet.class, "/status");
        if(!servletsMap.isEmpty()){
            for (Map.Entry<Class, String> entry : servletsMap.entrySet()) {
                jetty.endpoint(entry.getKey(), entry.getValue());
                log.info(LocalDateTime.now()+": "+this.getClass().getSimpleName()+" | "+ "Added servlet " + entry.getKey() + "at path " + entry.getValue());
            }
        }
        jetty.start();

    }

}
