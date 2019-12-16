package framework.routing;

import framework.controllers.RouteActionFunction;
import framework.middleware.AbstractMiddleware;

import java.util.*;

public class MiddlewareGroup {

    private Method method = Method.ALL;
    private String path;
    private List<Class<? extends AbstractMiddleware>> middlewareClasses;

    public MiddlewareGroup(Method method, String path) {
        this.method = method;
        this.path = path;
        this.middlewareClasses = new ArrayList<Class<? extends AbstractMiddleware>>();
    }

    public MiddlewareGroup middleware(Class<? extends AbstractMiddleware> middleware) {
        this.middlewareClasses.add(middleware);
        return this;
    }

    public MiddlewareGroup middleware(Class<? extends AbstractMiddleware>[] middlewares) {
        this.middlewareClasses.addAll(Arrays.asList(middlewares));
        return this;
    }

    public List<Class<? extends AbstractMiddleware>> getMiddlewareClasses() {
        return new ArrayList<Class<? extends AbstractMiddleware>>(middlewareClasses);
    }

    public Method getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }
}
