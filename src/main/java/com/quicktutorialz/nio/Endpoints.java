package com.quicktutorialz.nio;

import com.alexmawashi.nio.annotations.*;
import com.alexmawashi.nio.utils.XmlConverter;
import com.quicktutorialz.nio.entities.UserData;
import com.quicktutorialz.nio.services.ReactiveService;
import com.quicktutorialz.nio.services.ReactiveServiceImpl;
import com.alexmawashi.nio.utils.Action;
import com.alexmawashi.nio.utils.JsonConverter;
import com.alexmawashi.nio.utils.RestHandler;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


//TODO: utils che recuperano path params, query params, header params, cookies
//TODO: utils che settano i cookie e l'header nella response
//TODO: multipart, form, file upload
//TODO: dependency injection
//TODO: logging
//TODO: error responses
//TODO: tests

public class Endpoints {

    //inject services and components
    private ReactiveService service = ReactiveServiceImpl.getInstance();

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Path("/create/user/json")
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    private Action createUserJson = (HttpServletRequest request, HttpServletResponse response) -> {
        //get Json Body request using JsonConverter
        UserData userData = (UserData) JsonConverter.getInstance().getDataFromBodyRequest(request, UserData.class);
        //business logic -> subscribe to response
        service.createUserCompletelyNIO(userData)
                .subscribe(res -> RestHandler.getInstance().setResponseToJson(res));
    };

    @Path("/create/user/text")
    @POST
    @Consumes("plain/text")
    @Produces("plain/text")
    private Action createUserText = (HttpServletRequest request, HttpServletResponse response) -> {
        UserData userData = (UserData) JsonConverter.getInstance().getDataFromBodyRequest(request, UserData.class);
        service.createUserCompletelyNIO(userData)
                .subscribe(res -> RestHandler.getInstance().setResponseToText(res));
    };


    @Path("/create/user/xml")
    @POST
    @Consumes("application/xml")
    @Produces("application/xml")
    private Action createUserXml = (HttpServletRequest request, HttpServletResponse response) -> {
        UserData userData = (UserData) XmlConverter.getInstance().getDataFromBodyRequest(request, UserData.class);
        service.createUserCompletelyNIO(userData)
                .subscribe(res -> RestHandler.getInstance().setResponseToXml(res));
    };


    @Path("/get/greetings")
    @GET
    @Produces("text/plain")
    private Action getGreetings = (HttpServletRequest request, HttpServletResponse response) -> {
        String param = request.getParameter("name");
        String name = request.getQueryString();
        String headerToken = request.getHeader("header-token");
        //Cookie cookie = request.getCookies()[0];
        //String cookieStr = cookie.getName() + " = " + cookie.getValue();
        RestHandler.getInstance().addHeader("mykey", "myvalue").addHeader("mykey2", "myvalue2").setResponseToText(headerToken);

    };

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    //associate paths to actions
    public Endpoints(){
        RestHandler.getInstance()
                .setEndpoint("/create/user/json", createUserJson)
                .setEndpoint("/create/user/text", createUserText)
                .setEndpoint("/create/user/xml",  createUserXml)
                .setEndpoint("/get/greetings",    getGreetings);
    }
}
