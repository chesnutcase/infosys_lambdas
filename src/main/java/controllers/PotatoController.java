package controllers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import framework.controllers.AbstractController;

public class PotatoController extends AbstractController {
    public PotatoController(APIGatewayProxyRequestEvent event, Context context) {
        super(event, context);
        resourceName = "potato";
    }

    @Override
    protected APIGatewayProxyResponseEvent get(){
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(200);
        response.setBody("I'm a potato!");
        return response;
    }

    public String hello(int a, int b){
        return "Hello";
    }
}
