package framework.routing;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import framework.controllers.RouteActionFunction;
import framework.middleware.AbstractMiddleware;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.regex.Pattern;

public abstract class AbstractRouter {

    protected Set<Route> routes = new HashSet<>();
    protected Set<MiddlewareGroup> groups = new HashSet<>();

    public APIGatewayProxyResponseEvent processRequest(APIGatewayProxyRequestEvent event, Context context) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        String requestPath = event.getPath().replaceAll("/+$", "");
        // first find the matching route
        Optional<Route> matchingPathAndAction = this.routes.stream().filter(r -> r.getPath().equals(requestPath) && r.getMethod().name().compareToIgnoreCase(event.getHttpMethod()) == 0).findFirst();
        Optional<Route> matchingPathAnyAction = this.routes.stream().filter(r -> r.getPath().equals(requestPath) && r.getMethod() == Method.ALL).findFirst();
        Route matchingRoute = matchingPathAndAction.or(() -> matchingPathAnyAction).get();
        // build middleware instances
        String[] requestParts = requestPath.split("/");
        // first collect middleware instances from matching groups
        List<AbstractMiddleware> middlewares = new ArrayList();
        for (int i = 1; i < requestParts.length; i++) {
            String pathToMatch = Arrays.stream(Arrays.copyOfRange(requestParts, 0, i)).map(s -> "/" + s).reduce((s1, s2) -> (s1 + s2)).get();
            // first find matching groups
            Optional<MiddlewareGroup> matchingPathAndActionGroup = this.groups.stream().filter(r -> r.getPath().equals(requestPath) && r.getMethod().name().compareToIgnoreCase(event.getHttpMethod()) == 0).findFirst();
            Optional<MiddlewareGroup> matchingPathAnyActionGroup = this.groups.stream().filter(r -> r.getPath().equals(requestPath) && r.getMethod() == Method.ALL).findFirst();
            Optional<MiddlewareGroup> matchingGroupOptional = matchingPathAndActionGroup.or(() -> (matchingPathAnyActionGroup));
            if(matchingGroupOptional.isPresent()){
                MiddlewareGroup matchingGroup = matchingGroupOptional.get();
                List<Class<? extends AbstractMiddleware>> middlewareClasses = matchingGroup.getMiddlewareClasses();
                for(Class<? extends AbstractMiddleware> middlewareClass : middlewareClasses){
                    middlewares.add(this.constructMiddlewareFromClass(middlewareClass));
                }
            }
        }
        // then collect middleware instances from matched route
        for(Class<? extends AbstractMiddleware> middlewareClass : matchingRoute.getMiddlewareClasses()){
            middlewares.add(this.constructMiddlewareFromClass(middlewareClass));
        }
        // execute middleware in order, reject if necessary
        Iterator<AbstractMiddleware> iter = middlewares.iterator();
        while(iter.hasNext()){
            AbstractMiddleware middleware = iter.next();
            APIGatewayProxyResponseEvent terminableResponse = middleware.handle(event, context);
            if(terminableResponse != null){
                return terminableResponse;
            }
        }
        String[] params = {"hello", "world"};
        return matchingRoute.getFunction().processRequest(event, context, params);
    }
    
    protected String[] getParamValues(Route matchingRoute, String givenPath){
        matchingRoute.getPath();
        return null;
    }

    private AbstractMiddleware constructMiddlewareFromClass(Class<? extends AbstractMiddleware> middlewareClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor middlewareConstructor = middlewareClass.getConstructor();
        AbstractMiddleware middleware = (AbstractMiddleware) middlewareConstructor.newInstance();
        return middleware;
    }

    public Route get(String path, RouteActionFunction function) {
        Route route = new Route(Method.GET, path.replaceAll("/+$", ""), function);
        this.routes.add(route);
        return route;
    }

    public Route post(String path, RouteActionFunction function) {
        Route route = new Route(Method.POST, path.replaceAll("/+$", ""), function);
        this.routes.add(route);
        return route;
    }

    public Route put(String path, RouteActionFunction function) {
        Route route = new Route(Method.PUT, path.replaceAll("/+$", ""), function);
        this.routes.add(route);
        return route;
    }

    public Route delete(String path, RouteActionFunction function) {
        Route route = new Route(Method.DELETE, path.replaceAll("/+$", ""), function);
        this.routes.add(route);
        return route;
    }

    public Route any(String path, RouteActionFunction function) {
        Route route = new Route(Method.ALL, path.replaceAll("/+$", ""), function);
        this.routes.add(route);
        return route;
    }

    public MiddlewareGroup group(String path) {
        MiddlewareGroup group = new MiddlewareGroup(Method.ALL, path.replaceAll("/+$", ""));
        this.groups.add(group);
        return group;
    }

    public MiddlewareGroup group(String path, Method method) {
        MiddlewareGroup group = new MiddlewareGroup(method, path.replaceAll("/+$", ""));
        this.groups.add(group);
        return group;
    }
}
