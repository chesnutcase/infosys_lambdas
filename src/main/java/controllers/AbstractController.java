package controllers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractController {
    protected String resourceName;
    protected APIGatewayProxyRequestEvent event;
    protected Context context;

    public AbstractController(APIGatewayProxyRequestEvent event, Context context) {
        this.event = event;
        this.context = context;
    }

    private APIGatewayProxyResponseEvent makeMethodNotAllowedEvent(String methodName, String resourceName) throws JsonProcessingException {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(405);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        response.setHeaders(headers);
        ObjectMapper om = new ObjectMapper();
        ObjectNode body = om.createObjectNode();
        body.put("status", 405);
        body.put("error", "Method " + methodName + " not yet configured for resource controller " + resourceName);
        response.setBody(om.writeValueAsString(body));
        return response;
    }

    public APIGatewayProxyResponseEvent processRequest() throws JsonProcessingException {
        if(this.event.getHttpMethod().equals("GET")){
            return this.get();
        }else if(this.event.getHttpMethod().equals("POST")){
            return this.post();
        }else if(this.event.getHttpMethod().equals("PUT")){
            return this.put();
        }else if(this.event.getHttpMethod().equals("DELETE")){
            return this.delete();
        }else{
            return this.makeMethodNotAllowedEvent(this.event.getHttpMethod(), resourceName);
        }
    }

    protected APIGatewayProxyResponseEvent get() throws JsonProcessingException {
        return makeMethodNotAllowedEvent("GET", resourceName);
    }

    protected APIGatewayProxyResponseEvent post() throws JsonProcessingException {
        return makeMethodNotAllowedEvent("GET", resourceName);
    }

    protected APIGatewayProxyResponseEvent put() throws JsonProcessingException {

        return makeMethodNotAllowedEvent("GET", resourceName);
    }

    protected APIGatewayProxyResponseEvent delete() throws JsonProcessingException {

        return makeMethodNotAllowedEvent("GET", resourceName);
    }
}
