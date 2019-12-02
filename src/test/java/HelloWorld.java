import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class HelloWorld {
    @Test
    void testEnvironmentVars(){
        System.out.println("Hello World!");
        System.out.println(System.getenv("hibernate.connection.url"));
        System.out.println(System.getenv("hibernate.connection.username"));
        System.out.println(System.getenv("hibernate.connection.password"));

        int nd = 5;
        BigDecimal a = new BigDecimal("8.0");
        BigDecimal b = new BigDecimal("7.0");
        MathContext c = new MathContext(nd, RoundingMode.HALF_UP);
        System.out.println(a.divide(b, c));
    }

    @Test
    void testSession(){

    }
}
