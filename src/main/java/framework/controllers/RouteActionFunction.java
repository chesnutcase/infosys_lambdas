package framework.controllers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

@FunctionalInterface
public interface RouteActionFunction {
    APIGatewayProxyResponseEvent processRequest(APIGatewayProxyRequestEvent request, Context context, String... params);
}
