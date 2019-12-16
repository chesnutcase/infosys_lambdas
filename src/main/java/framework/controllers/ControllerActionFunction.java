package framework.controllers;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

@FunctionalInterface
public interface ControllerActionFunction {
    APIGatewayProxyResponseEvent run(APIGatewayProxyRequestEvent request, String... params);
}
