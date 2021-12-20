package co.com.jestma.api;

import co.com.jestma.api.dto.LoginRequestDto;
import co.com.jestma.api.dto.SignUpDto;
import co.com.jestma.model.credential.Credential;
import co.com.jestma.model.randomnumber.RandomNumber;
import co.com.jestma.model.restaurantexception.RestaurantThrowable;
import co.com.jestma.model.suggestion.Suggestion;
import co.com.jestma.model.user.User;
import co.com.jestma.model.usersession.UserSession;
import co.com.jestma.usecase.randomnumber.RandomNumberUseCase;
import co.com.jestma.usecase.suggestion.SuggestionUseCase;
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
    private final UserUseCase userUseCase;
    private final SuggestionUseCase suggestionUseCase;
    private final RandomNumberUseCase randomNumberUseCase;
    private final ObjectMapper mapper;
    private final ResponseUtils<User> userResponseUtils;
    private final ResponseUtils<UserSession> loginResponseUtils;
    private final ResponseUtils<List<User>> usersResponseUtils;
    private final ResponseUtils<Suggestion> suggestionResponseUtils;
    private final ResponseUtils<List<Suggestion>> suggestionsResponseUtils;
    private final ResponseUtils<RandomNumber> randomNumberResponseUtils;

    public Mono<ServerResponse> signUp(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(SignUpDto.class)
                .doOnNext(value -> log.info("::: Entering to signUp with requestBody {}", value))
                .map(signUpDto -> mapper.map(signUpDto, User.class))
                .flatMap(userUseCase::signup)
                .onErrorResume(throwable -> userResponseUtils.getResponseTypeError(User.builder().build(), throwable))
                .flatMap(value -> ServerResponse.status(value.getCode()).bodyValue(value))
                ;
    }

    public Mono<ServerResponse> login(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(LoginRequestDto.class)
                .doOnNext(value -> log.info("::: Entering to login with requestBody {}", value))
                .map(loginRequestDto -> mapper.map(loginRequestDto, Credential.class))
                .flatMap(userUseCase::login)
                .onErrorResume(throwable -> loginResponseUtils.getResponseTypeError(UserSession.builder().build(), throwable))
                .flatMap(value -> ServerResponse.status(value.getCode()).bodyValue(value))
                ;
    }

    public Mono<ServerResponse> getAllUsers(ServerRequest serverRequest) {
        return getUserAuthenticated(serverRequest)
                .flatMap(user -> userUseCase.findAll())
                .onErrorResume(throwable -> usersResponseUtils.getResponseTypeError(List.of(), throwable))
                .flatMap(value -> ServerResponse.status(value.getCode()).bodyValue(value))
                ;
    }

    public Mono<ServerResponse> getAllSuggest(ServerRequest serverRequest) {
        var page = getIntParam(serverRequest, "page", "0");
        var size = getIntParam(serverRequest, "size", "5");
        return getUserAuthenticated(serverRequest)
                .flatMap(user ->  suggestionUseCase.getAll(page, size, user))
                .onErrorResume(throwable -> suggestionsResponseUtils.getResponseTypeError(List.of(), throwable))
                .flatMap(value -> ServerResponse.status(value.getCode()).bodyValue(value))
                ;
    }

    private int getIntParam(ServerRequest serverRequest, String paramName, String dft) {
        var param = serverRequest.queryParam(paramName).orElse(dft);
        return Integer.parseInt(param);
    }

    public Mono<ServerResponse> createSuggestion(ServerRequest serverRequest) {
        return getUserAuthenticated(serverRequest)
                .flatMap(user -> serverRequest.bodyToMono(Suggestion.class)
                        .flatMap(suggestion -> suggestionUseCase.create(suggestion, user))
                )
                .onErrorResume(throwable -> suggestionResponseUtils.getResponseTypeError(Suggestion.builder().build(), throwable))
                .flatMap(value -> ServerResponse.status(value.getCode()).bodyValue(value))
                ;
    }

    public Mono<ServerResponse> updateSuggestion(ServerRequest serverRequest) {
        return getUserAuthenticated(serverRequest)
                .flatMap(user -> serverRequest.bodyToMono(Suggestion.class)
                        .flatMap(suggestion -> suggestionUseCase.update(suggestion, user))
                )
                .onErrorResume(throwable -> suggestionResponseUtils.getResponseTypeError(Suggestion.builder().build(), throwable))
                .flatMap(value -> ServerResponse.status(value.getCode()).bodyValue(value))
                ;
    }

    public Mono<ServerResponse> deleteSuggestion(ServerRequest serverRequest) {
        return getUserAuthenticated(serverRequest)
                .flatMap(user -> serverRequest.bodyToMono(Suggestion.class)
                        .flatMap(suggestion -> suggestionUseCase.delete(suggestion.getId(), user))
                )
                .onErrorResume(throwable -> suggestionResponseUtils.getResponseTypeError(Suggestion.builder().build(), throwable))
                .flatMap(value -> ServerResponse.status(value.getCode()).bodyValue(value))
                ;
    }

    public Mono<ServerResponse> getRandom(ServerRequest serverRequest) {
        var min = getIntParam(serverRequest, "min", "0");
        var max = getIntParam(serverRequest, "max", "100");
        var count = getIntParam(serverRequest, "count", "1");
        return getUserAuthenticated(serverRequest)
                .doOnNext(user -> log.info("::: Entering to getRandom with min={}, max={}, count={}", min, max, count))
                .flatMap(user -> randomNumberUseCase.getRandomNumber(min, max, count))
                .onErrorResume(throwable -> randomNumberResponseUtils.getResponseTypeError(RandomNumber.builder().build(), throwable))
                .doOnNext(value -> log.info("::: Response from getRandom ->  {}", value))
                .flatMap(value -> ServerResponse.status(value.getCode()).bodyValue(value))
                ;
    }

    private Mono<User> getUserAuthenticated(ServerRequest serverRequest) {
        return Mono.justOrEmpty(serverRequest.headers().firstHeader("Authorization"))
                .flatMap(userUseCase::findUserByToken)
                .switchIfEmpty(Mono.error(new RestaurantThrowable("401", "Unauthorized")));
    }
}
