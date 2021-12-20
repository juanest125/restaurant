package co.com.jestma.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@Configuration
public class RouterRest {

    public static final String API_V_1_SUGGEST = "/api/v1/suggestion";

    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST("/api/v1/user/signup"), handler::signUp)
                .andRoute(GET("/api/v1/user/all"), handler::getAllUsers)
                .andRoute(POST("/api/v1/user/login"), handler::login)
                .andRoute(POST(API_V_1_SUGGEST), handler::createSuggestion)
                .andRoute(PATCH(API_V_1_SUGGEST), handler::updateSuggestion)
                .andRoute(GET(API_V_1_SUGGEST), handler::getAllSuggest)
                .andRoute(DELETE(API_V_1_SUGGEST), handler::deleteSuggestion)
                .andRoute(GET("/api/v1/random"), handler::getRandom)
                ;
    }
}
