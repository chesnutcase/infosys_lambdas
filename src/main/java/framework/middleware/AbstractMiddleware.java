package framework.middleware;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public abstract class AbstractMiddleware {
    public AbstractMiddleware(){

    }
    public abstract APIGatewayProxyResponseEvent handle(APIGatewayProxyRequestEvent event, Context context);
}
