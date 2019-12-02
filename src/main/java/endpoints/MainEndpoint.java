package endpoints;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import controllers.AbstractController;
import controllers.PotatoController;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainEndpoint implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private Map<String, Class<? extends AbstractController>> controllerMappings;

    public MainEndpoint() {
        super();
        this.controllerMappings = new HashMap();
        this.controllerMappings.put("potato", PotatoController.class);
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        LambdaLogger logger = context.getLogger();
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        response.setHeaders(headers);
        try {
            if (!event.getHeaders().get("Content-Type").split(";")[0].equals("multipart/form-data")) {
                response.setStatusCode(400);
                Map<String, String> body = new HashMap<>();
                logger.log(event.getHeaders().get("Content-Type").split(";")[0]);
                logger.log("multipart/form-data");
                logger.log(((Boolean) (event.getHeaders().get("Content-Type").split(";")[0].equals("multipart/form-data"))).toString());
                body.put("status", "400");
                body.put("error", "Expected multipart/form-data HTTP request, received " + event.getHeaders().get("Content-Type") + " instead");
                ObjectMapper om = new ObjectMapper();
                try {
                    response.setBody(om.writeValueAsString(body));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                return response;
            } else {
                String[] paths = event.getPath().split("/");
                Class<? extends AbstractController> matchingController = this.controllerMappings.get(paths[1]);
                logger.log(event.getHttpMethod());
                logger.log(event.getPath());
                Constructor controllerConstructor = matchingController.getConstructor(APIGatewayProxyRequestEvent.class, Context.class);
                AbstractController controller = (AbstractController) controllerConstructor.newInstance(event, context);
                return controller.processRequest();
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            ObjectMapper om = new ObjectMapper();
            ObjectNode body = om.createObjectNode();
            body.put("status", 500);
            body.put("error", e.toString());
            ArrayNode traceArrayNode = om.createArrayNode();
            List<TextNode> stackTraceArray = Arrays.stream(e.getStackTrace()).map(i -> body.textNode(i.toString())).collect(Collectors.toList());
            traceArrayNode.addAll(stackTraceArray);
            body.put("trace", traceArrayNode);
            try {
                response.setBody(om.writeValueAsString(body));
            } catch (JsonProcessingException ex) {
                ex.printStackTrace();
            }
            return response;
        }
    }
}
