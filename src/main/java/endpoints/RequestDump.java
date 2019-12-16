package endpoints;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;

public class RequestDump implements RequestHandler<APIGatewayV2ProxyRequestEvent, APIGatewayV2ProxyResponseEvent> {
    
    @Override
    public APIGatewayV2ProxyResponseEvent handleRequest(APIGatewayV2ProxyRequestEvent request,
        Context context) {
        
        APIGatewayV2ProxyResponseEvent responseEvent = new APIGatewayV2ProxyResponseEvent();
        Map<String, String> body = new HashMap<String, String>();
        body.put("request.getHttpMethod", request.getHttpMethod());
        body.put("request.getPath", request.getPath());
        body.put("request.getResource", request.getResource());
        body.put("request.getMultiValueHeaders", request.getMultiValueHeaders().toString());
        body.put("request.getHeaders", request.getHeaders().toString());
        body.put("request.getMultiValueQueryStringParameters", request.getMultiValueQueryStringParameters().toString());
        body.put("request.getPathParameters", request.getPathParameters().toString());
        body.put("request.getQueryStringParameters", request.getQueryStringParameters().toString());
        body.put("request.getRequestContext", request.getRequestContext().toString());
        // body.put("request.getStageVariables", request.getStageVariables().toString());
        ObjectMapper om = new ObjectMapper();
        try {
            String bodyString = om.writeValueAsString(body);
            responseEvent.setBody(bodyString);
            responseEvent.setStatusCode(200);
            return responseEvent;
        } catch (JsonProcessingException e) {
            responseEvent.setBody(e.toString());
            responseEvent.setStatusCode(500);
            return responseEvent;
        }
    }
}
