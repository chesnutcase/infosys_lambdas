package framework.routing;

import framework.controllers.RouteActionFunction;
import framework.middleware.AbstractMiddleware;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Route {

    private Method method = Method.ALL;
    private String path;
    private Set<Class<? extends AbstractMiddleware>> middlewareClasses;
    private RouteActionFunction function;

    public Route(Method method, String path, RouteActionFunction function) {
        this.method = method;
        this.path = path;
        this.middlewareClasses = new HashSet<Class<? extends AbstractMiddleware>>();
        this.function = function;
    }
    
    public boolean matches(String givenPath){
        Pattern regexBlockFinderPattern = Pattern.compile("(?:\\{)([^\\{\\}]+)(?:\\})");
        List<String> ourBlocks = Arrays.asList(this.path.split("/"));
        List<String> theirBlocks = Arrays.asList(givenPath.split("/"));
        Iterator<String> ourIter = ourBlocks.iterator();
        Iterator<String> theirIter = theirBlocks.iterator();
        
        
        while(ourIter.hasNext() || theirIter.hasNext()){
            String ourBlock;
            String theirBlock;
            try {
                ourBlock = ourIter.next();
                theirBlock = theirIter.next();
            }catch(NoSuchElementException ex){
                return false;
            }
            if(!ourBlock.equals(theirBlock)){
                // check if contains regex block
                Matcher regexBlockFinder = regexBlockFinderPattern.matcher(ourBlock);
                if(!regexBlockFinder.find()){
                    return false;
                }else{
                    Pattern thisBlockPattern = Pattern.compile(ourBlock.replace(regexBlockFinder.group(0), regexBlockFinder.group(1)));
                    Matcher intraBlockFinder = thisBlockPattern.matcher(theirBlock);
                    if(!intraBlockFinder.find()){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public Route middleware(Class<? extends AbstractMiddleware> middleware) {
        this.middlewareClasses.add(middleware);
        return this;
    }

    public Route middleware(Class<? extends AbstractMiddleware>[] middlewares) {
        this.middlewareClasses.addAll(Arrays.asList(middlewares));
        return this;
    }

    public Set<Class<? extends AbstractMiddleware>> getMiddlewareClasses() {
        return new HashSet<Class<? extends AbstractMiddleware>>(middlewareClasses);
    }

    public RouteActionFunction getFunction() {
        return function;
    }

    public Method getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }
}
