package co.com.jestma.api;

import co.com.jestma.api.dto.LoginRequestDto;
import co.com.jestma.api.dto.SignUpDto;
import co.com.jestma.model.credential.Credential;
import co.com.jestma.model.restaurantexception.RestaurantThrowable;
import co.com.jestma.model.user.User;
import co.com.jestma.model.usersession.UserSession;
import co.com.jestma.usecase.user.UserUseCase;
import co.com.jestma.usecase.utility.ResponseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class Handler {
    private final UserUseCase useCase;
    private final ObjectMapper mapper;
    private final ResponseUtils<User> userResponseUtils;
    private final ResponseUtils<UserSession> loginResponseUtils;
    private final ResponseUtils<List<User>> usersResponseUtils;

    public Mono<ServerResponse> signUp(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(SignUpDto.class)
                .doOnNext(value -> log.info("::: Entering to signUp with requestBody {}", value))
                .map(signUpDto -> mapper.map(signUpDto, User.class))
                .flatMap(useCase::signup)
                .onErrorResume(throwable -> userResponseUtils.getResponseTypeError(User.builder().build(), throwable))
                .flatMap(value -> ServerResponse.status(value.getCode()).bodyValue(value))
                ;
    }

    public Mono<ServerResponse> login(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(LoginRequestDto.class)
                .doOnNext(value -> log.info("::: Entering to login with requestBody {}", value))
                .map(loginRequestDto -> mapper.map(loginRequestDto, Credential.class))
                .flatMap(useCase::login)
                .onErrorResume(throwable -> loginResponseUtils.getResponseTypeError(UserSession.builder().build(), throwable))
                .flatMap(value -> ServerResponse.status(value.getCode()).bodyValue(value))
                ;
    }

    public Mono<ServerResponse> getAllUsers(ServerRequest serverRequest) {
        return getUserAuthenticated(serverRequest)
                .flatMap(token -> useCase.findAll())
                .onErrorResume(throwable -> usersResponseUtils.getResponseTypeError(List.of(), throwable))
                .flatMap(value -> ServerResponse.status(value.getCode()).bodyValue(value))
                ;
    }

    private Mono<User> getUserAuthenticated(ServerRequest serverRequest) {
        return Mono.justOrEmpty(serverRequest.headers().firstHeader("Authorization"))
                .flatMap(useCase::findUserByToken)
                .switchIfEmpty(Mono.error(new RestaurantThrowable("401", "Unauthorized")));
    }
}
