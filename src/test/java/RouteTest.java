import framework.routing.Route;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RouteTest {
    @Test
    void testRouteMatching(){
        Route r1 = new Route(null, "/hello", null);
        Assertions.assertFalse(r1.matches("/world"));
        Route r2 = new Route(null, "/hello/{\\w+}", null);
        Assertions.assertFalse(r2.matches("/hello"));
        Assertions.assertTrue(r2.matches("/hello/world"));
        Route r3 = new Route(null, "/hello/wo{\\w+}", null);
        Assertions.assertTrue(r3.matches("/hello/world"));
        Assertions.assertFalse(r3.matches("/hello/wo"));
        Assertions.assertFalse(r3.matches("/hello/world/nani"));
    }
}
