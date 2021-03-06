package com.quicktutorialz.nio;

import com.alexmawashi.nio.annotations.*;
import com.alexmawashi.nio.utils.*;
import com.quicktutorialz.nio.entities.UserData;
import com.quicktutorialz.nio.services.ReactiveService;
import com.quicktutorialz.nio.services.ReactiveServiceImpl;
import io.reactivex.Flowable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//multipart, form, file upload (nio?) https://stackoverflow.com/questions/2422468/how-to-upload-files-to-server-using-jsp-servlet
//valutare se anche l'input debba essere letto in maniera reattiva (adesso c'è startAsync + inputStream nel Flowable.just() )
//dependency injection (v2 of library)

public class ExampleEndpoints extends Endpoints{

    //inject services and components
    private final ReactiveService service = ReactiveServiceImpl.getInstance();

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Api(path = "/create/user/json", method = "POST", consumes = "application/json", produces = "application/json", description = "")
    private final Action createUserJson = (HttpServletRequest request, HttpServletResponse response) -> {
        final UserData userData = (UserData) getDataFromJsonBodyRequest(request, UserData.class);
        service.createUserCompletelyNIO(userData)
                .subscribe(res -> toJsonResponse(request, response, res),
                            t  -> toJsonResponse(request, response, t)    );
    };

    @Api(path = "/create/user/json/allreactive", method = "POST", consumes = "application/json", produces = "application/json", description = "")
    private final Action createUserJsonInputReactive = (HttpServletRequest request, HttpServletResponse response) -> {
        Flowable.just(request)
                .map( req -> (UserData) getDataFromJsonBodyRequest(req, UserData.class))
                .flatMap(userData -> service.createUserCompletelyNIO(userData))
                .subscribe(res -> toJsonResponse(request, response, res),
                            t  -> toJsonResponse(request, response, t)    );
    };


    @Api(path = "/create/user/text", method = "POST", consumes = "plain/text", produces = "plain/text", description = "")
    private final Action createUserText = (HttpServletRequest request, HttpServletResponse response) -> {
        final UserData userData = (UserData) getDataFromJsonBodyRequest(request, UserData.class);
        service.createUserCompletelyNIO(userData)
                .subscribe(res -> toTextResponse(request, response, res),
                            t  -> toTextResponse(request, response, t)    );
    };


    @Api(path = "/create/user/xml", method = "POST", consumes = "application/xml", produces = "application/xml", description = "")
    private final Action createUserXml = (HttpServletRequest request, HttpServletResponse response) -> {
        final UserData userData = (UserData) getDataFromXmlBodyRequest(request, UserData.class);
        service.createUserCompletelyNIO(userData)
                .subscribe(res -> toXmlResponse(request, response, res),
                            t  -> toXmlResponse(request, response, t)    );
    };


    @Api(path = "/create/user/xml/plus/header", method = "POST", consumes = "application/xml", produces = "application/xml", description = "")
    private final Action createUserXmlPlusHeader = (HttpServletRequest request, HttpServletResponse response) -> {
        final UserData userData = (UserData) getDataFromXmlBodyRequest(request, UserData.class);
        service.createUserCompletelyNIO(userData)
                .subscribe(res -> { response.setHeader("headerKey", "headerValue");
                                    response.setHeader("jwt", "aaaaaa.bbbbbbb.ccccccc");
                                    toXmlResponse(request, response, res);
                                  },
                            t  -> toXmlResponse(request, response, t)    );
    };


    @Api(path = "/get/greetings", method = "GET", consumes = "", produces = "text/plain", description = "")
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

        response.setHeader("Set-Cookie", cookie.toString());
        response.setHeader("myKey1", "myValue1");
        toTextResponse(request, response, headerToken);
    };

    @Api(path = "/get/path/variables/{name}/{surname}", method = "GET", consumes = "", produces = "text/plain", description = "")
    private final Action getPathVariables = (HttpServletRequest request, HttpServletResponse response) -> {

        Flowable.just(request)
                .map(req -> getPathVariables(req))
                .subscribe(map -> toTextResponse(request, response, map));
    };

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public ExampleEndpoints(){
        setEndpoint("/create/user/json", createUserJson);
        setEndpoint("/create/user/json/allreactive", createUserJsonInputReactive);
        setEndpoint("/create/user/text", createUserText);
        setEndpoint("/create/user/xml",  createUserXml);
        setEndpoint("/create/user/xml/plus/header",  createUserXmlPlusHeader);
        setEndpoint("/get/greetings",    getGreetings);
        setEndpoint("/get/path/variables/{name}/{surname}", getPathVariables);
    }
}
