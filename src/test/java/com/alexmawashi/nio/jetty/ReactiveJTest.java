package com.alexmawashi.nio.jetty;

import com.alexmawashi.nio.utils.Action;
import com.alexmawashi.nio.utils.Endpoints;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReactiveJTest {

    ReactiveJ reactiveJ;

    @Before
    public void setUp(){
        reactiveJ = new ReactiveJ();
    }

    @Test
    public void addEndpointsTest() throws Exception {

        reactiveJ.endpoints(new Endpoints() {
            @Override
            protected synchronized void setEndpoint(String path, Action action) {
                super.setEndpoint(path, action);
            }
        });
        reactiveJ.endpoints(new Endpoints() {
            @Override
            protected synchronized void setEndpoint(String path, Action action) {
                super.setEndpoint(path, action);
            }
        });

        reactiveJ.start();

    }

    @Test
    public void addCustomServletTest(){

    }

    @Test
    public void addCustomFilterTest(){

    }

}