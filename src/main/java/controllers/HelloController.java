package controllers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public class HelloController {

    public static APIGatewayProxyResponseEvent hello(APIGatewayProxyRequestEvent event, Context context, String... args){
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setBody("hello");
        response.setStatusCode(200);
        return response;
    }

    public static APIGatewayProxyResponseEvent helloName(APIGatewayProxyRequestEvent event, Context context, String... args)
    {
        String name = args[0];
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setBody("hello " + name);
        response.setStatusCode(200);
        return response;
    }
}
