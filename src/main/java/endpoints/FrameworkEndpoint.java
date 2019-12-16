package endpoints;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import controllers.HelloController;
import framework.routing.AbstractRouter;

public class FrameworkEndpoint implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    class MyRouter extends AbstractRouter{
        public MyRouter(){
            super();
            this.get("/hello", HelloController::hello);
            this.get("/hello/{}", HelloController::helloName);
        }

    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context) {
        MyRouter router = new MyRouter();

        return null;
    }
}