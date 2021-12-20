package co.com.jestma.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;


@Configuration
public class RouterRest {
@Bean
public RouterFunction<ServerResponse> routerFunction(Handler handler) {
    return route(POST("/api/v1/user/signup"), handler::signUp)
            .andRoute(GET("/api/v1/user/all"), handler::getAllUsers)
            .andRoute(POST("/api/v1/user/login"), handler::login)
            ;
    }
}
