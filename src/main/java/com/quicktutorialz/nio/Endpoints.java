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



//TODO: multipart, form, file upload (nio?) https://stackoverflow.com/questions/2422468/how-to-upload-files-to-server-using-jsp-servlet
//TODO: logging
//TODO: error responses
//TODO: tests
//TODO: extends Class

//TODO: dependency injection (v2 of library)

public class Endpoints {

    //inject services and components
    private final ReactiveService service = ReactiveServiceImpl.getInstance();

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Path("/create/user/json")
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    private final Action createUserJson = (HttpServletRequest request, HttpServletResponse response) -> {
        //get Json Body request using JsonConverter
        final UserData userData = (UserData) JsonConverter.getInstance().getDataFromBodyRequest(request, UserData.class);
        //business logic -> subscribe to response
        service.createUserCompletelyNIO(userData)
                .subscribe(res -> RestHandler.getInstance().setResponseToJson(res));
    };

    @Path("/create/user/text")
    @POST
    @Consumes("plain/text")
    @Produces("plain/text")
    private final Action createUserText = (HttpServletRequest request, HttpServletResponse response) -> {
        final UserData userData = (UserData) JsonConverter.getInstance().getDataFromBodyRequest(request, UserData.class);
        service.createUserCompletelyNIO(userData)
                .subscribe(res -> RestHandler.getInstance().setResponseToText(res));
    };


    @Api(path = "/create/user/xml", method = "POST", consumes = "application/xml", produces = "application/xml", description = "")
    private final Action createUserXml = (HttpServletRequest request, HttpServletResponse response) -> {
        final UserData userData = (UserData) XmlConverter.getInstance().getDataFromBodyRequest(request, UserData.class);
        service.createUserCompletelyNIO(userData)
                .subscribe(res -> RestHandler.getInstance().setResponseToXml(res));
    };


    @Path("/get/greetings")
    @GET
    @Produces("text/plain")
    private final Action getGreetings = (HttpServletRequest request, HttpServletResponse response) -> {
        String param = request.getParameter("name");
        String name = request.getQueryString();
        String headerToken = request.getHeader("header-token");

        Cookie cookie = new Cookie("CIAO", "12345");
        cookie.setComment("This is a comment");
        cookie.setDomain("localhost");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(9999999);
        cookie.setSecure(false);
        cookie.setVersion(12);

        RestHandler.getInstance().addHeader("mykey", "myvalue").addHeader("mykey2", "myvalue2").setCookie(cookie).setResponseToText(headerToken);

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
