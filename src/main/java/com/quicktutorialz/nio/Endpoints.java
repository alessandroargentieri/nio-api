package com.quicktutorialz.nio;

import com.quicktutorialz.nio.entities.UserData;
import com.quicktutorialz.nio.services.ReactiveService;
import com.quicktutorialz.nio.services.ReactiveServiceImpl;
import com.alexmawashi.nio.utils.Action;
import com.alexmawashi.nio.utils.JsonConverter;
import com.alexmawashi.nio.utils.RestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Endpoints {

    //inject services and components
    private ReactiveService service = ReactiveServiceImpl.getInstance();

    //create actions for each endpoint
    private Action createUserJson = (HttpServletRequest request, HttpServletResponse response) -> {
        //get Json Body request using JsonConverter
        UserData userData = (UserData) JsonConverter.getInstance().getDataFromBodyRequest(request, UserData.class);
        //business logic -> subscribe to response
        service.createUserCompletelyNIO(userData)
                .subscribe(res -> RestHandler.getInstance().setResponseToJson(res));
    };

    //create actions for each endpoint
    private Action createUserText = (HttpServletRequest request, HttpServletResponse response) -> {
        //get Json Body request using JsonConverter
        UserData userData = (UserData) JsonConverter.getInstance().getDataFromBodyRequest(request, UserData.class);
        //business logic -> subscribe to response
        service.createUserCompletelyNIO(userData)
                .subscribe(res -> RestHandler.getInstance().setResponseToText(res));
    };

    //associate paths to actions
    public Endpoints(){
        RestHandler.getInstance()
                .setEndpoint("/create/user/json", createUserJson)
                .setEndpoint("/create/user/text", createUserText);

    }
}
